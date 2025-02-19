package com.integradora.AssetTrackerUtez.usuario.control;

import com.integradora.AssetTrackerUtez.rol.model.Rol;
import com.integradora.AssetTrackerUtez.rol.model.RolRepository;
import com.integradora.AssetTrackerUtez.usuario.model.Usuario;
import com.integradora.AssetTrackerUtez.usuario.model.UsuarioDto;
import com.integradora.AssetTrackerUtez.usuario.model.UsuarioRepository;
import com.integradora.AssetTrackerUtez.utils.Message;
import com.integradora.AssetTrackerUtez.utils.TypesResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;

    //private final PasswordEncoder passwordEncoder;
    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, RolRepository rolRepository/*, PasswordEncoder passwordEncoder*/) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        /*this.passwordEncoder = passwordEncoder;*/
    }
    @Transactional(readOnly = true)
    public ResponseEntity<Message> findAll() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        logger.info("La búsqueda de todos los usuarios ha sido realizada correctamente");
        return new ResponseEntity<>(new Message(usuarios, "Listado de usuarios", TypesResponse.SUCCESS), HttpStatus.OK);
    }
    @Transactional(readOnly = true)
    public ResponseEntity<Message> findById(Long id) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        if (usuarioOptional.isPresent()) {
            Usuario foundCliente = usuarioOptional.get();
            return new ResponseEntity<>(new Message(foundCliente, "Usuario encontrado", TypesResponse.SUCCESS), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new Message("El usuario no existe", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }
    }
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Message> save(UsuarioDto dto) {
        // Validación de longitudes
        if (dto.getNombre().length() > 100) {
            return new ResponseEntity<>(new Message("El nombre excede el número de caracteres permitidos", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        if (dto.getApellidos().length() > 100) {
            return new ResponseEntity<>(new Message("Los apellidos exceden el número de caracteres permitidos", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        if (dto.getCorreo().length() > 100) {
            return new ResponseEntity<>(new Message("El correo excede el número de caracteres permitidos", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        if (usuarioRepository.existsByCorreo(dto.getCorreo())) { return new ResponseEntity<>(new Message("El correo ya está registrado", TypesResponse.WARNING), HttpStatus.BAD_REQUEST); }
        // Guardar el usuario
        Optional<Rol> optionalRole = rolRepository.findByNombre(dto.getRol()); // Cambia 'getRole' según tu DTO
        System.out.println("Rol recibido: " + dto.getRol());
        if (optionalRole.isEmpty()) {
            return new ResponseEntity<>(new Message("Rol no encontrado", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        Rol rol = optionalRole.get();
        //String contrasenaEncriptada = passwordEncoder.encode(dto.getContrasena());
        Usuario usuario = new Usuario(
                dto.getNombre(),
                dto.getApellidos(),
                dto.getCorreo(),
                //contrasenaEncriptada,
                dto.getContrasena(),
                true,
                dto.getFechaCreacion(),
                dto.getUltimaActualizacion()
        );
        // Asociar el rol al usuario
        usuario.getRol().add(rol);
        usuario = usuarioRepository.saveAndFlush(usuario);
        if (usuario == null) {
            return new ResponseEntity<>(new Message("El usuario no se registró", TypesResponse.ERROR), HttpStatus.BAD_REQUEST);
        }
        logger.info("El usuario se registró correctamente");
        return new ResponseEntity<>(new Message(usuario.getCorreo(), "Usuario registrado correctamente", TypesResponse.SUCCESS), HttpStatus.CREATED);
    }
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Message> update(UsuarioDto dto) {
        if (dto.getId() == null) {
            return new ResponseEntity<>(new Message("El id del usuario no puede ser nulo", TypesResponse.ERROR), HttpStatus.BAD_REQUEST);
        }
        // Validación de longitudes
        if (dto.getNombre().length() > 100) {
            return new ResponseEntity<>(new Message("El nombre excede el número de caracteres permitidos", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        if (dto.getApellidos().length() > 100) {
            return new ResponseEntity<>(new Message("Los apellidos exceden el número de caracteres permitidos", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        if (dto.getCorreo().length() > 100) {
            return new ResponseEntity<>(new Message("El correo excede el número de caracteres permitidos", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }

        Optional<Usuario> usuarioOptional = usuarioRepository.findById(dto.getId());
        System.out.println(dto.getId());
        if (!usuarioOptional.isPresent()) {
            return new ResponseEntity<>(new Message("El usuario no existe", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }
        Usuario usuario = usuarioOptional.get();
        usuario.setNombre(dto.getNombre());
        usuario.setApellidos(dto.getApellidos());
        usuario.setCorreo(dto.getCorreo());
        usuario.setUltimaActualizacion(new Date());
        usuario = usuarioRepository.saveAndFlush(usuario);
        if (usuario == null) {
            return new ResponseEntity<>(new Message("El usuario no se actualizó", TypesResponse.ERROR), HttpStatus.BAD_REQUEST);
        }
        logger.info("La actualización del usuario ha sido realizada correctamente");
        return new ResponseEntity<>(new Message(usuario.getNombre(), "Usuario actualizado correctamente", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Message> changeStatus(UsuarioDto dto) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(dto.getId());
        if (!usuarioOptional.isPresent()) {
            return new ResponseEntity<>(new Message("El usuario no existe", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }
        Usuario usuario = usuarioOptional.get();
        usuario.setEstado(!usuario.isEstado());
        usuario = usuarioRepository.saveAndFlush(usuario);
        if (usuario == null) {
            return new ResponseEntity<>(new Message("El estado del usuario no se actualizó", TypesResponse.ERROR), HttpStatus.BAD_REQUEST);
        }
        logger.info("El estado del usuario se actualizó correctamente");
        return new ResponseEntity<>(new Message(usuario, "Estado del usuario actualizado correctamente", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Message> findActives(){
        List<Usuario> respuestas = usuarioRepository.findAllByEstadoIsTrue();
        logger.info("Lista de usuarios activos");
        return new ResponseEntity<>(new Message(respuestas, "Usuarios con status activo", TypesResponse.SUCCESS), HttpStatus.OK);
    }
}
