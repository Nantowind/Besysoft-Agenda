package com.besysoft.agendaspring.repositorios;

import com.besysoft.agendaspring.entidades.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonaRepositorio extends JpaRepository<Persona,String> {
    @Query("SELECT p FROM Persona p WHERE p.id = :id")
    Persona buscarPorId(@Param("id") String id);


    @Query("SELECT p FROM Persona p WHERE LOWER(p.nombre) = LOWER(:nombre)")
    List<Persona> buscarPorNombre(@Param("nombre") String nombre);

    @Query("SELECT p FROM Persona p WHERE p.ciudad = LOWER(:ciudad)")
    List<Persona> buscarPorCiudad(@Param("ciudad") String ciudad);

    List<Persona> findByCiudadIgnoreCase(String ciudad);
    Optional<Persona> findById(String id);

    List<Persona> findByNombreContainingIgnoreCaseAndCiudadInIgnoreCase(String nombre, List<String> ciudades);
}
