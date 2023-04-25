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
        Persona persona = personaRepositorio.findById(idPersona).orElseThrow(() -> new RuntimeException("Crear Contacto: Persona no encontrada"));

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
    public Contacto modificarContacto(String idContacto, String idPersona, String idEmpresa) throws MiException {
        verificarDatosModificarontacto(idContacto, idPersona);
        Persona persona = personaRepositorio.findById(idPersona).orElseThrow(() -> new RuntimeException("modificarContacto: Persona no encontrada"));
        Contacto contacto = contactoRepositorio.findById(idContacto).orElseThrow(() -> new RuntimeException("modificarContacto: Contacto no encontrado"));

        if (idEmpresa != null && !idEmpresa.isEmpty()) { // Cambia la condición aquí
            Empresa empresa = empresaRepositorio.findById(idEmpresa).orElseThrow(() -> new RuntimeException("modificarContacto: Empresa no encontrada"));
            contacto.setEmpresa(empresa);
        }

        contacto.setPersona(persona);

        contactoRepositorio.save(contacto);
        return contacto;
    }



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










    public List<Contacto> listarContactos(){
        List <Contacto> contactos = new ArrayList<>();
        contactos = contactoRepositorio.findAll();

        return contactos;
    }
    public Contacto getOne(String id){
        return contactoRepositorio.getOne(id);
    }

    //Verificacion de que ningun dato este vacio.
    private void verificarDatosCrearContacto(String idPersona) throws MiException {
        if (idPersona == null || idPersona.isEmpty()) {
            throw new MiException("crearContacto: El campo persona no puede estar vacio");
        }
    }
    private void verificarDatosModificarontacto(String idContacto,String idPersona) throws MiException {
        if (idContacto == null || idContacto.isEmpty() || idPersona == null || idPersona.isEmpty()) {
            throw new MiException("modificarContacto: Todos los campos son requeridos");
        }
    }
}
