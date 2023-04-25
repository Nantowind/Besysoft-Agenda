package com.besysoft.agendaspring.controladores;

import com.besysoft.agendaspring.entidades.Empresa;
import com.besysoft.agendaspring.entidades.Persona;
import com.besysoft.agendaspring.exepciones.MiException;
import com.besysoft.agendaspring.servicios.PersonaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
@RequestMapping("/api/persona")
public class PersonaControlador {

    @Autowired
    private PersonaServicio personaServicio;

    @GetMapping("/registrar")
    public String registrar(){
        return "persona";
    }

    @GetMapping("/lista")
    public String lista(ModelMap modelo){
        cargarModel(modelo);
        return "TablaPersonas";
    }

    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, ModelMap modelo){
        modelo.put("persona", personaServicio.getOne(id));

        return "ModificarPersonas";
    }

    @GetMapping("/eliminar/{idPersona}")
    public String eliminarPersona(@PathVariable String idPersona) {
        try {
            personaServicio.eliminarPersona(idPersona);
            return "redirect:/api/persona/lista";
        } catch (MiException ex) {
            Logger.getLogger(PersonaControlador.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            return "redirect:/api/persona/lista";
        }
    }



    @PostMapping("/modificar/{id}")
    public String modificar(ModelMap modelo,@PathVariable String id,String nombre,
                            String apellido,String ciudad,String telefono,String email){

        cargarModel(modelo);
        try {
            personaServicio.modificarPersona(id,nombre,apellido,ciudad,telefono,email);
            return "redirect:../lista";
        }catch (MiException ex){
            Logger.getLogger(PersonaControlador.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            return "TablaPersonas";
        }


    }



    @PostMapping("/registro")
    public String registro(@RequestParam String nombre, String apellido ,String ciudad,String telefono,String email) {
       try {
           personaServicio.crearPersona(nombre,apellido,ciudad,telefono,email);
           return "redirect:/api/persona/lista";
       }catch (MiException ex){
           Logger.getLogger(PersonaControlador.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
           return "redirect:/api/persona/registrar";
       }

    }
    private void cargarModel(ModelMap modelo){
        List<Persona> personas = personaServicio.listarPersonas();
        modelo.addAttribute("personas",personas);
    }
}
