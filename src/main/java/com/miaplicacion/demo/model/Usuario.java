package com.miaplicacion.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "login")
public class Usuario {

    @Id
    private String id;

    private String usuario;
    private String contra;

    // Constructors
    public Usuario() {}

    public Usuario(String usuario, String contra) {
        this.usuario = usuario;
        this.contra = contra;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContra() {
        return contra;
    }

    public void setContra(String contra) {
        this.contra = contra;
    }
}