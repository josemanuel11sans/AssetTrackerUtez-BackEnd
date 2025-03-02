package com.integradora.AssetTrackerUtez.edificio.model;

import com.integradora.AssetTrackerUtez.usuario.model.UsuarioDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class EdificioDTO {
    //Atributos
    @NotNull(groups = {UsuarioDto.Modify.class, UsuarioDto.ChangeStatus.class}, message = "Es necesario el id")
    private Long id;
    @NotBlank(groups = {UsuarioDto.Register.class, UsuarioDto.Modify.class}, message = "El nombre no puede quedar vacío")
    private String nombre;
    @NotNull(groups = {UsuarioDto.Register.class, UsuarioDto.Modify.class}, message ="Es nesesario el numero de pisos")
    private Integer numeroPisos;

    //Constructores
    public EdificioDTO() {
    }

    public EdificioDTO(Long id, String nombre, Integer numeroPisos) {
        this.id = id;
        this.nombre = nombre;
        this.numeroPisos = numeroPisos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    //Interfaces validación
    public interface Register {}
    public interface Modify {}
    public interface ChangeStatus {}
    public interface FindByEmail {}
    public interface VerifyCode {}
}
