package com.integradora.AssetTrackerUtez.categoriaEspacio.model;

import com.integradora.AssetTrackerUtez.espacio.model.Espacio;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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

    @Column(name = "estado", columnDefinition = "BOOL DEFAULT TRUE")
    private boolean estado = true;

    //Este campo no tiene ni getter ni setter
    @CreationTimestamp
    @Column(name = "fechaCreacion",columnDefinition = "TIMESTAMP DEFAULT NOW()")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;

    @UpdateTimestamp
    @Column(name = "ultimaActualizacion",columnDefinition = "TIMESTAMP DEFAULT NOW()")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ultimaActualizacion;

    @OneToMany (mappedBy = "categoriaEspacio")
    private List<Espacio> espacios;

    public CategoriaEspacio() {
    }

    public CategoriaEspacio(String nombre, String descripcion, boolean estado, Date fechaCreacion, Date ultimaActualizacion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.ultimaActualizacion = ultimaActualizacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public Date getUltimaActualizacion() {
        return ultimaActualizacion;
    }

    public void setUltimaActualizacion(Date ultimaActualizacion) {
        this.ultimaActualizacion = ultimaActualizacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
