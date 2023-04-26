package com.besysoft.agendaspring.controladores;

import com.besysoft.agendaspring.entidades.Usuario;
import com.besysoft.agendaspring.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UsuarioServicio usuarioServicio;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Usuario usuarioAutenticado = usuarioServicio.buscarPorEmail(userDetails.getUsername());

        Map<String, Object> usuarioData = new HashMap<>();
        usuarioData.put("id", usuarioAutenticado.getId());
        usuarioData.put("nombre", usuarioAutenticado.getNombre());
        usuarioData.put("email", usuarioAutenticado.getEmail());
        usuarioData.put("rol", usuarioAutenticado.getRol());

        return ResponseEntity.ok().body(usuarioData);
    }

}
