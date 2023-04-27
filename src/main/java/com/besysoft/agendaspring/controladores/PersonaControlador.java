package com.besysoft.agendaspring.controladores;

import com.besysoft.agendaspring.entidades.Empresa;
import com.besysoft.agendaspring.entidades.Persona;
import com.besysoft.agendaspring.exepciones.MiException;
import com.besysoft.agendaspring.servicios.PersonaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
@PreAuthorize("hasRole('USER')")
@RequestMapping("/api/persona")
public class PersonaControlador {
    @Autowired
    private PersonaServicio personaServicio;

    // Muestra el formulario de registro de personas
    @GetMapping("/registrar")
    public String registrar() {
        return "persona";
    }

    // Muestra la lista de personas
    @GetMapping("/lista")
    public String lista(ModelMap modelo){
        try {
            cargarModel(modelo);
            return "TablaPersonas";
        } catch (MiException ex) {
            // Registrar el error en la consola para obtener más información
            System.err.println("Error al obtener la lista de personas: " + ex.getMessage());
            ex.printStackTrace();

            // Agregar un mensaje de error al modelo para mostrar en la vista
            modelo.addAttribute("error", "No se pudo obtener la lista de personas o esta esta vacia.");

            return "TablaPersonas";
        }
    }



    // Muestra el formulario de modificación de personas
    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, ModelMap modelo) {
        modelo.put("persona", personaServicio.getOne(id));
        return "ModificarPersonas";
    }

    // Elimina una persona
    @GetMapping("/eliminar/{idPersona}")
    public String eliminarPersona(@PathVariable String idPersona, RedirectAttributes redirectAttrs) {
        try {
            personaServicio.eliminarPersona(idPersona);
            return "redirect:/api/persona/lista";
        } catch (MiException ex) {
            redirectAttrs.addFlashAttribute("error", ex.getMessage());
            Logger.getLogger(EmpresaControlador.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            return "redirect:/api/persona/lista";
        }
    }

    // Muestra el formulario de búsqueda de personas por nombre
    @GetMapping("/buscar")
    public String buscar() {
        return "BuscarPersonas";
    }

    // Busca personas por nombre
    @GetMapping("/buscarPorNombre")
    public String buscarPorNombre(@RequestParam String nombre, Model model) {
        try {
            List<Persona> personas = personaServicio.buscarPorNombre(nombre);
            model.addAttribute("personas", personas);
        } catch (MiException ex) {
            Logger.getLogger(EmpresaControlador.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return "resultadosBusqueda";
    }

    // Busca personas por ciudad
    @GetMapping("/buscarPorCiudad")
    public String buscarPorCiudad(@RequestParam String ciudad, Model model) {
        try {
            List<Persona> personas = personaServicio.buscarPorCiudad(ciudad);
            model.addAttribute("personas", personas);
        } catch (MiException ex) {
            Logger.getLogger(EmpresaControlador.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return "resultadosBusqueda";
    }

    // Busca personas por nombre y ciudad
    @GetMapping("/buscarPorNombreYCiudades")
    public String buscarPorNombreYCiudades(@RequestParam String nombre_ciudad, @RequestParam String ciudades, Model model) {
        List<String> listaCiudades = Arrays.asList(ciudades.split(","));
        List<Persona> personas = personaServicio.buscarPorNombreYCiudades(nombre_ciudad, listaCiudades);
        model.addAttribute("personas", personas);
        return "resultadosBusqueda";
    }


    // Modifica una persona
    @PostMapping("/modificar/{id}")
    public String modificar(ModelMap modelo, @PathVariable String id, String nombre,
                            String apellido, String ciudad, String telefono, String email) {
        try {
            cargarModel(modelo);
            personaServicio.modificarPersona(id, nombre, apellido, ciudad, telefono, email);
            return "redirect:../lista";
        } catch (MiException ex) {
            Logger.getLogger(PersonaControlador.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            return "TablaPersonas";
        }
    }

    // Registra una persona
    @PostMapping("/registro")
    public String registro(@RequestParam String nombre, String apellido, String ciudad, String telefono, String email) {
        try {
            personaServicio.crearPersona(nombre, apellido, ciudad, telefono, email);
            return "redirect:/api/persona/lista";
        } catch (MiException ex) {
            Logger.getLogger(PersonaControlador.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            return "redirect:/api/persona/registrar";
        }
    }


    // Carga el modelo con la lista de personas
    private void cargarModel(ModelMap modelo) throws MiException {
        List<Persona> personas = personaServicio.listarPersonas();
        modelo.addAttribute("personas", personas);
    }


}
