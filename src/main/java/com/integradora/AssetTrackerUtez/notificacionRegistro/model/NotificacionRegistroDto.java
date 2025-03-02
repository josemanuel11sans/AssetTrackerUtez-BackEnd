package com.integradora.AssetTrackerUtez.notificacionRegistro.model;

import com.integradora.AssetTrackerUtez.usuario.model.UsuarioDto;
import jakarta.validation.constraints.NotNull;
import java.util.Date;

public class NotificacionRegistroDto {

    private Long id;

    @NotNull(message = "El usuario es obligatorio")
    private UsuarioDto usuario;

    private EstadoNotificacion estado;

    private Date fechaSolicitud;

    private UsuarioDto administrador;

    private String comentarios;

    public NotificacionRegistroDto() {
    }

    public NotificacionRegistroDto(Long id, UsuarioDto usuario, EstadoNotificacion estado, Date fechaSolicitud, UsuarioDto administrador, String comentarios) {
        this.id = id;
        this.usuario = usuario;
        this.estado = estado;
        this.fechaSolicitud = fechaSolicitud;
        this.administrador = administrador;
        this.comentarios = comentarios;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UsuarioDto getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioDto usuario) {
        this.usuario = usuario;
    }

    public EstadoNotificacion getEstado() {
        return estado;
    }

    public void setEstado(EstadoNotificacion estado) {
        this.estado = estado;
    }

    public Date getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(Date fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public UsuarioDto getAdministrador() {
        return administrador;
    }

    public void setAdministrador(UsuarioDto administrador) {
        this.administrador = administrador;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }
}