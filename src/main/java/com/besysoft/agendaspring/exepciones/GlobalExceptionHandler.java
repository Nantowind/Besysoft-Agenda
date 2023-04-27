package com.besysoft.agendaspring.exepciones;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(MiException.class)
    public ResponseEntity<Map<String, String>> handleMiExceptions(MiException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Ha ocurrido un error: " + ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }



    @ExceptionHandler(JsonMappingException.class)
    protected ResponseEntity<Object> handleJsonMappingException(JsonMappingException ex) {
        String mensaje = "Todos los campos son necesarios";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mensaje);
    }
    public class EmpresaNotFoundException extends MiException {
        public EmpresaNotFoundException(String msg) {
            super(msg);
        }
    }

    public class ContactoNotFoundException extends MiException {
        public ContactoNotFoundException(String msg) {
            super(msg);
        }
    }

    @ExceptionHandler(EmpresaNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleEmpresaNotFoundException(EmpresaNotFoundException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ContactoNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleContactoNotFoundException(ContactoNotFoundException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);

    }


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }



}
