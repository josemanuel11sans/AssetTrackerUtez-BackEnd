package com.integradora.AssetTrackerUtez.responsable.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ResponsableRepository extends JpaRepository<Responsable, Long> {
    Optional<Responsable> findById(Long id);
    List<Responsable> findAll();
    List<Responsable> findAllByEstadoIsTrue();
}
