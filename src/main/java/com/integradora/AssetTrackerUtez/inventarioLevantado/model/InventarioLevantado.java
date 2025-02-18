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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "status", columnDefinition = "BOOL DEFAULT TRUE")
    private boolean status = true;

    //Este campo no tiene ni getter ni setter
    @Column(name = "create_at",columnDefinition = "TIMESTAMP DEFAULT NOW()")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;

    // FALTA ÚLTIMA ACTUALIZACIÓN

    @ManyToOne
    @JsonIgnore
    private Espacio espacio;

    @OneToMany(mappedBy = "inventarioLevantado")
    private List<Recurso> recursos;

    public InventarioLevantado() {
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
