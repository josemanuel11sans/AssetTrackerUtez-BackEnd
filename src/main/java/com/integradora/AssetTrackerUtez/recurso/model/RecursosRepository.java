package com.integradora.AssetTrackerUtez.recurso.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecursosRepository extends JpaRepository<Recurso, Long> {
    List<Recurso> findByStatus(boolean status);
    List<Recurso> findAllByInventarioLevantadoId(Long idInventario);
    Optional<Recurso> findById(Long id);
    long count();
    Optional<Recurso> findFirstByCodigoOrderByFechaCreacionDesc(String codigo);
}
