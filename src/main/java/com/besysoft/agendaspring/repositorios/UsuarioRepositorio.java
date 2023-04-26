package com.besysoft.agendaspring.repositorios;

import com.besysoft.agendaspring.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepositorio  extends JpaRepository<Usuario,String> {
    @Query("SELECT u FROM Usuario u where u.email = :email")
    public Usuario buscarPorEmail(@Param("email") String Email);
    Optional<Usuario> findByEmail(String email);
}
