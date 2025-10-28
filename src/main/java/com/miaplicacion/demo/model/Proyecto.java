package com.miaplicacion.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "proyectos")
public class Proyecto {

    @Id
    private String id;

    private String nombre;
    private String descripcion;
    private Long empleadoId;

    // Constructors
    public Proyecto() {}

    public Proyecto(String nombre, String descripcion, Long empleadoId) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.empleadoId = empleadoId;
    }

    // Getters and Setters
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getEmpleadoId() {
        return empleadoId;
    }

    public void setEmpleadoId(Long empleadoId) {
        this.empleadoId = empleadoId;
    }
}