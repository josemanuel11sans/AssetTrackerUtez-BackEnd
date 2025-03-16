package com.integradora.AssetTrackerUtez.categoriaRecurso.model;

import com.integradora.AssetTrackerUtez.espacio.model.EspaciosDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CategoriaRecursoDTO {

    @NotNull(groups = {EspaciosDTO.Modify.class, EspaciosDTO.ChangeStatus.class},message = "Es necesario el id")
    private int id;
    @NotBlank(groups = {EspaciosDTO.Modify.class, EspaciosDTO.Register.class}, message = "Es necesario el nombre")
    private String nombre;
    @NotBlank(groups = {EspaciosDTO.Modify.class, EspaciosDTO.Register.class}, message = "Es necesario el material")
    private String material;
    @NotBlank(groups = {EspaciosDTO.Register.class}, message = "Es necesario la url de la imagen")
    private String imagenUrl;
    @NotBlank(groups = {EspaciosDTO.Register.class}, message = "Es necesario el public id de la imagen")
    private String publicId;

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

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public interface Register {
    }

    public interface Modify {
    }

    public interface ChangeStatus {
    }
}
