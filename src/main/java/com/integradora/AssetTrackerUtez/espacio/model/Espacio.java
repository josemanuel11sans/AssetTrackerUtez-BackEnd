package com.integradora.AssetTrackerUtez.espacio.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.integradora.AssetTrackerUtez.categoriaEspacio.model.CategoriaEspacio;
import com.integradora.AssetTrackerUtez.edificio.model.Edificio;
import com.integradora.AssetTrackerUtez.inventarioLevantado.model.InventarioLevantado;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "espacios")
public class Espacio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(name = "nombre", columnDefinition = "VARCHAR(100)")
    private String nombre;

    @NotNull(message = "El número de planta es obligatorio")
    @Column(name = "numeroPlanta", columnDefinition = "INT")
    private Integer numeroPlanta;

    // FALTA IMAGEN

    @Column(name = "status", columnDefinition = "BOOL DEFAULT TRUE")
    private boolean status = true;

    //Este campo no tiene ni getter ni setter
    @Column(name = "create_at",columnDefinition = "TIMESTAMP DEFAULT NOW()")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;

    // FALTA ÚLTIMA ACTUALIZACIÓN

    @ManyToOne
    @JsonIgnore
    private Edificio edificio;

    @ManyToOne
    @JsonIgnore
    private CategoriaEspacio categoriaEspacio;

    @OneToMany (mappedBy = "espacio")
    private List<InventarioLevantado> inventariosLevantados;

    public Espacio() {
    }
}
