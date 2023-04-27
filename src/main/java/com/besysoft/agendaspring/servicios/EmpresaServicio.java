package com.besysoft.agendaspring.servicios;

import com.besysoft.agendaspring.entidades.Contacto;
import com.besysoft.agendaspring.entidades.Empresa;


import com.besysoft.agendaspring.exepciones.MiException;
import com.besysoft.agendaspring.repositorios.ContactoRepositorio;
import com.besysoft.agendaspring.repositorios.EmpresaRepositorio;
import com.besysoft.agendaspring.exepciones.EmpresaNotFoundException;
import com.besysoft.agendaspring.exepciones.ContactoNotFoundException;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class EmpresaServicio {

    @Autowired
    private EmpresaRepositorio empresaRepositorio;
    @Autowired
    private ContactoRepositorio contactoRepositorio;

    /**
     * Crea una nueva empresa con los datos proporcionados.
     * Si se proporciona un ID de contacto, lo asocia a la empresa.
     *
     * @param nombre     El nombre de la empresa.
     * @param direccion  La dirección de la empresa.
     * @param ciudad     La ciudad en la que se encuentra la empresa.
     * @param telefono   El número de teléfono de la empresa.
     * @param email      El correo electrónico de la empresa.
     * @param idContacto El ID del contacto a asociar con la empresa (opcional).
     * @return La empresa creada.
     * @throws MiException Si alguno de los datos proporcionados no es válido.
     */
    @Transactional
    public Empresa crearEmpresa(String nombre, String direccion, String ciudad, String telefono, String email, String idContacto) throws MiException {
        verificarDatosCrearEmpresa(nombre, direccion, ciudad, telefono, email);

        // Validación del número de teléfono
        String regex = "^[0-9]+$";
        if (!Pattern.matches(regex, telefono)) {
            throw new MiException("El número de teléfono solo debe contener dígitos.");
        }

        Empresa empresa = new Empresa();
        empresa.setNombre(nombre);
        empresa.setDireccion(direccion);
        empresa.setCiudad(ciudad);
        empresa.setTelefono(telefono);
        empresa.setEmail(email);

        if (idContacto != null && !idContacto.isEmpty()) {
            Optional<Contacto> contactoOpt = contactoRepositorio.findById(idContacto);
            if (contactoOpt.isPresent()) {
                Contacto contacto = contactoOpt.get();
                contacto.setEmpresa(empresa); // Asegurar que el contacto tenga la referencia a la empresa
                List<Contacto> contactos = new ArrayList<>();
                contactos.add(contacto);
                empresa.setContactos(contactos);
            } else {
                System.out.println("crearEmpresa: Contacto no encontrado, se creará la empresa sin contacto asociado");
            }
        } else {
            System.out.println("crearEmpresa: idContacto es nulo o vacío, se creará la empresa sin contacto asociado");
        }

        return empresaRepositorio.save(empresa);
    }

    /**
     * Modifica los datos de una empresa existente.
     *
     * @param idEmpresa El ID de la empresa a modificar.
     * @param nombre    El nuevo nombre de la empresa.
     * @param direccion La nueva dirección de la empresa.
     * @param ciudad    La nueva ciudad en la que se encuentra la empresa.
     * @param telefono  El nuevo número de teléfono de la empresa.
     * @param email     El nuevo correo electrónico de la empresa.
     * @return La empresa modificada.
     * @throws MiException Si alguno de los datos proporcionados no es válido.
     */
    @Transactional
    public Empresa modificarEmpresa(String idEmpresa, String nombre, String direccion, String ciudad, String telefono, String email) throws MiException {
        verificarDatosModificarEmpresa(idEmpresa, nombre, direccion, ciudad, telefono, email);
        Empresa empresa = empresaRepositorio.findById(idEmpresa).orElseThrow(() -> new RuntimeException("modificarEmpresa: Empresa no encontrada"));

        empresa.setNombre(nombre);
        empresa.setDireccion(direccion);
        empresa.setCiudad(ciudad);
        empresa.setTelefono(telefono);
        empresa.setEmail(email);

        return empresaRepositorio.save(empresa);
    }

    /**
     * Agrega un contacto a una empresa existente.
     *
     * @param idEmpresa  El ID de la empresa a la que se agregará el contacto.
     * @param idContacto El ID del contacto a agregar.
     * @return La empresa con el contacto agregado.
     * @throws MiException Si alguno de los datos proporcionados no es válido.
     */
    @Transactional
    public Empresa agregarContacto(String idEmpresa, String idContacto) throws MiException {
        verificarDatosAgregarEliminarContacto(idEmpresa, idContacto);
        Empresa empresa = empresaRepositorio.findById(idEmpresa).orElseThrow(() -> new EmpresaNotFoundException("agregarContacto: Empresa no encontrada"));
        Contacto contacto = contactoRepositorio.findById(idContacto).orElseThrow(() -> new ContactoNotFoundException("agregarContacto: Contacto no encontrado"));

        empresa.getContactos().add(contacto);
        contacto.setEmpresa(empresa);

        return empresaRepositorio.save(empresa);
    }
    /**
     * Elimina un contacto de una empresa existente.
     *
     * @param idEmpresa  El ID de la empresa de la que se eliminará el contacto.
     * @param idContacto El ID del contacto a eliminar.
     * @return La empresa con el contacto eliminado.
     * @throws MiException Si alguno de los datos proporcionados no es válido.
     */
    @Transactional
    public Empresa eliminarContactoDeEmpresa(String idEmpresa, String idContacto) throws MiException {
        verificarDatosAgregarEliminarContacto(idEmpresa, idContacto);
        Empresa empresa = empresaRepositorio.findById(idEmpresa)
                .orElseThrow(() -> new EmpresaNotFoundException("eliminarContactoDeEmpresa: Empresa no encontrada con el ID: " + idEmpresa));
        Contacto contacto = contactoRepositorio.findById(idContacto)
                .orElseThrow(() -> new ContactoNotFoundException("eliminarContactoDeEmpresa: Contacto no encontrado con el ID: " + idContacto));

        // Verifica si el contacto existe en la lista de contactos de la empresa
        if (empresa.getContactos().remove(contacto)) {
            contacto.setEmpresa(null);
            contactoRepositorio.save(contacto);

            return empresaRepositorio.save(empresa);
        } else {
            throw new ContactoNotFoundException("El contacto no pertenece a la empresa");
        }
    }

    /**
     * Elimina una empresa con el ID proporcionado.
     *
     * @param idEmpresa El ID de la empresa a eliminar.
     * @throws MiException Si la empresa está asociada a uno o más contactos y no puede ser eliminada.
     */
    @Transactional
    public void eliminarEmpresa(String idEmpresa) throws MiException {
        verificarEmpresaAsociadaAContactos(idEmpresa);
        Empresa empresa = empresaRepositorio.findById(idEmpresa)
                .orElseThrow(() -> new EmpresaNotFoundException("eliminarEmpresa: Empresa no encontrada con el ID: " + idEmpresa));
        empresaRepositorio.delete(empresa);
    }
    /**
     * Obtiene una lista de todas las empresas en el repositorio.
     *
     * @return La lista de empresas.
     */
    public List<Empresa> listarEmpresas() {
        List<Empresa> empresas = new ArrayList<>();
        empresas = empresaRepositorio.findAll();

        return empresas;
    }

    /**
     * Obtiene una empresa por su ID.
     *
     * @param id El ID de la empresa a buscar.
     * @return La empresa encontrada.
     */
    public Empresa getOne(String id) {
        return empresaRepositorio.getOne(id);
    }

    // Métodos de verificación de datos

    private void verificarDatosCrearEmpresa(String nombre, String direccion, String ciudad, String telefono, String email) throws MiException {
        if (nombre == null || nombre.isEmpty() || direccion == null || direccion.isEmpty() || ciudad == null || ciudad.isEmpty() ||
                telefono == null || telefono.isEmpty() || email == null || email.isEmpty()) {
            throw new MiException("crearEmpresa: Todos los campos son requeridos");
        }
    }

    private void verificarDatosModificarEmpresa(String idEmpresa, String nombre, String direccion, String ciudad, String telefono, String email) throws MiException {
        if (idEmpresa == null || idEmpresa.isEmpty() || nombre == null || nombre.isEmpty() || direccion == null || direccion.isEmpty() || ciudad == null || ciudad.isEmpty() ||
                telefono == null || telefono.isEmpty() || email == null || email.isEmpty()) {
            throw new MiException("modificarEmpresa: Todos los campos son requeridos");
        }
    }

    private void verificarDatosAgregarEliminarContacto(String idEmpresa, String idContacto) throws MiException {
        if (idEmpresa == null || idEmpresa.isEmpty() || idContacto == null || idContacto.isEmpty()) {
            throw new MiException("agregar/eliminarContacto: Todos los campos son requeridos");
        }
    }
    private void verificarEmpresaAsociadaAContactos(String idEmpresa) throws MiException {
        List<Contacto> contactosAsociados = contactoRepositorio.findByEmpresaId(idEmpresa);
        if (!contactosAsociados.isEmpty()) {
            throw new MiException("La empresa está asociada a uno o más contactos y no puede ser eliminada.");
        }
    }


}
