package com.integradora.AssetTrackerUtez.inventarioLevantado.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InventarioLevantadoRepository extends JpaRepository<InventarioLevantado, Long> {
    // es una  query personalizada
    @Query("SELECT i FROM InventarioLevantado i LEFT JOIN FETCH i.espacio LEFT JOIN FETCH i.recursos")
    List<InventarioLevantado> findAllWithEspacioYRecursos();

    List<InventarioLevantado> findAllByStatus(boolean status);
}
