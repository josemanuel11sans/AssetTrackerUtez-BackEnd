package com.integradora.AssetTrackerUtez.usuario.model;

import com.integradora.AssetTrackerUtez.edificio.model.Edificio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findById(Long id);

    List<Usuario> findAll();

    Optional<Usuario> findFirstByCorreo(String correo);

    Optional<Usuario> findFirstByCorreoAndCodigo(String correo, String code);

    List<Usuario> findAllByEstadoIsTrue();
    boolean existsByCorreo(String correo);
}
