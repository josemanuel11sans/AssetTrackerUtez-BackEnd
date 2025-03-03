package com.integradora.AssetTrackerUtez.edificio.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EdificioRepository  extends JpaRepository<Edificio, Long> {
    //listar ediificios depende del estado
    List<Edificio> findAllByStatusOrderByNombreAsc(boolean status);
    //listar edificios
    List<Edificio> findAll();
    //buscar edificio por id
    //Edificio findById(Long id);
    //valida si existe el nombre regresar un boolean
    boolean existsByNombre(String nombre);
    //Edificio saveAndFlush(Edificio edificio);
}
