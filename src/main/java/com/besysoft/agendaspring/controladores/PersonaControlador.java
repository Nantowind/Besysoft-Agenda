package com.besysoft.agendaspring.controladores;

import com.besysoft.agendaspring.entidades.Empresa;
import com.besysoft.agendaspring.entidades.Persona;
import com.besysoft.agendaspring.exepciones.MiException;
import com.besysoft.agendaspring.servicios.PersonaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @PostMapping("/registro")
    public String registro(@RequestParam String nombre, String apellido ,String ciudad,String telefono,String email) {
       try {
           personaServicio.crearPersona(nombre,apellido,ciudad,telefono,email);
           return "persona";
       }catch (MiException ex){
           Logger.getLogger(PersonaControlador.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
           return "persona";
       }

    }
    private void cargarModel(ModelMap modelo){
        List<Persona> personas = personaServicio.listarPersonas();
        modelo.addAttribute("personas",personas);
    }
}
