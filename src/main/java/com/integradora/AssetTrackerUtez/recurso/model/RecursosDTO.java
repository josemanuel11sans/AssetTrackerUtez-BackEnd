package com.integradora.AssetTrackerUtez.recurso.model;

import com.integradora.AssetTrackerUtez.inventarioLevantado.model.InventarioLevantadoDTO;
import jakarta.validation.constraints.NotNull;

public class RecursosDTO {
    @NotNull(groups = {RecursosDTO.Modify.class, RecursosDTO.ChangeStatus.class},message = "Es necesario el id")
    private Long id;
    @NotNull(groups = {RecursosDTO.Modify.class, RecursosDTO.Register.class}, message = "Es nesesario el codigo del recurso")
    private String codigo;
    //puede o no puede estar
    private String descripcion;
    //puede o no puede estar
    private String marca;
    //puede o no puede estar
    private String modelo;
    //puede o no puede estar
    private String numeroSerie;
    //puede o no puede esatr
    private String observaciones;
    
    @NotNull(groups = {RecursosDTO.Modify.class, RecursosDTO.Register.class}, message = "Es necesario asignar el inventario al que pertenece")
    private int invetariolevantadoid;
    @NotNull(groups = {RecursosDTO.Modify.class, RecursosDTO.Register.class}, message = "Es necesario asignar una categoria")
    private int categoriaRecursoid;
    @NotNull(groups = {RecursosDTO.Modify.class, RecursosDTO.Register.class}, message = "Es necesario un asignar un responsable ")
    private int responsableid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getNumeroSerie() {
        return numeroSerie;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public int getInvetariolevantadoid() {
        return invetariolevantadoid;
    }

    public void setInvetariolevantadoid(int invetariolevantadoid) {
        this.invetariolevantadoid = invetariolevantadoid;
    }

    public int getCategoriaRecursoid() {
        return categoriaRecursoid;
    }

    public void setCategoriaRecursoid(int categoriaRecursoid) {
        this.categoriaRecursoid = categoriaRecursoid;
    }

    public int getResponsableid() {
        return responsableid;
    }

    public void setResponsableid(int responsableid) {
        this.responsableid = responsableid;
    }

    public interface Register {}
    public interface Modify {}
    public interface ChangeStatus {}
}
