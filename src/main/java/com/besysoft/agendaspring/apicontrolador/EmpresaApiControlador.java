package com.besysoft.agendaspring.apicontrolador;

import com.besysoft.agendaspring.entidades.Empresa;
import com.besysoft.agendaspring.exepciones.ContactoNotFoundException;
import com.besysoft.agendaspring.exepciones.EmpresaNotFoundException;
import com.besysoft.agendaspring.exepciones.MiException;
import com.besysoft.agendaspring.servicios.ContactoServicio;
import com.besysoft.agendaspring.servicios.EmpresaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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
    public ResponseEntity<Object> lista() {
        List<Empresa> empresas = empresaServicio.listarEmpresas();

        if (empresas.isEmpty()) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "No hay empresas creadas.");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(empresas, HttpStatus.OK);
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
    public ResponseEntity<Map<String, String>> eliminar(@PathVariable String id) {
        Map<String, String> response = new HashMap<>();
        try {
            empresaServicio.eliminarEmpresa(id);
            response.put("mensaje", "Empresa eliminada exitosamente");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (MiException ex) {
            Logger.getLogger(EmpresaApiControlador.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            response.put("error", "No se pudo eliminar la empresa: " + ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/agregar-contacto")
    public ResponseEntity<Map<String, String>> addContactoToEmpresa(@RequestBody Map<String, String> ids) throws MiException {
        String empresaId = ids.get("idEmpresa");
        String contactoId = ids.get("idContacto");
        empresaServicio.agregarContacto(empresaId, contactoId);

        Map<String, String> successResponse = new HashMap<>();
        successResponse.put("mensaje", "Contacto agregado exitosamente a la empresa");

        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }





    @DeleteMapping("/eliminar-Contacto-En-Empresa")
    public ResponseEntity<Map<String, String>> deleteContactoToEmpresa(@RequestBody Map<String, String> ids) {
        Map<String, String> response = new HashMap<>();
        try {
            String empresaId = ids.get("idEmpresa");
            String contactoId = ids.get("idContacto");
            empresaServicio.eliminarContactoDeEmpresa(empresaId, contactoId);
            response.put("mensaje", "Contacto eliminado de la empresa exitosamente");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (MiException ex) {
            Logger.getLogger(EmpresaApiControlador.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            response.put("error", "No se pudo eliminar el contacto de la empresa: " + ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }




}
