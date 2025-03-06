package com.integradora.AssetTrackerUtez.espacio.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EspacioRepository  extends JpaRepository<Espacio, Long> {
    //Método para buscar todos los espacios por status
    List<Espacio> findAllByStatusOrderByNombre(boolean status);
    boolean existsByNombre(String nombre);
}
