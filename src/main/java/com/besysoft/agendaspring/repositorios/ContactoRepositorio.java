package com.besysoft.agendaspring.repositorios;

import com.besysoft.agendaspring.entidades.Contacto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactoRepositorio extends JpaRepository<Contacto, String> {
    @Query("SELECT c FROM Contacto c WHERE c.id = :id")
    Contacto buscarPorId(@Param("id") String id);
    List<Contacto> findByPersonaId(String personaId);
}
