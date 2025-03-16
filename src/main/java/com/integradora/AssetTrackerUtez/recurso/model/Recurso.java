package com.integradora.AssetTrackerUtez.recurso.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.integradora.AssetTrackerUtez.categoriaRecurso.model.CategoriaRecurso;
import com.integradora.AssetTrackerUtez.inventarioLevantado.model.InventarioLevantado;
import com.integradora.AssetTrackerUtez.responsable.model.Responsable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "recursos")
public class Recurso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@Column(name = "codigo", columnDefinition = "VARCHAR(100)")
    private String codigo;

    //@NotBlank(message = "La descripción es obligatoria")
    @Column(name = "descripcion", columnDefinition = "VARCHAR(255)", nullable = true)
    private String descripcion;

    //@NotBlank(message = "La marca es obligatoria")
    @Column(name = "marca", columnDefinition = "VARCHAR(100)",nullable = true)
    private String marca;

    //@NotBlank(message = "El modelo es obligatorio")
    @Column(name = "modelo", columnDefinition = "VARCHAR(100)",nullable = true)
    private String modelo;

    //@NotBlank(message = "El número de serie es obligatorio")
    @Column(name = "numeroSerie", columnDefinition = "VARCHAR(100)",nullable = true)
    private String numeroSerie;

    //@NotBlank(message = "Las observaciones son obligatorias")
    @Column(name = "observaciones", columnDefinition = "VARCHAR(255)",nullable = true)
    private String observaciones;

    @Column(name = "status", columnDefinition = "BOOL DEFAULT TRUE")
    private boolean status = true;

    @Column(name = "urlImagen", columnDefinition = "VARCHAR(255)")
    private String urlImagen;
    @Column(name = "publicid", columnDefinition = "VARCHAR(255)")
    private String publicId;

    //Este campo no tiene ni getter ni setter
    @Column(name = "create_at",columnDefinition = "TIMESTAMP DEFAULT NOW()")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;

    @ManyToOne
    @JsonIgnore
    private InventarioLevantado inventarioLevantado;

    @ManyToOne
    @JsonManagedReference
    private CategoriaRecurso categoriaRecurso;

    @ManyToOne
    @JsonManagedReference
    private Responsable responsable;

    public Recurso() {
    }



    public Recurso(String codigo, String descripcion, String marca, String modelo, String numeroSerie, String observaciones, boolean status, InventarioLevantado inventarioLevantado, CategoriaRecurso categoriaRecurso, Responsable responsable, String urlImagen, String publicId) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.marca = marca;
        this.modelo = modelo;
        this.numeroSerie = numeroSerie;
        this.observaciones = observaciones;
        this.status = status;
        this.inventarioLevantado = inventarioLevantado;
        this.categoriaRecurso = categoriaRecurso;
        this.responsable = responsable;
        this.publicId = publicId;
        this.urlImagen = urlImagen;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getNumeroSerie() {
        return numeroSerie;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }


}
