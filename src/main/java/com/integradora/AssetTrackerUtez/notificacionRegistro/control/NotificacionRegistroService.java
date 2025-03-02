package com.integradora.AssetTrackerUtez.notificacionRegistro.control;

import com.integradora.AssetTrackerUtez.notificacionRegistro.model.*;
import com.integradora.AssetTrackerUtez.usuario.model.Usuario;
import com.integradora.AssetTrackerUtez.usuario.model.UsuarioRepository;
import com.integradora.AssetTrackerUtez.utils.Message;
import com.integradora.AssetTrackerUtez.utils.TypesResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class NotificacionRegistroService {
    private static final Logger logger = LoggerFactory.getLogger(NotificacionRegistroService.class);
    private final NotificacionRegistroRepository notificacionRegistroRepository;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public NotificacionRegistroService(NotificacionRegistroRepository notificacionRegistroRepository, UsuarioRepository usuarioRepository) {
        this.notificacionRegistroRepository = notificacionRegistroRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Message> findAll() {
        List<NotificacionRegistro> notificaciones = notificacionRegistroRepository.findAll();
        logger.info("Búsqueda de todas las notificaciones realizada correctamente");
        return new ResponseEntity<>(new Message(notificaciones, "Listado de notificaciones", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Message> findById(Long id) {
        Optional<NotificacionRegistro> notificacionOptional = notificacionRegistroRepository.findById(id);
        if (notificacionOptional.isPresent()) {
            return new ResponseEntity<>(new Message(notificacionOptional.get(), "Notificación encontrada", TypesResponse.SUCCESS), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new Message("La notificación no existe", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }
    }
    @Transactional(readOnly = true)
    public ResponseEntity<Message> findPendientes() {
        List<NotificacionRegistro> pendientes = notificacionRegistroRepository.findByStatus(EstadoNotificacion.PENDIENTE);

        if (pendientes.isEmpty()) {
            return new ResponseEntity<>(new Message("No hay notificaciones pendientes", TypesResponse.SUCCESS), HttpStatus.OK);
        }

        return new ResponseEntity<>(new Message(pendientes, "Listado de notificaciones pendientes", TypesResponse.SUCCESS), HttpStatus.OK);
    }


    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Message> crearNotificacion(NotificacionRegistroDto dto) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(dto.getUsuario().getId());
        if (!usuarioOptional.isPresent()) {
            return new ResponseEntity<>(new Message("El usuario no existe", TypesResponse.ERROR), HttpStatus.BAD_REQUEST);
        }

        NotificacionRegistro notificacion = new NotificacionRegistro();
        notificacion.setUsuario(usuarioOptional.get());
        notificacion.setStatus(EstadoNotificacion.PENDIENTE);
        notificacion.setFechaSolicitud(new Date());
        notificacion.setComentarios(dto.getComentarios());

        notificacion = notificacionRegistroRepository.save(notificacion);
        if (notificacion == null) {
            return new ResponseEntity<>(new Message("La notificación no se pudo crear", TypesResponse.ERROR), HttpStatus.BAD_REQUEST);
        }

        logger.info("Notificación de registro creada correctamente");
        return new ResponseEntity<>(new Message(notificacion, "Notificación creada", TypesResponse.SUCCESS), HttpStatus.CREATED);
    }

    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Message> actualizarEstado(Long id, EstadoNotificacion estado, String comentarios, Long administradorId) {
        Optional<NotificacionRegistro> notificacionOptional = notificacionRegistroRepository.findById(id);
        if (!notificacionOptional.isPresent()) {
            return new ResponseEntity<>(new Message("La notificación no existe", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }

        NotificacionRegistro notificacion = notificacionOptional.get();
        Optional<Usuario> administradorOptional = usuarioRepository.findById(administradorId);
        if (!administradorOptional.isPresent()) {
            return new ResponseEntity<>(new Message("El administrador no existe", TypesResponse.ERROR), HttpStatus.BAD_REQUEST);
        }

        notificacion.setStatus(estado);
        notificacion.setComentarios(comentarios);
        notificacion.setAdministrador(administradorOptional.get());

        notificacion = notificacionRegistroRepository.save(notificacion);
        if (notificacion == null) {
            return new ResponseEntity<>(new Message("No se pudo actualizar la notificación", TypesResponse.ERROR), HttpStatus.BAD_REQUEST);
        }

        logger.info("Notificación actualizada correctamente");
        return new ResponseEntity<>(new Message(notificacion, "Notificación actualizada", TypesResponse.SUCCESS), HttpStatus.OK);
    }
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Message> aprobarUsuario(Long notificacionId, Long administradorId) {
        Optional<NotificacionRegistro> notificacionOptional = notificacionRegistroRepository.findById(notificacionId);
        if (!notificacionOptional.isPresent()) {
            return new ResponseEntity<>(new Message("La notificación no existe", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }

        NotificacionRegistro notificacion = notificacionOptional.get();
        Usuario usuario = notificacion.getUsuario();

        if (usuario == null) {
            return new ResponseEntity<>(new Message("El usuario asociado no existe", TypesResponse.ERROR), HttpStatus.BAD_REQUEST);
        }

        Optional<Usuario> administradorOptional = usuarioRepository.findById(administradorId);
        if (!administradorOptional.isPresent()) {
            return new ResponseEntity<>(new Message("El administrador no existe", TypesResponse.ERROR), HttpStatus.BAD_REQUEST);
        }

        usuario.setEstado(true);
        notificacion.setStatus(EstadoNotificacion.APROBADO);
        notificacion.setAdministrador(administradorOptional.get());

        usuarioRepository.save(usuario);
        notificacionRegistroRepository.save(notificacion);

        logger.info("Usuario aprobado y notificación actualizada correctamente");
        return new ResponseEntity<>(new Message("Usuario aprobado exitosamente", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Message> rechazarUsuario(Long notificacionId, Long administradorId, String motivo) {
        Optional<NotificacionRegistro> notificacionOptional = notificacionRegistroRepository.findById(notificacionId);
        if (!notificacionOptional.isPresent()) {
            return new ResponseEntity<>(new Message("La notificación no existe", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }

        NotificacionRegistro notificacion = notificacionOptional.get();
        Optional<Usuario> administradorOptional = usuarioRepository.findById(administradorId);
        if (!administradorOptional.isPresent()) {
            return new ResponseEntity<>(new Message("El administrador no existe", TypesResponse.ERROR), HttpStatus.BAD_REQUEST);
        }

        notificacion.setStatus(EstadoNotificacion.RECHAZADO);
        notificacion.setAdministrador(administradorOptional.get());
        notificacion.setComentarios(motivo);

        notificacionRegistroRepository.save(notificacion);

        logger.info("Usuario rechazado y notificación actualizada correctamente");
        return new ResponseEntity<>(new Message("Usuario rechazado exitosamente", TypesResponse.SUCCESS), HttpStatus.OK);
    }

}
