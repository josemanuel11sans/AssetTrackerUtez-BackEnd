package com.integradora.AssetTrackerUtez.inventarioLevantado.model;
import jakarta.validation.constraints.NotNull;

public class InventarioLevantadoDTO {

    @NotNull(groups = {InventarioLevantadoDTO.Modify.class, InventarioLevantadoDTO.ChangeStatus.class},message = "Es necesario el id")
    private  Long id;
    @NotNull(groups = {InventarioLevantadoDTO.Modify.class, InventarioLevantadoDTO.Register.class}, message = "Es nesesario asignar un espacio")
    private int espacio;

    public int getEspacio() {
        return espacio;
    }

    public void setEspacio(int espacio) {
        this.espacio = espacio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public interface Register {
    }

    public interface Modify {
    }

    public interface ChangeStatus {
    }
}
