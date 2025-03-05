package com.integradora.AssetTrackerUtez.categoriaEspacio.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

public class CategoriaEspacioDto {
    @NotNull(groups = {Modify.class, ChangeStatus.class}, message = "Es necesario el id")
    private Long id;
    @NotBlank(groups = {Register.class, Modify.class}, message = "El nombre no puede quedar vacío")
    private String nombre;
    @NotBlank(groups = {Register.class, Modify.class}, message = "La descripción no puede quedar vacía")
    private String descripcion;
    @NotNull(groups = {Register.class, Modify.class})
    private Date fechaCreacion;
    @NotNull(groups = {Register.class, Modify.class})
    private Date ultimaActualizacion;

    public CategoriaEspacioDto() {
    }

    public CategoriaEspacioDto(Long id, String nombre, String descripcion, Date fechaCreacion, Date ultimaActualizacion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaCreacion = fechaCreacion;
        this.ultimaActualizacion = ultimaActualizacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getUltimaActualizacion() {
        return ultimaActualizacion;
    }

    public void setUltimaActualizacion(Date ultimaActualizacion) {
        this.ultimaActualizacion = ultimaActualizacion;
    }

    //Interfaces validación
    public interface Register {}
    public interface Modify {}
    public interface ChangeStatus {}
}
