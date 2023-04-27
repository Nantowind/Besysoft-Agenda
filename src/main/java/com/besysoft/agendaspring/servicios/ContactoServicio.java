package com.besysoft.agendaspring.servicios;

import com.besysoft.agendaspring.entidades.Contacto;

import com.besysoft.agendaspring.entidades.Empresa;
import com.besysoft.agendaspring.entidades.Persona;
import com.besysoft.agendaspring.exepciones.MiException;

import com.besysoft.agendaspring.repositorios.ContactoRepositorio;
import com.besysoft.agendaspring.repositorios.EmpresaRepositorio;
import com.besysoft.agendaspring.repositorios.PersonaRepositorio;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    /**
     * Crea un nuevo contacto asociado a una persona y opcionalmente a una empresa.
     *
     * @param idPersona El ID de la persona asociada al contacto.
     * @param idEmpresa El ID de la empresa asociada al contacto (puede ser null o vacío).
     * @return El contacto creado.
     * @throws MiException Si la persona no se encuentra o si el ID de la persona está vacío.
     */
    @Transactional
    public Contacto crearContacto(String idPersona, String idEmpresa) throws MiException {
        verificarDatosCrearContacto(idPersona);
        Persona persona = personaRepositorio.findById(idPersona).orElseThrow(() -> new RuntimeException("Crear Contacto: Persona no encontrada"));

        Contacto contacto = new Contacto();
        contacto.setPersona(persona);
        asignarEmpresaAContacto(idEmpresa, contacto);
        contactoRepositorio.save(contacto);

        return contacto;
    }

    /**
     * Modifica un contacto existente asociándolo a una nueva persona y opcionalmente a una nueva empresa.
     *
     * @param idContacto El ID del contacto a modificar.
     * @param idPersona  El ID de la nueva persona asociada al contacto.
     * @param idEmpresa  El ID de la nueva empresa asociada al contacto (puede ser null o vacío).
     * @return El contacto modificado.
     * @throws MiException Si alguno de los IDs está vacío o si no se encuentran la persona o el contacto.
     */
    @Transactional
    public Contacto modificarContacto(String idContacto, String idPersona, String idEmpresa) throws MiException {
        verificarDatosModificarontacto(idContacto, idPersona);
        Persona persona = personaRepositorio.findById(idPersona).orElseThrow(() -> new RuntimeException("modificarContacto: Persona no encontrada"));
        Contacto contacto = contactoRepositorio.findById(idContacto).orElseThrow(() -> new RuntimeException("modificarContacto: Contacto no encontrado"));

        contacto.setPersona(persona);
        asignarEmpresaAContacto(idEmpresa, contacto);
        contactoRepositorio.save(contacto);

        return contacto;
    }

    /**
     * Elimina un contacto si no está asociado a ninguna empresa.
     *
     * @param idContacto El ID del contacto a eliminar.
     * @throws MiException Si el contacto no se encuentra o si está asociado a una o más empresas.
     */
    @Transactional
    public void eliminarContacto(String idContacto) throws MiException {
        Optional<Contacto> contactoOptional = contactoRepositorio.findById(idContacto);

        if (!contactoOptional.isPresent()) {
            throw new MiException("Contacto no encontrado con el ID proporcionado");
        }

        Contacto contacto = contactoOptional.get();
        List<Empresa> empresas = empresaRepositorio.findByContactos_Id(idContacto);

        if (!empresas.isEmpty()) {
            throw new MiException("No se puede eliminar el contacto porque está asociado a una o más empresas.");
        }

        contactoRepositorio.delete(contacto);
    }
    /**
     * Lista todos los contactos.
     *
     * @return Una lista de todos los contactos.
     */
    public List<Contacto> listarContactos() {
        return contactoRepositorio.findAll();
    }

    /**
     * Obtiene un contacto por su ID.
     *
     * @param id El ID del contacto a buscar.
     * @return El contacto encontrado o null si no se encuentra.
     */
    public Contacto getOne(String id) {
        return contactoRepositorio.getOne(id);
    }

    // Métodos privados para verificar datos y asignar empresa a un contacto

    private void verificarDatosCrearContacto(String idPersona) throws MiException {
        if (idPersona == null || idPersona.isEmpty()) {
            throw new MiException("crearContacto: El campo persona no puede estar vacio");
        }
    }

    private void verificarDatosModificarontacto(String idContacto, String idPersona) throws MiException {
        if (idContacto == null || idContacto.isEmpty() || idPersona == null || idPersona.isEmpty()) {
            throw new MiException("modificarContacto: Todos los campos son requeridos");
        }
    }

    private void asignarEmpresaAContacto(String idEmpresa, Contacto contacto) {
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
    }
}
