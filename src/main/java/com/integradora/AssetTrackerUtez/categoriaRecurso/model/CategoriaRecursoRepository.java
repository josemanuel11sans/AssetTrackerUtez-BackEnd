package com.integradora.AssetTrackerUtez.categoriaRecurso.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoriaRecursoRepository  extends JpaRepository<CategoriaRecurso,Long> {
    List<CategoriaRecurso> findAllByStatusOrderByNombre(boolean status);
    boolean existsByNombre(String nombre);
    //material ya existente por el material
    boolean existsByMaterial(String material);
    Optional<CategoriaRecurso> findById(Long id);
}
