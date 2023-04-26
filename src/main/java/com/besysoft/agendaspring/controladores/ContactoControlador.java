package com.besysoft.agendaspring.controladores;
import com.besysoft.agendaspring.entidades.*;
import com.besysoft.agendaspring.exepciones.MiException;
import com.besysoft.agendaspring.servicios.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@PreAuthorize("hasRole('USER')")
@Controller
@RequestMapping("/api/contacto")
public class ContactoControlador {
    @Autowired
    private ContactoServicio contactoServicio;
    @Autowired
    private PersonaServicio personaServicio;
    @Autowired
    private EmpresaServicio empresaServicio;

    // Método para cargar el modelo con los datos necesarios
    private void cargarModel(ModelMap modelo){
        try {
            List<Persona> personas = personaServicio.listarPersonas();
            List<Empresa> empresas = empresaServicio.listarEmpresas();
            List<Contacto> contactos = contactoServicio.listarContactos();
            modelo.addAttribute("personas",personas);
            modelo.addAttribute("contactos",contactos);
            modelo.addAttribute("empresas",empresas);
        } catch (MiException ex) {
            Logger.getLogger(EmpresaControlador.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    // Método para registrar un nuevo contacto
    @GetMapping("/registrar")
    public String registrar(ModelMap modelo){
        cargarModel(modelo);
        return "contacto";
    }

    // Método para listar todos los contactos
    @GetMapping("/lista")
    public String lista(ModelMap modelo){
        cargarModel(modelo);
        return "TablaContactos";
    }

    // Método para modificar un contacto existente
    @GetMapping("/modificar/{id}")
    public String modificarContacto(@PathVariable String id, ModelMap modelo) {
        try {
            Contacto contacto = contactoServicio.getOne(id);
            List<Empresa> empresas = empresaServicio.listarEmpresas();
            List<Persona> personas = personaServicio.listarPersonas();

            modelo.addAttribute("contacto", contacto);
            modelo.addAttribute("empresas", empresas);
            modelo.addAttribute("personas", personas);
        } catch (MiException ex) {
            Logger.getLogger(EmpresaControlador.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }

        return "ModificarContacto";
    }

    // Método para actualizar un contacto con los nuevos datos
    @PostMapping("/modificar/{id}")
    public String modificarContactoPost(ModelMap modelo,
                                        @PathVariable String id,
                                        @RequestParam String idPersona,
                                        @RequestParam(required = false) String idEmpresa) {
        try {
            contactoServicio.modificarContacto(id, idPersona, idEmpresa);
            return "redirect:/api/contacto/lista";
        } catch (MiException ex) {
            Logger.getLogger(ContactoControlador.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            return "redirect:/api/contacto/lista";
        }
    }

    // Método para registrar un nuevo contacto
    @PostMapping("/registro")
    public String registro(ModelMap modelo, @RequestParam String idPersona,RedirectAttributes redirectAttrs,
                           @RequestParam(required = false) String idEmpresa) {
        cargarModel(modelo);

        try {
            contactoServicio.crearContacto(idPersona, idEmpresa);
            return "redirect:/api/contacto/lista";
        } catch (MiException ex) {
            redirectAttrs.addFlashAttribute("error", ex.getMessage());
            Logger.getLogger(EmpresaControlador.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            return "redirect:/api/contacto/registrar";
        }
    }


    // Método para eliminar un contacto
    @GetMapping("/eliminar/{id}")
    public String eliminarContacto(@PathVariable String id, ModelMap modelo, RedirectAttributes redirectAttrs) {
        try {
            contactoServicio.eliminarContacto(id);
            redirectAttrs.addFlashAttribute("error", "Contacto eliminado");
            return "redirect:/api/contacto/lista";
        } catch (MiException ex) {
            redirectAttrs.addFlashAttribute("error", ex.getMessage());
            Logger.getLogger(ContactoControlador.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return "redirect:/api/contacto/lista";
    }



}
