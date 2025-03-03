package com.integradora.AssetTrackerUtez.edificio.model;

import com.integradora.AssetTrackerUtez.espacio.model.Espacio;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.List;


@Entity
@Table(name = "edificios")
public class Edificio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(name = "nombre", columnDefinition = "VARCHAR(100)")
    private String nombre;

    @NotNull(message = "El número de pisos es obligatorio")
    @Column(name = "numeroPisos", columnDefinition = "INT")
    private Integer numeroPisos;

    @Column(name = "status", columnDefinition = "BOOL DEFAULT TRUE")
    private boolean status = true;
    // Columna para almacenar la fecha de creación
    @Column(name = "create_at", columnDefinition = "TIMESTAMP DEFAULT NOW()")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    // Columna para almacenar la fecha de actualización
    @Column(name = "update_at", columnDefinition = "TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaActualizacion;

    @PreUpdate
    protected void onUpdate() {
        this.fechaActualizacion = new Date(); // Establece la fecha y hora actuales al actualizar
    }

    @OneToMany(mappedBy = "edificio")
    private List<Espacio> espacios;

    // Constructor vacío (obligatorio para JPA)
    public Edificio() {
    }

    // Constructor con parámetros (sin fechaCreacion)
    public Edificio(String nombre, Integer numeroPisos, boolean status) {
        this.nombre = nombre;
        this.numeroPisos = numeroPisos;
        this.status = status;
    }

    // Método para establecer la fecha de creación automáticamente
    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = new Date(); // Establece la fecha y hora actuales
    }

    // Getters y Setters
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

    public Integer getNumeroPisos() {
        return numeroPisos;
    }

    public void setNumeroPisos(Integer numeroPisos) {
        this.numeroPisos = numeroPisos;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public List<Espacio> getEspacios() {
        return espacios;
    }

    public void setEspacios(List<Espacio> espacios) {
        this.espacios = espacios;
    }
}