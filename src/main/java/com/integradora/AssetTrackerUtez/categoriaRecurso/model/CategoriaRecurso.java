package com.integradora.AssetTrackerUtez.categoriaRecurso.model;

import com.integradora.AssetTrackerUtez.recurso.model.Recurso;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "categoriasRecurso")
public class CategoriaRecurso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(name = "nombre", columnDefinition = "VARCHAR(100)")
    private String nombre;

    @NotBlank(message = "El material es obligatorio")
    @Column(name = "material", columnDefinition = "VARCHAR(255)")
    private String material;

    // FALTA IMAGEN

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
}
