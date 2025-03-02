package com.integradora.AssetTrackerUtez.notificacionRegistro.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificacionRegistroRepository extends JpaRepository<NotificacionRegistro, Long> {
    List<NotificacionRegistro> findByStatus(EstadoNotificacion status);

    List<NotificacionRegistro> findAll();
}
