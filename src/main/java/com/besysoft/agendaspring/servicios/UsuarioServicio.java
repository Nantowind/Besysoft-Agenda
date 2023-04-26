package com.besysoft.agendaspring.servicios;

import com.besysoft.agendaspring.entidades.Usuario;
import com.besysoft.agendaspring.enumeraciones.Rol;
import com.besysoft.agendaspring.exepciones.MiException;
import com.besysoft.agendaspring.repositorios.UsuarioRepositorio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;


    public Usuario buscarPorEmail(String email) {
        return usuarioRepositorio.findByEmail(email).orElse(null);
    }

    @Transactional
    public void registrar( String nombre, String email, String password, String password2) throws MiException {

        validar(nombre, email, password, password2);
        validarEmailUnico(email, "");
        Usuario usuario = new Usuario();

        usuario.setNombre(nombre);
        usuario.setEmail(email);

        usuario.setPassword(new BCryptPasswordEncoder().encode(password));

        usuario.setRol(Rol.USER);


        usuarioRepositorio.save(usuario);
    }

    @Transactional
    public void actualizar(MultipartFile archivo, String idUsuario, String nombre, String email, String password, String password2) throws MiException {

        validar(nombre, email, password, password2);

        Optional<Usuario> respuesta = usuarioRepositorio.findById(idUsuario);
        if (respuesta.isPresent()) {

            Usuario usuario = respuesta.get();
            validarEmailUnico(email, idUsuario);
            usuario.setNombre(nombre);
            usuario.setEmail(email);

            usuario.setPassword(new BCryptPasswordEncoder().encode(password));

            usuario.setRol(Rol.USER);

            String idImagen = null;



            usuarioRepositorio.save(usuario);
        }

    }

    public Usuario getOne(String id){
        return usuarioRepositorio.getOne(id);
    }

    @Transactional(readOnly=true)
    public List<Usuario> listarUsuarios() {

        List<Usuario> usuarios = new ArrayList();

        usuarios = usuarioRepositorio.findAll();

        return usuarios;
    }

    @Transactional
    public void cambiarRol(String id){
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);

        if(respuesta.isPresent()) {

            Usuario usuario = respuesta.get();

            if(usuario.getRol().equals(Rol.USER)) {

                usuario.setRol(Rol.ADMIN);

            }else if(usuario.getRol().equals(Rol.ADMIN)) {
                usuario.setRol(Rol.USER);
            }
        }
    }

    private void validar(String nombre, String email, String password, String password2) throws MiException {

        if (nombre.isEmpty() || nombre == null) {
            throw new MiException("el nombre no puede ser nulo o estar vacío");
        }
        if (email.isEmpty() || email == null) {
            throw new MiException("el email no puede ser nulo o estar vacio");
        }
        if (password.isEmpty() || password == null || password.length() <= 5) {
            throw new MiException("La contraseña no puede estar vacía, y debe tener más de 5 dígitos");
        }

        if (!password.equals(password2)) {
            throw new MiException("Las contraseñas ingresadas deben ser iguales");
        }

    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepositorio.buscarPorEmail(email);

        if (usuario != null) {

            List<GrantedAuthority> permisos = new ArrayList();

            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toString());

            permisos.add(p);

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

            HttpSession session = attr.getRequest().getSession(true);

            session.setAttribute("usuariosession", usuario);

            return new User(usuario.getEmail(), usuario.getPassword(), permisos);
        } else {
            return null;
        }

    }
    private void validarEmailUnico(String email, String idUsuario) throws MiException {
        Optional<Usuario> usuarioExistente = usuarioRepositorio.findByEmail(email);
        if (usuarioExistente.isPresent() && !usuarioExistente.get().getId().equals(idUsuario)) {
            throw new MiException("Ya existe un usuario con el correo electrónico proporcionado.");
        }
    }
}
