package com.besysoft.agendaspring.controladores;

import com.besysoft.agendaspring.entidades.Contacto;
import com.besysoft.agendaspring.entidades.Empresa;
import com.besysoft.agendaspring.exepciones.MiException;
import com.besysoft.agendaspring.servicios.ContactoServicio;
import com.besysoft.agendaspring.servicios.EmpresaServicio;
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
@RequestMapping("/api/empresa")
public class EmpresaControlador {

    @Autowired
    private EmpresaServicio empresaServicio;

    @Autowired
    private ContactoServicio contactoServicio;

    // Método para cargar el modelo con los datos necesarios
    private void cargarModel(ModelMap modelo){
        List<Contacto> contactos = contactoServicio.listarContactos();
        modelo.addAttribute("contactos",contactos);
        List<Empresa> empresas = empresaServicio.listarEmpresas();
        modelo.addAttribute("empresas",empresas);
    }

    // Método para registrar una nueva empresa
    @GetMapping("/registrar")
    private String registrar(ModelMap modelo){
        cargarModel(modelo);
        return "empresa";
    }

    // Método para listar todas las empresas
    @GetMapping("/lista")
    public String lista(ModelMap modelo){
        cargarModel(modelo);
        return "TablaEmpresas";
    }

    // Método para registrar una nueva empresa con los datos proporcionados
    @PostMapping("/registro")
    public String registroEmpresa(ModelMap modelo,
                                  @RequestParam String nombre,
                                  @RequestParam String direccion,
                                  @RequestParam String ciudad,
                                  @RequestParam String telefono,
                                  @RequestParam String email,
                                  @RequestParam(required = false) String idContacto) {
        try {
            cargarModel(modelo);
            empresaServicio.crearEmpresa(nombre, direccion, ciudad, telefono, email, idContacto);
            return "redirect:/api/empresa/lista";
        } catch (MiException ex) {
            Logger.getLogger(EmpresaControlador.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            return "redirect:/api/empresa/registrar";
        }
    }

    // Método para modificar una empresa existente
    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, ModelMap modelo){
        modelo.put("empresa", empresaServicio.getOne(id));
        return "ModificarEmpresas";
    }

    // Método para actualizar una empresa con los nuevos datos
    @PostMapping("/modificar/{id}")
    public String modificar(ModelMap modelo,@PathVariable String id,String nombre,
                            String direccion,String ciudad,String telefono,String email){

        cargarModel(modelo);
        try {
            empresaServicio.modificarEmpresa(id,nombre,direccion,ciudad,telefono,email);
            return "redirect:../lista";
        }catch (MiException ex){
            Logger.getLogger(PersonaControlador.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            return "TablaEmpresas";
        }
    }

    // Método para agregar un contacto a una empresa
    @GetMapping("/agregar-contacto/{idEmpresa}")
    public String agregarContactoEmpresa(@PathVariable String idEmpresa, ModelMap modelo){
        Empresa empresa = empresaServicio.getOne(idEmpresa);
        List<Contacto> contactos = contactoServicio.listarContactos();

        modelo.addAttribute("empresa", empresa);
        modelo.addAttribute("contactos", contactos);

        return "AgregarContactoEmpresa";
    }

    // Método para mostrar los contactos de una empresa para eliminar
    @GetMapping("/eliminar-contacto/{idEmpresa}")
    public String mostrarEliminarContacto(@PathVariable String idEmpresa, ModelMap modelo) {
        Empresa empresa = empresaServicio.getOne(idEmpresa);
        List<Contacto> contactos = empresa.getContactos();

        modelo.addAttribute("empresa", empresa);
        modelo.addAttribute("contactos", contactos);

        return "EliminarContactoEmpresa";
    }
    // Método para eliminar una empresa
    @GetMapping("/eliminar-empresa/{idEmpresa}")
    public String eliminarEmpresa(@PathVariable String idEmpresa, RedirectAttributes redirectAttrs) {
        try {
            empresaServicio.eliminarEmpresa(idEmpresa);
            redirectAttrs.addFlashAttribute("error", "Empresa eliminada");
            return "redirect:/api/empresa/lista";
        } catch (MiException ex) {
            redirectAttrs.addFlashAttribute("error", ex.getMessage());
            Logger.getLogger(EmpresaControlador.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return "redirect:/api/empresa/lista";
    }

    // Método para eliminar un contacto de una empresa
    @PostMapping("/eliminar-contacto/{idEmpresa}")
    public String eliminarContactoEmpresa(ModelMap modelo,
                                          @PathVariable String idEmpresa,
                                          @RequestParam String idContacto) {
        try {
            cargarModel(modelo);
            empresaServicio.eliminarContactoDeEmpresa(idEmpresa, idContacto);
            return "redirect:/api/empresa/lista";
        } catch (MiException ex) {
            Logger.getLogger(EmpresaControlador.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            return "EliminarContactoEmpresa";
        }
    }

    // Método para agregar un contacto a una empresa
    @PostMapping("/agregar-contacto/{id}")
    public String agregarContactoEmpresa(ModelMap modelo,
                                         @PathVariable String id,
                                         @RequestParam String idEmpresa,
                                         @RequestParam String idContacto) {
        try {
            cargarModel(modelo);
            empresaServicio.agregarContacto(idEmpresa, idContacto);
            return "redirect:/api/empresa/lista";
        } catch (MiException ex) {
            Logger.getLogger(EmpresaControlador.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            return "AgregarContactoEmpresa";
        }
    }

}
