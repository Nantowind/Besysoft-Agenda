package com.besysoft.agendaspring.apicontrolador;


import com.besysoft.agendaspring.entidades.Contacto;
import com.besysoft.agendaspring.entidades.ContactoDTO;
import com.besysoft.agendaspring.entidades.Empresa;
import com.besysoft.agendaspring.entidades.Persona;


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
            Contacto contacto = contactoServicio.createContacto(contactoDTO.getIdPersona(), contactoDTO.getIdEmpresa());
            return ResponseEntity.status(HttpStatus.CREATED).body(contacto);
        } catch (Exception ex) {
            Logger.getLogger(ContactoApiControlador.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al crear la empresa: " + ex.getMessage());

        }
    }



}
