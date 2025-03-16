package com.integradora.AssetTrackerUtez.recurso.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecursosRepository extends JpaRepository<Recurso, Long> {
    List<Recurso> findByStatus(boolean status);

    Optional<Recurso> findById(Long id);
}
