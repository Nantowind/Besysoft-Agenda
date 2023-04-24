package com.besysoft.agendaspring.repositorios;
import com.besysoft.agendaspring.entidades.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmpresaRepositorio extends JpaRepository<Empresa, String> {
    @Query("SELECT e FROM Empresa e WHERE e.id = :id")
    Empresa buscarPorId(@Param("id") String id);

    @Query("SELECT e FROM Empresa e WHERE e.nombre = :nombre")
    List<Empresa> buscarPorNombre(@Param("nombre") String nombre);

    @Query("SELECT e FROM Empresa e WHERE e.ciudad = :ciudad")
    List<Empresa> buscarPorCiudad(@Param("ciudad") String ciudad);
}
