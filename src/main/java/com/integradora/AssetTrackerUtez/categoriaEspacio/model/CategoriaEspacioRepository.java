package com.integradora.AssetTrackerUtez.categoriaEspacio.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoriaEspacioRepository extends JpaRepository<CategoriaEspacio, Long> {
    Optional<CategoriaEspacio> findById(Long id);
    List<CategoriaEspacio> findAll();
    List<CategoriaEspacio> findAllByEstadoIsTrue();
}
