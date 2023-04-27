package com.besysoft.agendaspring.apicontrolador;

import com.besysoft.agendaspring.controladores.PersonaControlador;
import com.besysoft.agendaspring.entidades.Persona;
import com.besysoft.agendaspring.exepciones.MiException;
import com.besysoft.agendaspring.servicios.PersonaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1/persona")
public class PersonaApiControlador {
    @Autowired
    private PersonaServicio personaServicio;

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
    @DeleteMapping("/eliminar/{id}")
    @ResponseBody
    public ResponseEntity<String> eliminarPersona(@PathVariable String id) {
        try {
            personaServicio.eliminarPersona(id);
            return ResponseEntity.ok("La persona con ID " + id + " ha sido eliminada correctamente.");
        } catch (MiException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar la persona con ID " + id + ": " + e.getMessage());
        }
    }

}
