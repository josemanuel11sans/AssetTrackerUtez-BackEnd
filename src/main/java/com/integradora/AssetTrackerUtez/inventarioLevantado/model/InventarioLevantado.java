package com.integradora.AssetTrackerUtez.inventarioLevantado.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    private Long id;
    //status

    @Column(name = "status", columnDefinition = "BOOL DEFAULT TRUE")
    private boolean status = true;
    //esta es la fecha de creacion
    //Este campo no tiene ni getter ni setter
    @Column(name = "create_at",columnDefinition = "TIMESTAMP DEFAULT NOW()")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;

    @Column(name = "update_at", columnDefinition = "TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaActualizacion;


    //este es el espacio
    @ManyToOne
    @JsonIgnoreProperties({"nombre", "numeroPlanta", "urlImagen", "status", "publicId","fechaCreacion","edificio","fechaActualizacion","categoriaEspacio"})
    private Espacio espacio;

    @OneToMany(mappedBy = "inventarioLevantado")
    @JsonIgnore
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(Date fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
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
