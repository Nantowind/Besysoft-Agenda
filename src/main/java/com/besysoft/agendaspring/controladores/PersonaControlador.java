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
            modelo.addAttribute("error", "No se pudo obtener la lista de personas.");

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

    // A continuación, las funciones relacionadas con las solicitudes de la API de Postman

    // Agrega una persona
    @PostMapping("/agregar")
    @ResponseBody
    public ResponseEntity<?> agregarPersona(@RequestBody Persona persona) {
        try {
            Persona nuevaPersona = personaServicio.crearPersona(persona.getNombre(), persona.getApellido(), persona.getCiudad(), persona.getTelefono(), persona.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaPersona);
        } catch (MiException ex) {
            Logger.getLogger(PersonaControlador.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al crear la persona: " + ex.getMessage());
        }
    }

    // Lista todas las personas
    @GetMapping("/listar")
    @ResponseBody
    public ResponseEntity<?> listarPersonas() {
        try {
            List<Persona> personas = personaServicio.listarPersonas();
            return ResponseEntity.ok(personas);
        } catch (MiException ex) {
            Logger.getLogger(PersonaControlador.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener la lista de personas: " + ex.getMessage());
        }
    }

    // Busca personas por nombre
    @GetMapping("/buscarNombre")
    @ResponseBody
    public ResponseEntity<List<Persona>> buscarPersonaPorNombre(@RequestParam String nombre) {
        try {
            List<Persona> personas = personaServicio.buscarPorNombre(nombre);
            return ResponseEntity.ok(personas);
        } catch (MiException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

    // Busca personas por ciudad
    @GetMapping("/buscarCiudad")
    @ResponseBody
    public ResponseEntity<List<Persona>> buscarPersonaPorCiudad(@RequestParam String ciudad) {
        try {
            List<Persona> personas = personaServicio.buscarPorCiudad(ciudad);
            return ResponseEntity.ok(personas);
        } catch (MiException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }
    // Busca personas por nombre en ciudades
    @GetMapping("/buscarNombreEnCiudades")
    @ResponseBody
    public ResponseEntity<List<Persona>> buscarPersonaPorNombreYCiudades(@RequestParam String nombre, @RequestParam List<String> ciudades) {
        List<Persona> personas = personaServicio.buscarPorNombreYCiudades(nombre, ciudades);
        return ResponseEntity.ok(personas);
    }

    // Modifica una persona usando la API
    @PutMapping("/modificarPersona")
    public ResponseEntity<String> modificarPersona(@RequestBody Persona personaActualizada) {
        try {
            personaServicio.modificarPersona(personaActualizada.getId(), personaActualizada.getNombre(), personaActualizada.getApellido(), personaActualizada.getCiudad(), personaActualizada.getTelefono(), personaActualizada.getEmail());
            return ResponseEntity.ok("Persona modificada correctamente");
        } catch (MiException ex) {
            Logger.getLogger(PersonaControlador.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al modificar la persona");
        }
    }

    // Elimina una persona usando la API
    @ResponseBody
    public ResponseEntity<String> eliminarPersona(@PathVariable String id) {
        try {
            personaServicio.eliminarPersona(id);
            return ResponseEntity.ok("La persona con ID " + id + " ha sido eliminada correctamente.");
        } catch (MiException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar la persona con ID " + id + ": " + e.getMessage());
        }
    }

    // Carga el modelo con la lista de personas
    private void cargarModel(ModelMap modelo) throws MiException {
        List<Persona> personas = personaServicio.listarPersonas();
        modelo.addAttribute("personas", personas);
    }


}
