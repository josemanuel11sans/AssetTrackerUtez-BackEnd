package com.integradora.AssetTrackerUtez.usuario.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findById(Long id);
    @Query(value = "SELECT \n" +
            "    u.id AS usuario_id,                -- 0\n" +
            "    u.nombre AS usuario_nombre,        -- 1\n" +
            "    u.apellidos AS usuario_apellidos,  -- 2\n" +
            "    u.correo AS usuario_correo,        -- 3\n" +
            "    u.contrasena AS usuario_contrasena,-- 4\n" +
            "    u.estado AS usuario_estado,        -- 5\n" +
            "    u.fecha_creacion AS fecha_creacion,-- 6\n" +
            "    u.ultima_actualizacion AS ultima_actualizacion, -- 7\n" +
            "    u.codigo AS usuario_codigo,        -- 8\n" +
            "    r.id AS rol_id,                    -- 9\n" +
            "    r.nombre AS rol_nombre             -- 10\n" +
            "FROM \n" +
            "    usuarios u \n" +
            "JOIN \n" +
            "    usuario_role ur ON u.id = ur.usuario_id \n" +
            "JOIN \n" +
            "    rol r ON ur.role_id = r.id WHERE u.id = ?;", nativeQuery = true)
    List<Object[]> findByIdWithRolesNative(Long id);

    List<Usuario> findAll();
    @Query(value = "SELECT \n" +
            "    u.id AS usuario_id,                -- 0\n" +
            "    u.nombre AS usuario_nombre,        -- 1\n" +
            "    u.apellidos AS usuario_apellidos,  -- 2\n" +
            "    u.correo AS usuario_correo,        -- 3\n" +
            "    u.contrasena AS usuario_contrasena,-- 4\n" +
            "    u.estado AS usuario_estado,        -- 5\n" +
            "    u.fecha_creacion AS fecha_creacion,-- 6\n" +
            "    u.ultima_actualizacion AS ultima_actualizacion, -- 7\n" +
            "    u.codigo AS usuario_codigo,        -- 8\n" +
            "    r.id AS rol_id,                    -- 9\n" +
            "    r.nombre AS rol_nombre             -- 10\n" +
            "FROM \n" +
            "    usuarios u \n" +
            "JOIN \n" +
            "    usuario_role ur ON u.id = ur.usuario_id \n" +
            "JOIN \n" +
            "    rol r ON ur.role_id = r.id;", nativeQuery = true)
    List<Object[]> findAllWithRolesNative();


    Optional<Usuario> findFirstByCorreo(String correo);

    Optional<Usuario> findFirstByCorreoAndCodigo(String correo, String code);

    List<Usuario> findAllByEstadoIsTrue();
    boolean existsByCorreo(String correo);
}
