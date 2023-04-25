package com.besysoft.agendaspring.servicios;

import com.besysoft.agendaspring.entidades.Contacto;
import com.besysoft.agendaspring.entidades.Persona;
import com.besysoft.agendaspring.exepciones.MiException;
import com.besysoft.agendaspring.repositorios.ContactoRepositorio;
import com.besysoft.agendaspring.repositorios.PersonaRepositorio;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PersonaServicio {

    @Autowired
    private PersonaRepositorio personaRepositorio;
    @Autowired
    private ContactoRepositorio contactoRepositorio;

    @Transactional
    public Persona  crearPersona(String nombre,String apellido,String ciudad,String telefono,String mail) throws MiException {
        verificarDatosCrearPersona(nombre, apellido, ciudad, telefono, mail);
        Persona persona = new Persona();
        persona.setNombre(nombre);
        persona.setApellido(apellido);
        persona.setCiudad(ciudad);
        persona.setTelefono(telefono);
        persona.setEmail(mail);

        personaRepositorio.save(persona);
        return persona;
    }
    @Transactional
    public Persona modificarPersona(String idPersona,String nombre,String apellido,String ciudad,String telefono,String mail) throws MiException {
        verificarDatosModificarPersona( idPersona, nombre, apellido, ciudad, telefono, mail);
        Persona persona = personaRepositorio.findById(idPersona).orElseThrow(() -> new RuntimeException("modificarPersona: Persona no encontrada"));

        persona.setNombre(nombre);
        persona.setApellido(apellido);
        persona.setCiudad(ciudad);
        persona.setTelefono(telefono);
        persona.setEmail(mail);

        personaRepositorio.save(persona);

        return persona;
    }
    @Transactional
    public void eliminarPersona(String idPersona) throws MiException {
        verificarDatosEliminarPersona(idPersona);
        verificarSiPersonaTieneContacto(idPersona);
        personaRepositorio.deleteById(idPersona);
    }

    //listar y obtener
    public List<Persona> listarPersonas(){
        List <Persona> personas = new ArrayList<>();
        personas = personaRepositorio.findAll();

        return personas;
    }
    public Persona getOne(String id){
        return personaRepositorio.getOne(id);
    }

    //verficar datos nulos o vacios
    public void verificarDatosCrearPersona(String nombre,String apellido,String ciudad,String telefono,String mail) throws MiException {
        if (nombre == null || nombre.isEmpty() || apellido == null || apellido.isEmpty() || ciudad == null || ciudad.isEmpty() ||
                telefono == null || telefono.isEmpty() || mail == null || mail.isEmpty()) {
            throw new MiException("crearPersona: Todos los campos son requeridos");
        }
    }
    public void verificarDatosModificarPersona(String idPersona,String nombre,String apellido,String ciudad,String telefono,String mail) throws MiException {
        if (idPersona == null || idPersona.isEmpty() ||nombre == null || nombre.isEmpty() || apellido == null || apellido.isEmpty() || ciudad == null || ciudad.isEmpty() ||
                telefono == null || telefono.isEmpty() || mail == null || mail.isEmpty()) {
            throw new MiException("modificarPersona: Todos los campos son requeridos");
        }
    }
    private void verificarDatosEliminarPersona(String idPersona) throws MiException {
        if (idPersona == null || idPersona.trim().isEmpty()) {
            throw new MiException("El ID de la persona no puede ser nulo o vacío");
        }
    }
    private void verificarSiPersonaTieneContacto(String idPersona) throws MiException {
        List<Contacto> contactos = contactoRepositorio.findByPersonaId(idPersona);
        if (!contactos.isEmpty()) {
            throw new MiException("No se puede eliminar una persona vinculada a un contacto");
        }
    }
}
