package com.besysoft.agendaspring.entidades;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;
@Entity
@Table(name = "empresa")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")

public class Empresa {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name ="uuid",strategy = "uuid2")
    private String id;
    private String nombre;
    private String direccion;
    private String ciudad;
    private String telefono;
    private String email;

    @OneToMany(mappedBy = "empresa")
    private List<Contacto> contactos;

    //constructor
    public Empresa() {
    }
    //gettters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String apellido) {
        this.direccion = apellido;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Contacto> getContactos() {
        return contactos;
    }

    public void setContactos(List<Contacto> contactos) {
        this.contactos = contactos;
    }
}
