package com.besysoft.agendaspring.controladores;

import com.besysoft.agendaspring.entidades.Contacto;
import com.besysoft.agendaspring.entidades.Empresa;
import com.besysoft.agendaspring.exepciones.MiException;
import com.besysoft.agendaspring.servicios.ContactoServicio;
import com.besysoft.agendaspring.servicios.EmpresaServicio;
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
@RequestMapping("/api/empresa")
public class EmpresaControlador {

    @Autowired
    private EmpresaServicio empresaServicio;

    @Autowired
    private ContactoServicio contactoServicio;

    @GetMapping("/registrar")
    private String registrar(ModelMap modelo){
        cargarModel(modelo);
        return "empresa";
    }
    @GetMapping("/lista")
    public String lista(ModelMap modelo){

        return "TablaEmpresas";
    }

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
            return "empresa";
        } catch (MiException ex) {
            Logger.getLogger(EmpresaControlador.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            return "empresa";
        }
    }

    private void cargarModel(ModelMap modelo){
        List<Contacto> contactos = contactoServicio.listarContactos();
        modelo.addAttribute("contactos",contactos);
    }

}
