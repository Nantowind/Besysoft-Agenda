package com.besysoft.agendaspring.apicontrolador;


import com.besysoft.agendaspring.controladores.ContactoControlador;
import com.besysoft.agendaspring.entidades.Contacto;
import com.besysoft.agendaspring.entidades.ContactoDTO;
import com.besysoft.agendaspring.entidades.Empresa;
import com.besysoft.agendaspring.entidades.Persona;


import com.besysoft.agendaspring.exepciones.MiException;
import com.besysoft.agendaspring.exepciones.ResourceNotFoundException;
import com.besysoft.agendaspring.repositorios.ContactoRepositorio;
import com.besysoft.agendaspring.repositorios.EmpresaRepositorio;
import com.besysoft.agendaspring.repositorios.PersonaRepositorio;
import com.besysoft.agendaspring.servicios.ContactoServicio;
import com.besysoft.agendaspring.servicios.EmpresaServicio;
import com.besysoft.agendaspring.servicios.PersonaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1/contacto")
public class ContactoApiControlador {

    @Autowired
    ContactoServicio contactoServicio;
    @Autowired
    PersonaRepositorio personaRepositorio;

    @Autowired
    EmpresaRepositorio empresaRepositorio;

    @Autowired
    ContactoRepositorio contactoRepositorio;




    @PostMapping("/crear")
    public ResponseEntity<?> createContacto(@RequestBody ContactoDTO contactoDTO) {
        try {
            Contacto contacto = contactoServicio.crearContacto(contactoDTO.getIdPersona(), contactoDTO.getIdEmpresa());
            return ResponseEntity.status(HttpStatus.CREATED).body(contacto);
        } catch (Exception ex) {
            Logger.getLogger(ContactoApiControlador.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al crear la empresa: " + ex.getMessage());

        }
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Map<String, String>> eliminar(@PathVariable String id) {
        Map<String, String> response = new HashMap<>();
        try {
            contactoServicio.eliminarContacto(id);
            response.put("mensaje", "Contacto eliminado exitosamente");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (MiException ex) {
            Logger.getLogger(ContactoApiControlador.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            response.put("error", "No se pudo eliminar el contacto: " + ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/lista")
    public ResponseEntity<Object> lista() {
        List<Contacto> contactos = contactoServicio.listarContactos();

        if (contactos.isEmpty()) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "No hay contactos creados.");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(contactos, HttpStatus.OK);
    }

    @PostMapping("/modificar")
    public ResponseEntity<?> modificarContactoPost(@RequestBody Map<String, String> body) {
        try {
            String id = body.get("idContacto");
            String idPersona = body.get("idPersona");
            String idEmpresa = body.get("idEmpresa");

            contactoServicio.modificarContacto(id, idPersona, idEmpresa);

            return ResponseEntity.status(HttpStatus.CREATED).body(contactoServicio.getOne(id));

        } catch (MiException ex) {
            Logger.getLogger(ContactoControlador.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al modificar el contacto : " + ex.getMessage());

        }
    }



}
