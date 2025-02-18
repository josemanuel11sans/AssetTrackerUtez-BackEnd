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

    //Este campo no tiene ni getter ni setter
    @Column(name = "create_at",columnDefinition = "TIMESTAMP DEFAULT NOW()")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;

    // FALTA ÚLTIMA ACTUALIZACIÓN

    @OneToMany (mappedBy = "edificio")
    private List<Espacio> espacios;

    public Edificio() {
    }

    public Edificio(String nombre, Integer numeroPisos, boolean status) {
        this.nombre = nombre;
        this.numeroPisos = numeroPisos;
        this.status = status;
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
}
