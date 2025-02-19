package com.integradora.AssetTrackerUtez.usuario.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.integradora.AssetTrackerUtez.rol.model.Rol;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(name = "nombre", columnDefinition = "VARCHAR(100)")
    private String nombre;

    @NotBlank(message = "Los apellidos son obligatorios")
    @Column(name = "apellidos", columnDefinition = "VARCHAR(100)")
    private String apellidos;

    @Email(message = "Debe ser un correo válido")
    @NotBlank(message = "El correo es obligatorio")
    @Column(name = "correo", columnDefinition = "VARCHAR(100)", unique = true)
    private String correo;

    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    @Column(name = "contrasena", columnDefinition = "VARCHAR(255)")
    private String contrasena;

    @Column(name = "estado", columnDefinition = "BOOL DEFAULT TRUE")
    private boolean estado = true;

    //Este campo no tiene ni getter ni setter
    @CreationTimestamp
    @Column(name = "fechaCreacion",columnDefinition = "TIMESTAMP DEFAULT NOW()")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @UpdateTimestamp
    @Column(name = "ultimaActualizacion",columnDefinition = "TIMESTAMP DEFAULT NOW()")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ultimaActualizacion;
    @Column(name = "codigo", columnDefinition = "VARCHAR(10)")
    private String codigo;

    @ManyToMany
    @JsonIgnore
    @JoinTable(
            name = "usuario_role",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Rol> rol = new HashSet<>();

    public Usuario() {
    }

    public Usuario(String nombre, String apellidos, String correo, String contrasena, boolean estado, Set<Rol> rol) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.correo = correo;
        this.contrasena = contrasena;
        this.estado = estado;
        this.rol = rol;
    }

    public Usuario(String nombre, String apellidos, String correo, String contrasena, boolean estado, Date fechaCreacion, Date ultimaActualizacion, String codigo, Set<Rol> rol) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.correo = correo;
        this.contrasena = contrasena;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.ultimaActualizacion = ultimaActualizacion;
        this.codigo = codigo;
        this.rol = rol;
    }

    public Usuario(String nombre, String apellidos, String correo, String contrasena, boolean estado, Date fechaCreacion, Date ultimaActualizacion) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.correo = correo;
        this.contrasena = contrasena;
        this.estado = estado;
        this.fechaCreacion = new Date();
        this.ultimaActualizacion = ultimaActualizacion;
    }

    public Usuario(String nombre, String apellidos, String correo, String contrasena, boolean estado, Date fechaCreacion, Date ultimaActualizacion, Set<Rol> rol) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.correo = correo;
        this.contrasena = contrasena;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.ultimaActualizacion = ultimaActualizacion;
        this.rol = rol;
    }

    public Date getUltimaActualizacion() {
        return ultimaActualizacion;
    }

    public void setUltimaActualizacion(Date ultimaActualizacion) {
        this.ultimaActualizacion = ultimaActualizacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getApellido() {
        return apellidos;
    }

    public void setApellido(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Set<Rol> getRol() {
        return rol;
    }

    public void setRol(Set<Rol> rol) {
        this.rol = rol;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}
