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

@Controller
@RequestMapping("/api/contacto")
public class ContactoControlador {
    @Autowired
    private ContactoServicio contactoServicio;
    @Autowired
    private PersonaServicio personaServicio;
    @Autowired
    private EmpresaServicio empresaServicio;

    @GetMapping("/registrar")
    public String registrar(ModelMap modelo){
        cargarModel(modelo);
        return "contacto";
    }
    @GetMapping("/lista")
    public String lista(ModelMap modelo){
        cargarModel(modelo);
        return "TablaContactos";
    }
    @GetMapping("/modificar/{id}")
    public String modificarContacto(@PathVariable String id, ModelMap modelo) {
        Contacto contacto = contactoServicio.getOne(id);
        List<Empresa> empresas = empresaServicio.listarEmpresas();
        List<Persona> personas = personaServicio.listarPersonas();

        modelo.addAttribute("contacto", contacto);
        modelo.addAttribute("empresas", empresas);
        modelo.addAttribute("personas", personas);

        return "ModificarContacto";
    }

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


    private void cargarModel(ModelMap modelo){
        List<Persona> personas = personaServicio.listarPersonas();
        List<Empresa> empresas = empresaServicio.listarEmpresas();
        List<Contacto> contactos = contactoServicio.listarContactos();
        modelo.addAttribute("personas",personas);
        modelo.addAttribute("contactos",contactos);
        modelo.addAttribute("empresas",empresas);
    }
}
