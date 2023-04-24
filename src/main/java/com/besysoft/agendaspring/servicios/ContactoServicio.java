package com.besysoft.agendaspring.servicios;

import com.besysoft.agendaspring.entidades.Contacto;
import com.besysoft.agendaspring.entidades.Empresa;
import com.besysoft.agendaspring.entidades.Persona;
import com.besysoft.agendaspring.exepciones.MiException;
import com.besysoft.agendaspring.repositorios.ContactoRepositorio;
import com.besysoft.agendaspring.repositorios.EmpresaRepositorio;
import com.besysoft.agendaspring.repositorios.PersonaRepositorio;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ContactoServicio {

    @Autowired
    ContactoRepositorio contactoRepositorio;
    @Autowired
    PersonaRepositorio personaRepositorio;
    @Autowired
    EmpresaRepositorio empresaRepositorio;

    @Transactional
    public Contacto crearContacto(String idPersona, String idEmpresa) throws MiException {
        verificarDatosCrearContacto(idPersona);
        Persona persona = personaRepositorio.findById(idPersona).orElseThrow(() -> new RuntimeException("crearContacto: Persona no encontrada"));

        Contacto contacto = new Contacto();
        contacto.setPersona(persona);

        if (idEmpresa != null && !idEmpresa.isEmpty()) {
            Optional<Empresa> empresaOpt = empresaRepositorio.findById(idEmpresa);
            if (empresaOpt.isPresent()) {
                contacto.setEmpresa(empresaOpt.get());
            } else {
                System.out.println("crearContacto: Empresa no encontrada, se creará el contacto sin empresa asociada");
            }
        } else {
            System.out.println("crearContacto: idEmpresa es nulo o vacío, se creará el contacto sin empresa asociada");
        }

        contactoRepositorio.save(contacto);
        return contacto;
    }
    @Transactional
    public Contacto modificarContacto(String idContacto,String idPersona,String idEmpresa) throws MiException {
        verificarDatosModificarontacto(idContacto,idPersona,idEmpresa);
        Persona persona = personaRepositorio.findById(idPersona).orElseThrow(() -> new RuntimeException("modificarContacto: Persona no encontrada"));
        Empresa empresa = empresaRepositorio.findById(idEmpresa).orElseThrow(() -> new RuntimeException("modificarContacto: Empresa no encontrada"));
        Contacto contacto= contactoRepositorio.findById(idContacto).orElseThrow(() -> new RuntimeException("modificarContacto: Contacto no encontrado"));

        contacto.setEmpresa(empresa);
        contacto.setPersona(persona);

        contactoRepositorio.save(contacto);
        return contacto;
    }
    public List<Contacto> listarContactos(){
        List <Contacto> contactos = new ArrayList<>();
        contactos = contactoRepositorio.findAll();

        return contactos;
    }


    //Verificacion de que ningun dato este vacio.
    private void verificarDatosCrearContacto(String idPersona) throws MiException {
        if (idPersona == null || idPersona.isEmpty()) {
            throw new MiException("crearContacto: Todos los campos son requeridos");
        }
    }
    private void verificarDatosModificarontacto(String idContacto,String idPersona,String idEmpresa) throws MiException {
        if (idContacto == null || idContacto.isEmpty() || idPersona == null || idPersona.isEmpty() || idEmpresa == null || idEmpresa.isEmpty()) {
            throw new MiException("modificarContacto: Todos los campos son requeridos");
        }
    }
}
