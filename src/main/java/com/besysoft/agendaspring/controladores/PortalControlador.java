package com.besysoft.agendaspring.controladores;

import com.besysoft.agendaspring.exepciones.MiException;
import com.besysoft.agendaspring.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
@RequestMapping("/api/")
public class PortalControlador {

    @Autowired
    UsuarioServicio usuarioServicio;

    @GetMapping("/index")
    public String index(){
        return "index";
    }

    @GetMapping("/registrar")
    public String registrar(){
        return "Registro";
    }

    // Cambiar de @GetMapping a @PostMapping
    @PostMapping("/registro")
    public String registro(@RequestParam String nombre,
                           @RequestParam String email,
                           @RequestParam String password,
                           @RequestParam String password2,
                           RedirectAttributes redirectAttrs){

        try {
            usuarioServicio.registrar(nombre,email,password,password2);
            return "index";
        }catch (MiException ex){
            redirectAttrs.addFlashAttribute("error", ex.getMessage());
            Logger.getLogger(EmpresaControlador.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            return "redirect:/api/registrar";
        }

    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error,RedirectAttributes redirectAttrs){

        if (error != null){
            redirectAttrs.addFlashAttribute("error", "Usuario o contraseña incorrecto");
            return "redirect:/api/login";
        }


        return "Login";
    }

}
