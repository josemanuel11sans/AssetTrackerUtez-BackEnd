package com.integradora.AssetTrackerUtez.espacio.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EspacioRepository  extends JpaRepository<Espacio, Long> {
    //Método para buscar todos los espacios por status
    List<Espacio> findAllByStatusOrderByNombre(boolean status);
    boolean existsByNombre(String nombre);
    //buscar por id
    Optional<Espacio> findById(Long id);


}
