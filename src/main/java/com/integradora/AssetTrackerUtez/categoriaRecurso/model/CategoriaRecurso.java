package com.integradora.AssetTrackerUtez.categoriaRecurso.model;

import com.integradora.AssetTrackerUtez.recurso.model.Recurso;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "categoriasRecurso")
public class CategoriaRecurso {
    //id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    //nombre
    @NotBlank(message = "El nombre es obligatorio")
    @Column(name = "nombre", columnDefinition = "VARCHAR(100)")
    private String nombre;
    //material
    @NotBlank(message = "El material es obligatorio")
    @Column(name = "material", columnDefinition = "VARCHAR(255)")
    private String material;
    //imagenUrl
    @Column(name = "imagenurl", columnDefinition = "VARCHAR(255)")
    private String imagenUrl;
    //publicId
    @Column(name = "publicid", columnDefinition = "VARCHAR(255)")
    private String publicId;

    @Column(name = "status", columnDefinition = "BOOL DEFAULT TRUE")
    private boolean status = true;

    //Este campo no tiene ni getter ni setter
    @Column(name = "create_at",columnDefinition = "TIMESTAMP DEFAULT NOW()")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;

    // FALTA ÚLTIMA ACTUALIZACIÓN

    @OneToMany(mappedBy = "categoriaRecurso")
    private List<Recurso> recursos;

    public CategoriaRecurso() {
    }

    public CategoriaRecurso(String nombre, String material, String imagenUrl, String publicId, boolean status) {
        this.nombre = nombre;
        this.material = material;
        this.imagenUrl = imagenUrl;
        this.publicId = publicId;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }
}
