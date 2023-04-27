package com.besysoft.agendaspring.apicontrolador;



import com.besysoft.agendaspring.entidades.Empresa;
import com.besysoft.agendaspring.exepciones.MiException;
import com.besysoft.agendaspring.servicios.ContactoServicio;
import com.besysoft.agendaspring.servicios.EmpresaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


@RestController
@RequestMapping("/api/v1/empresa")
public class EmpresaApiControlador {

    @Autowired
    private EmpresaServicio empresaServicio;

    @Autowired
    private ContactoServicio contactoServicio;

    // Método para listar todas las empresas
    @GetMapping("/lista")
    public List<Empresa> lista() {
        return empresaServicio.listarEmpresas();
    }

    // Método para registrar una nueva empresa
    @PostMapping("/registrar")
    @ResponseBody
    public ResponseEntity<?> registrar(@RequestBody Empresa empresa, @RequestParam(required = false) String idContacto) {
        try {
            Empresa nuevaEmpresa = empresaServicio.crearEmpresa(empresa.getNombre(), empresa.getDireccion(), empresa.getCiudad(), empresa.getTelefono(), empresa.getEmail(),idContacto);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaEmpresa);
        } catch (MiException ex) {
            Logger.getLogger(EmpresaApiControlador.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al crear la empresa: " + ex.getMessage());
        }
    }
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        try {
            empresaServicio.eliminarEmpresa(id);
        } catch (MiException ex) {
            Logger.getLogger(EmpresaApiControlador.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/agregar-contacto")
    public ResponseEntity<Void> addContactoToEmpresa(@RequestBody Map<String, String> ids) throws MiException {
        String empresaId = ids.get("idEmpresa");
        String contactoId = ids.get("idContacto");
        empresaServicio.agregarContacto(empresaId, contactoId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @DeleteMapping("/eliminar-Contacto-En-Empresa")
    public ResponseEntity<Void> deleteContactoToEmpresa(@RequestBody Map<String, String> ids) throws MiException {
        String empresaId = ids.get("idEmpresa");
        String contactoId = ids.get("idContacto");
        empresaServicio.eliminarContactoDeEmpresa(empresaId, contactoId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
