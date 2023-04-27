package com.besysoft.agendaspring.entidades;

public class ContactoDTO {
    private String idPersona;
    private String idEmpresa;

    // Getters y setters

    public ContactoDTO() {
    }

    public ContactoDTO(String idPersona) {
        this.idPersona = idPersona;
    }

    public ContactoDTO(String idPersona, String idEmpresa) {
        this.idPersona = idPersona;
        this.idEmpresa = idEmpresa;
    }

    public String getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(String idPersona) {
        this.idPersona = idPersona;
    }

    public String getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(String idEmpresa) {
        this.idEmpresa = idEmpresa;
    }
}
