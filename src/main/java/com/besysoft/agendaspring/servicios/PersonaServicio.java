package com.besysoft.agendaspring.servicios;


import com.besysoft.agendaspring.entidades.Contacto;
import com.besysoft.agendaspring.entidades.Persona;
import com.besysoft.agendaspring.exepciones.MiException;
import com.besysoft.agendaspring.repositorios.ContactoRepositorio;
import com.besysoft.agendaspring.repositorios.PersonaRepositorio;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonaServicio {

    @Autowired
    private PersonaRepositorio personaRepositorio;
    @Autowired
    private ContactoRepositorio contactoRepositorio;

    @Transactional
    public Persona crearPersona(String nombre, String apellido, String ciudad, String telefono, String mail) throws MiException {
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
    public Persona modificarPersona(String idPersona, String nombre, String apellido, String ciudad, String telefono, String mail) throws RuntimeException {
        verificarDatosModificarPersona(idPersona, nombre, apellido, ciudad, telefono, mail);
        Persona persona = personaRepositorio.findById(idPersona).orElseThrow(() -> new RuntimeException("modificarPersona: Persona no encontrada"));

        persona.setNombre(nombre);
        persona.setApellido(apellido);
        persona.setCiudad(ciudad);
        persona.setTelefono(telefono);
        persona.setEmail(mail);

        personaRepositorio.save(persona);

        return persona;
    }

    /**
     * Elimina una persona por su ID.
     *
     * @param idPersona El ID de la persona a eliminar.
     * @throws MiException Si se encuentra algún problema en la validación de los datos.
     */
    @Transactional
    public void eliminarPersona(String idPersona) throws MiException {
        verificarDatosEliminarPersona(idPersona);
        verificarSiPersonaTieneContacto(idPersona);
        personaRepositorio.deleteById(idPersona);
    }

    /**
     * Obtiene una lista de todas las personas en el repositorio.
     *
     * @return La lista de personas.
     * @throws MiException Si no se encuentran personas en la base de datos.
     */
    public List<Persona> listarPersonas() throws MiException {
        List<Persona> personas = personaRepositorio.findAll();
        if (personas.isEmpty()) {
            throw new MiException("No se encontraron personas en la base de datos.");
        }
        return personas;
    }

    /**
     * Obtiene una persona por su ID.
     *
     * @param id El ID de la persona a buscar.
     * @return La persona encontrada.
     */
    public Persona getOne(String id) {
        return personaRepositorio.getOne(id);
    }

    // Los siguientes métodos están relacionados con la búsqueda de personas en el repositorio

    public List<Persona> buscarPorNombre(String nombre) throws MiException {
        List<Persona> personas = personaRepositorio.buscarPorNombre(nombre);
        if (personas.isEmpty()) {
            throw new MiException("No se encontraron personas con el nombre: " + nombre);
        }
        return personas;
    }

    public List<Persona> buscarPorCiudad(String ciudad) throws MiException {
        List<Persona> personas = personaRepositorio.findByCiudadIgnoreCase(ciudad);
        if (personas.isEmpty()) {
            throw new MiException("No se encontraron personas en la ciudad: " + ciudad);
        }
        return personas;
    }
    public List<Persona> buscarPorNombreYCiudades(String nombre, List<String> ciudades) {
        return personaRepositorio.findByNombreContainingIgnoreCaseAndCiudadInIgnoreCase(nombre, ciudades);
    }

    // Los siguientes métodos están relacionados con la verificación de datos de entrada

    private void verificarDatosCrearPersona(String nombre, String apellido, String ciudad, String telefono, String mail) throws MiException {
        if (nombre == null || nombre.isEmpty() || apellido == null || apellido.isEmpty() || ciudad == null || ciudad.isEmpty() ||
                telefono == null || telefono.isEmpty() || mail == null || mail.isEmpty()) {
            throw new MiException("crearPersona: Todos los campos son requeridos");
        }
    }

    private void verificarDatosModificarPersona(String idPersona, String nombre, String apellido, String ciudad, String telefono, String mail) throws RuntimeException {
        if (idPersona == null || idPersona.isEmpty() || nombre == null || nombre.isEmpty() || apellido == null || apellido.isEmpty() || ciudad == null || ciudad.isEmpty() ||
                telefono == null || telefono.isEmpty() || mail == null || mail.isEmpty()) {
            throw new RuntimeException("modificarPersona: Todos los campos son requeridos");
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
