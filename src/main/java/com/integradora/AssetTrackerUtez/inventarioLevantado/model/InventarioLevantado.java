package com.integradora.AssetTrackerUtez.inventarioLevantado.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.integradora.AssetTrackerUtez.espacio.model.Espacio;
import com.integradora.AssetTrackerUtez.recurso.model.Recurso;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "inventarioLevantado")
public class InventarioLevantado {
    //id autogeneradoo
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    //status

    @Column(name = "status", columnDefinition = "BOOL DEFAULT TRUE")
    private boolean status = true;
    //esta es la fecha de creacion
    //Este campo no tiene ni getter ni setter
    @Column(name = "create_at",columnDefinition = "TIMESTAMP DEFAULT NOW()")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;

    // FALTA ÚLTIMA ACTUALIZACIÓN


    //este es el espacio
    @ManyToOne
    @JsonIgnore
    private Espacio espacio;

    @OneToMany(mappedBy = "inventarioLevantado")
    private List<Recurso> recursos;

    public InventarioLevantado(boolean status, Espacio espacio) {
        this.status = status;
        this.espacio = espacio;
    }

    public InventarioLevantado() {
    }

    public boolean isStatus() {
        return status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Espacio getEspacio() {
        return espacio;
    }

    public void setEspacio(Espacio espacio) {
        this.espacio = espacio;
    }

    public List<Recurso> getRecursos() {
        return recursos;
    }

    public void setRecursos(List<Recurso> recursos) {
        this.recursos = recursos;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
    @PrePersist
    public void prePersist() {
        // Asignar la fecha de creación automáticamente antes de persistir la entidad
        if (fechaCreacion == null) {
            fechaCreacion = new Date();
        }
    }
}
