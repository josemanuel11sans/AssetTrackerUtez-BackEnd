package com.integradora.AssetTrackerUtez.categoriaRecurso.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoriaRecursoRepository  extends JpaRepository<CategoriaRecurso,Long> {
    List<CategoriaRecurso> findAllByStatusOrderByNombre(boolean status);
    boolean existsByNombre(String nombre);
}
