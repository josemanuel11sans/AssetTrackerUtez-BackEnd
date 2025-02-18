package com.integradora.AssetTrackerUtez.categoriaEspacio.model;

import com.integradora.AssetTrackerUtez.espacio.model.Espacio;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "categoriasEspacio")
public class CategoriaEspacio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(name = "nombre", columnDefinition = "VARCHAR(100)")
    private String nombre;

    @NotBlank(message = "La descripción es obligatoria")
    @Column(name = "descripcion", columnDefinition = "VARCHAR(255)")
    private String descripcion;

    @Column(name = "status", columnDefinition = "BOOL DEFAULT TRUE")
    private boolean status = true;

    //Este campo no tiene ni getter ni setter
    @Column(name = "create_at",columnDefinition = "TIMESTAMP DEFAULT NOW()")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;

    // FALTA ÚLTIMA ACTUALIZACIÓN

    @OneToMany (mappedBy = "categoriaEspacio")
    private List<Espacio> espacios;

    public CategoriaEspacio() {
    }

    public CategoriaEspacio(String nombre, String descripcion, boolean status) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.status = status;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
