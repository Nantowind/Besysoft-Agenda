package com.besysoft.agendaspring.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/")
public class PortalControlador {


    @GetMapping("/api/")
    public String index(){
        return "index.html";
    }

}
