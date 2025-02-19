package com.integradora.AssetTrackerUtez.usuario.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

public class UsuarioDto {
    @NotNull(groups = {Modify.class, ChangeStatus.class}, message = "Es necesario el id")
    private Long id;
    @NotBlank(groups = {Register.class, Modify.class}, message = "El nombre no puede quedar vacío")
    private String nombre;
    @NotBlank(groups = {Register.class, Modify.class}, message = "Los apellidos no pueden quedar vacíos")
    private String apellidos;
    @NotBlank(groups = {Register.class, Modify.class, FindByEmail.class, VerifyCode.class}, message = "El correo no puede quedar vacío")
    private String correo;
    @NotBlank(groups = {Register.class, Modify.class}, message = "La contraseña no puede quedar vacía")
    private String contrasena;
    @NotBlank(groups = {Modify.class, Register.class}, message = "Es necesario el rol")
    private String rol;
    @NotBlank(groups = {VerifyCode.class})
    private String code;
    @NotNull(groups = {Register.class, Modify.class})
    private Date fechaCreacion;
    @NotNull(groups = {Register.class, Modify.class})
    private Date ultimaActualizacion;

    public UsuarioDto() {
    }

    public UsuarioDto(Long id, String nombre, String apellidos, String correo, String contrasena, String rol, String code, Date fechaCreacion, Date ultimaActualizacion) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.correo = correo;
        this.contrasena = contrasena;
        this.rol = rol;
        this.code = code;
        this.fechaCreacion = fechaCreacion;
        this.ultimaActualizacion = ultimaActualizacion;
    }

    public UsuarioDto(Long id, String nombre, String apellidos, String correo, String contrasena, Date fechaCreacion, Date ultimaActualizacion) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.correo = correo;
        this.contrasena = contrasena;
        this.fechaCreacion = fechaCreacion;
        this.ultimaActualizacion = ultimaActualizacion;
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

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getUltimaActualizacion() {
        return ultimaActualizacion;
    }

    public void setUltimaActualizacion(Date ultimaActualizacion) {
        this.ultimaActualizacion = ultimaActualizacion;
    }

    //Interfaces validación
    public interface Register {}
    public interface Modify {}
    public interface ChangeStatus {}
    public interface FindByEmail {}
    public interface VerifyCode {}
}
