package com.besysoft.agendaspring.apicontrolador;

import com.besysoft.agendaspring.controladores.EmpresaControlador;
import com.besysoft.agendaspring.entidades.Usuario;
import com.besysoft.agendaspring.exepciones.MiException;
import com.besysoft.agendaspring.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    public static class UsuarioDTO {
        private String nombre;
        private String email;
        private String password;
        private String password2;

        // Agrega los getters y setters para cada atributo
        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getPassword2() {
            return password2;
        }

        public void setPassword2(String password2) {
            this.password2 = password2;
        }
    }
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

    @PostMapping("/registro")
    public ResponseEntity<Map<String, String>> registro(@RequestBody UsuarioDTO usuario, RedirectAttributes redirectAttrs) {
        Map<String, String> response = new HashMap<>();
        try {
            usuarioServicio.registrar(usuario.getNombre(), usuario.getEmail(), usuario.getPassword(), usuario.getPassword2());
            response.put("mensaje", "Usuario registrado exitosamente");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (MiException ex) {
            redirectAttrs.addFlashAttribute("error", ex.getMessage());
            Logger.getLogger(EmpresaControlador.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            response.put("error", "No se pudo registrar el usuario: " + ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

}
