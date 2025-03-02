package com.integradora.AssetTrackerUtez.notificacionRegistro.model;

import com.integradora.AssetTrackerUtez.usuario.model.Usuario;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Table(name = "notificaciones_registro")
public class NotificacionRegistro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    private EstadoNotificacion status;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaSolicitud;

    @ManyToOne
    @JoinColumn(name = "administrador_id")
    private Usuario administrador;

    private String comentarios;

    public NotificacionRegistro() {
    }

    public NotificacionRegistro(Long id, Usuario usuario, EstadoNotificacion status, Date fechaSolicitud, Usuario administrador, String comentarios) {
        this.id = id;
        this.usuario = usuario;
        this.status = status;
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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public EstadoNotificacion getStatus() {
        return status;
    }

    public void setStatus(EstadoNotificacion status) {
        this.status = status;
    }

    public Date getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(Date fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public Usuario getAdministrador() {
        return administrador;
    }

    public void setAdministrador(Usuario administrador) {
        this.administrador = administrador;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }
}