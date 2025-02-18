package com.integradora.AssetTrackerUtez.responsable.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.integradora.AssetTrackerUtez.recurso.model.Recurso;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

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

    @Column(name = "status", columnDefinition = "BOOL DEFAULT TRUE")
    private boolean status = true;

    //Este campo no tiene ni getter ni setter
    @Column(name = "create_at",columnDefinition = "TIMESTAMP DEFAULT NOW()")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;

    // FALTA ÚLTIMA ACTUALIZACIÓN

    @OneToMany(mappedBy = "responsable")
    private List<Recurso> recursos;

    public Responsable() {
    }

    public Responsable(String nombre, String divisionAcademica, boolean status) {
        this.nombre = nombre;
        this.divisionAcademica = divisionAcademica;
        this.status = status;
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

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
