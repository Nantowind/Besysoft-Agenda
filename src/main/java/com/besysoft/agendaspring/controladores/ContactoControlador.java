package com.besysoft.agendaspring.controladores;

import com.besysoft.agendaspring.entidades.Contacto;
import com.besysoft.agendaspring.entidades.Empresa;
import com.besysoft.agendaspring.entidades.Persona;
import com.besysoft.agendaspring.exepciones.MiException;
import com.besysoft.agendaspring.servicios.ContactoServicio;
import com.besysoft.agendaspring.servicios.EmpresaServicio;
import com.besysoft.agendaspring.servicios.PersonaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @PostMapping("/registro")
    public String registro(ModelMap modelo,@RequestParam String idPersona,@RequestParam(required = false) String idEmpresa){
        cargarModel(modelo);

        try {
            contactoServicio.crearContacto(idPersona,idEmpresa);
            return "contacto";
        }catch (MiException ex){
            Logger.getLogger(PersonaControlador.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            return "contacto";
        }

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
