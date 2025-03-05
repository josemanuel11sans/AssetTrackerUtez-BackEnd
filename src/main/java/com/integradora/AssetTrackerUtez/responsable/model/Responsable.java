package com.integradora.AssetTrackerUtez.responsable.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.integradora.AssetTrackerUtez.recurso.model.Recurso;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "responsables")
public class Responsable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(name = "nombre", columnDefinition = "VARCHAR(100)")
    private String nombre;

    @NotBlank(message = "La división académica es obligatoria")
    @Column(name = "divisionAcademica", columnDefinition = "VARCHAR(100)")
    private String divisionAcademica;

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

    @OneToMany(mappedBy = "responsable")
    private List<Recurso> recursos;

    public Responsable() {
    }

    public Responsable(String nombre, String divisionAcademica, boolean estado, Date fechaCreacion, Date ultimaActualizacion) {
        this.nombre = nombre;
        this.divisionAcademica = divisionAcademica;
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

    public String getDivisionAcademica() {
        return divisionAcademica;
    }

    public void setDivisionAcademica(String divisionAcademica) {
        this.divisionAcademica = divisionAcademica;
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

}
