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
    //id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    //nombre
    @Column(name = "nombre", columnDefinition = "VARCHAR(100)")
    private String nombre;
    //numeroPlanta
    @Column(name = "numeroPlanta", columnDefinition = "INT")
    private Integer numeroPlanta;
    // urlImagen
    @Column(name = "urlImagen", columnDefinition = "VARCHAR(255)")
    private String urlImagen;
    @Column(name = "status", columnDefinition = "BOOL DEFAULT TRUE")
    private boolean status = true;
    @Column(name = "publicid", columnDefinition = "VARCHAR(255)")
    private String publicId;



    //Este campo no tiene ni getter ni setter
    @Column(name = "create_at", insertable = false  ,columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    //varios espacios pueden pertenecer a un edificio
    @ManyToOne
    @JsonIgnore
    private Edificio edificio;
    //varios espacios pueden pertenecer a una categoria
    @ManyToOne
    @JsonIgnore
    private CategoriaEspacio categoriaEspacio;
    //un espacio puede tener varios inventarios levantados
    @OneToMany (mappedBy = "espacio")
    private List<InventarioLevantado> inventariosLevantados;

    public Espacio() {
    }

    public Espacio(String nombre, Integer numeroPlanta, String urlImagen,String publicId, boolean status) {
        this.nombre = nombre;
        this.numeroPlanta = numeroPlanta;
        this.urlImagen = urlImagen;
        this.status = status;
        this.publicId = publicId;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

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

    public Integer getNumeroPlanta() {
        return numeroPlanta;
    }

    public void setNumeroPlanta(Integer numeroPlanta) {
        this.numeroPlanta = numeroPlanta;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
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

    public Edificio getEdificio() {
        return edificio;
    }

    public void setEdificio(Edificio edificio) {
        this.edificio = edificio;
    }

    public CategoriaEspacio getCategoriaEspacio() {
        return categoriaEspacio;
    }

    public void setCategoriaEspacio(CategoriaEspacio categoriaEspacio) {
        this.categoriaEspacio = categoriaEspacio;
    }

    public List<InventarioLevantado> getInventariosLevantados() {
        return inventariosLevantados;
    }

    public void setInventariosLevantados(List<InventarioLevantado> inventariosLevantados) {
        this.inventariosLevantados = inventariosLevantados;
    }
}
