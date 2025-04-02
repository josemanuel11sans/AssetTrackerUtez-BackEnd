package com.integradora.AssetTrackerUtez.usuario.control;

import com.integradora.AssetTrackerUtez.notificacionRegistro.control.NotificacionRegistroService;
import com.integradora.AssetTrackerUtez.notificacionRegistro.model.NotificacionRegistroDto;
import com.integradora.AssetTrackerUtez.notificacionRegistro.model.NotificacionRegistroRepository;
import com.integradora.AssetTrackerUtez.rol.model.Rol;
import com.integradora.AssetTrackerUtez.rol.model.RolRepository;
import com.integradora.AssetTrackerUtez.usuario.model.Usuario;
import com.integradora.AssetTrackerUtez.usuario.model.UsuarioDto;
import com.integradora.AssetTrackerUtez.usuario.model.UsuarioRepository;
import com.integradora.AssetTrackerUtez.utils.EmailSender;
import com.integradora.AssetTrackerUtez.utils.Message;
import com.integradora.AssetTrackerUtez.utils.TypesResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UsuarioService {
    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final NotificacionRegistroRepository notificacionRegistroRepository;
    private final  NotificacionRegistroService notificacionRegistroService;
    private final EmailSender emailSender;

    private final PasswordEncoder passwordEncoder;
    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, RolRepository rolRepository, PasswordEncoder passwordEncoder, NotificacionRegistroRepository notificacionRegistroRepository, NotificacionRegistroService notificacionRegistroService, EmailSender emailSender) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
        this.notificacionRegistroRepository = notificacionRegistroRepository;
        this.notificacionRegistroService = notificacionRegistroService;
        this.emailSender = emailSender;
    }
    @Transactional(readOnly = true)
    public ResponseEntity<Message> findAll() {
        // Usar la consulta nativa para obtener los datos
        List<Object[]> usuarios = usuarioRepository.findAllWithRolesNative();

        // Convertir los resultados al formato deseado
        List<Map<String, Object>> usuariosConRoles = usuarios.stream().map(fila -> {
            Map<String, Object> usuario = new HashMap<>();
            usuario.put("id", fila[0]);                 // usuario_id
            usuario.put("nombre", fila[1]);             // usuario_nombre
            usuario.put("apellidos", fila[2]);          // usuario_apellidos
            usuario.put("correo", fila[3]);             // usuario_correo
            usuario.put("contrasena", fila[4]);          // usuario_ontraseña
            usuario.put("estado", fila[5]);              // usuario_estado
            usuario.put("fechaCreacion", fila[6]);       // fecha_creacion
            usuario.put("ultimaActualizacion", fila[7]); // ultima_actualizacion
            usuario.put("codigo", fila[8]);              // usuario_codigo
            usuario.put("rolId", fila[9]);               // rol_id
            usuario.put("rol", fila[10]);                // rol_nombre
            return usuario;
        }).collect(Collectors.toList());

        // Log y retorno de la respuesta
        logger.info("La búsqueda de todos los usuarios ha sido realizada correctamente");
        return new ResponseEntity<>(new Message(usuariosConRoles, "Listado de usuarios", TypesResponse.SUCCESS), HttpStatus.OK);
    }
    @Transactional(readOnly = true)
    public ResponseEntity<Message> findById(Long id) {
        List<Object[]> resultados = usuarioRepository.findByIdWithRolesNative(id);

        if (resultados.isEmpty()) {
            return new ResponseEntity<>(new Message("El usuario no existe", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }

        // Tomar los datos comunes del usuario desde la primera fila
        List<Map<String, Object>> usuarioConRol = resultados.stream().map(fila -> {
            Map<String, Object> usuario = new HashMap<>();
            usuario.put("id", fila[0]);                 // usuario_id
            usuario.put("nombre", fila[1]);             // usuario_nombre
            usuario.put("apellidos", fila[2]);          // usuario_apellidos
            usuario.put("correo", fila[3]);             // usuario_correo
            usuario.put("contrasena", fila[4]);          // usuario_ontraseña
            usuario.put("estado", fila[5]);              // usuario_estado
            usuario.put("fechaCreacion", fila[6]);       // fecha_creacion
            usuario.put("ultimaActualizacion", fila[7]); // ultima_actualizacion
            usuario.put("codigo", fila[8]);              // usuario_codigo
            usuario.put("rolId", fila[9]);               // rol_id
            usuario.put("rol", fila[10]);                // rol_nombre
            return usuario;
        }).collect(Collectors.toList());

        return new ResponseEntity<>(new Message(usuarioConRol, "Usuario encontrado", TypesResponse.SUCCESS), HttpStatus.OK);
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
        String contrasenaEncriptada = passwordEncoder.encode(dto.getContrasena());
        Usuario usuario = new Usuario(
                dto.getNombre(),
                dto.getApellidos(),
                dto.getCorreo(),
                contrasenaEncriptada,
                //dto.getContrasena(),
                false,
                dto.getFechaCreacion(),
                dto.getUltimaActualizacion()
        );
        // Asociar el rol al usuario
        usuario.getRol().add(rol);
        usuario = usuarioRepository.saveAndFlush(usuario);
        logger.info(String.valueOf(rol));
        if (usuario == null) {
            return new ResponseEntity<>(new Message("El usuario no se registró", TypesResponse.ERROR), HttpStatus.BAD_REQUEST);
        }
        // Crear notificación de registro
        NotificacionRegistroDto notificacionDto = new NotificacionRegistroDto();
        notificacionDto.setUsuario(new UsuarioDto(usuario.getId(), usuario.getNombre(), usuario.getApellidos(), usuario.getCorreo(), null, null, null, null, null));
        notificacionRegistroService.crearNotificacion(notificacionDto);
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

    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Message> delete(UsuarioDto dto) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(dto.getId());
        if (!usuarioOptional.isPresent()) {
            return new ResponseEntity<>(new Message("El usuario no existe", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }
        try {
            usuarioRepository.deleteById(dto.getId());
            logger.info("El usuario fue eliminado correctamente");
            return new ResponseEntity<>(new Message("Usuario eliminado correctamente", TypesResponse.SUCCESS), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Message("No se pudo eliminar el usuario", TypesResponse.ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Message> findActives(){
        List<Usuario> respuestas = usuarioRepository.findAllByEstadoIsTrue();
        logger.info("Lista de usuarios activos");
        return new ResponseEntity<>(new Message(respuestas, "Usuarios con status activo", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    //Para cambio de contraseña
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Message> cambiarContra(UsuarioDto dto){
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(dto.getId());
        System.out.println(dto.getId());
        if (!usuarioOptional.isPresent()) {
            return new ResponseEntity<>(new Message("El usuario no existe", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }
        Usuario usuario = usuarioOptional.get();
        //Validar la contraseña actual
        if (!passwordEncoder.matches(dto.getContrasena(), usuario.getContrasena())) {
            return new ResponseEntity<>(new Message("La contraseña actual es incorrecta", TypesResponse.ERROR), HttpStatus.BAD_REQUEST);
        }

        usuario.setContrasena(passwordEncoder.encode(dto.getContrasena()));
        //usuario.setContrasena(dto.getContrasena());
        usuario = usuarioRepository.saveAndFlush(usuario);
        if (usuario == null) {
            return new ResponseEntity<>(new Message("La contraseña del usuario no se actualizó", TypesResponse.ERROR), HttpStatus.BAD_REQUEST);
        }
        logger.info("La contraseña se actualizó correctamente");
        return new ResponseEntity<>(new Message(usuario, "Contraseña del usuario actualizada correctamente", TypesResponse.SUCCESS), HttpStatus.OK);
    }
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Message> cambiarContraGral(UsuarioDto dto){
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(dto.getId());
        if (!usuarioOptional.isPresent()) {
            return new ResponseEntity<>(new Message("El usuario no existe", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }
        Usuario usuario = usuarioOptional.get();
        usuario.setContrasena(passwordEncoder.encode(dto.getContrasena()));
        //usuario.setContrasena(dto.getContrasena());
        usuario = usuarioRepository.saveAndFlush(usuario);
        if (usuario == null) {
            return new ResponseEntity<>(new Message("La contraseña del usuario no se actualizó", TypesResponse.ERROR), HttpStatus.BAD_REQUEST);
        }
        logger.info("La contraseña se actualizó correctamente");
        return new ResponseEntity<>(new Message(usuario, "Contraseña del usuario actualizada correctamente", TypesResponse.SUCCESS), HttpStatus.OK);
    }
    @Transactional(readOnly = true)
    public ResponseEntity<Object> verifyCode(UsuarioDto dto) {
        Optional<Usuario> optional = usuarioRepository.findFirstByCorreoAndCodigo(dto.getCorreo(),dto.getCode());

        if(!optional.isPresent()){
            return new ResponseEntity<>(new Message("No se pudo verificar", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        logger.info("El código se ha verificado");
        return new ResponseEntity<>(new Message("Verificado", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    public ResponseEntity<Message> verifyPassword(UsuarioDto dto) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(dto.getId());
        if (!usuarioOptional.isPresent()) {
            return new ResponseEntity<>(new Message("El usuario no existe", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }
        Usuario usuario = usuarioOptional.get();
        //if ((dto.getContrasena() == usuario.getContrasena())){
        if (!passwordEncoder.matches(dto.getContrasena(), usuario.getContrasena())) {

            return new ResponseEntity<>(new Message("La contraseña actual es incorrecta", TypesResponse.ERROR), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new Message("La contraseña actual es correcta", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Object> sendEmail(UsuarioDto dto) {
        Optional<Usuario> optional = usuarioRepository.findFirstByCorreo(dto.getCorreo());
        if (!optional.isPresent()) {
            return new ResponseEntity<>(new Message("Usuario no encontrado", TypesResponse.WARNING), HttpStatus.NOT_FOUND);
        }

        Random random = new Random();
        StringBuilder numberString = new StringBuilder();

        for (int i = 0; i < 5; i++) {
            int digit = random.nextInt(10);
            numberString.append(digit);
        }

        Usuario user = optional.get();
        user.setCodigo(numberString.toString());
        user = usuarioRepository.saveAndFlush(user);
        if (user == null) {
            return new ResponseEntity<>(new Message("Código no registrado", TypesResponse.ERROR), HttpStatus.BAD_REQUEST);
        }

        emailSender.sendEmail(user.getCorreo(),
                "AssetTracker | Solicitud de restablecimiento de contraseña",
                "<!DOCTYPE html>\n" +
                        "<html xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\" lang=\"es\">\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\n" +
                        "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                        "    <title>Restablecer contraseña</title>\n" +
                        "    <style>\n" +
                        "        body {\n" +
                        "            margin: 0;\n" +
                        "            padding: 0;\n" +
                        "            font-family: 'Arial', sans-serif;\n" +
                        "            line-height: 1.6;\n" +
                        "            color: #333333;\n" +
                        "            background-color: #f5f7fa;\n" +
                        "        }\n" +
                        "        .container {\n" +
                        "            max-width: 600px;\n" +
                        "            margin: 0 auto;\n" +
                        "            background: #ffffff;\n" +
                        "            border-radius: 8px;\n" +
                        "            overflow: hidden;\n" +
                        "            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);\n" +
                        "        }\n" +
                        "        .header {\n" +
                        "            background-color: #133E87;\n" +
                        "            padding: 30px 20px;\n" +
                        "            text-align: center;\n" +
                        "        }\n" +
                        "        .header h1 {\n" +
                        "            color: #ffffff;\n" +
                        "            margin: 0;\n" +
                        "            font-size: 24px;\n" +
                        "            font-weight: 600;\n" +
                        "        }\n" +
                        "        .content {\n" +
                        "            padding: 30px;\n" +
                        "        }\n" +
                        "        .code-container {\n" +
                        "            background-color: #f0f4f9;\n" +
                        "            border-radius: 6px;\n" +
                        "            padding: 20px;\n" +
                        "            margin: 25px 0;\n" +
                        "            text-align: center;\n" +
                        "        }\n" +
                        "        .verification-code {\n" +
                        "            font-size: 32px;\n" +
                        "            font-weight: bold;\n" +
                        "            color: #133E87;\n" +
                        "            letter-spacing: 3px;\n" +
                        "        }\n" +
                        "        .footer {\n" +
                        "            text-align: center;\n" +
                        "            padding: 20px;\n" +
                        "            font-size: 12px;\n" +
                        "            color: #777777;\n" +
                        "            background-color: #f5f7fa;\n" +
                        "        }\n" +
                        "        .button {\n" +
                        "            display: inline-block;\n" +
                        "            padding: 12px 24px;\n" +
                        "            background-color: #133E87;\n" +
                        "            color: #ffffff !important;\n" +
                        "            text-decoration: none;\n" +
                        "            border-radius: 4px;\n" +
                        "            font-weight: 600;\n" +
                        "            margin: 15px 0;\n" +
                        "        }\n" +
                        "        .icon {\n" +
                        "            width: 80px;\n" +
                        "            height: 80px;\n" +
                        "            margin-bottom: 20px;\n" +
                        "        }\n" +
                        "        @media only screen and (max-width: 600px) {\n" +
                        "            .container {\n" +
                        "                width: 100%;\n" +
                        "                border-radius: 0;\n" +
                        "            }\n" +
                        "            .content {\n" +
                        "                padding: 20px;\n" +
                        "            }\n" +
                        "        }\n" +
                        "    </style>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "    <div class=\"container\">\n" +
                        "        <div class=\"header\">\n" +
                        "            <h1>Restablecimiento de contraseña</h1>\n" +
                        "        </div>\n" +
                        "        <div class=\"content\">\n" +
                        "            <div style=\"text-align: center;\">\n" +
                        "                <svg class=\"icon\" xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 24 24\" fill=\"#133E87\">\n" +
                        "                    <path d=\"M12 1C8.14 1 5 4.14 5 8v1H4c-1.1 0-2 .9-2 2v10c0 1.1.9 2 2 2h16c1.1 0 2-.9 2-2V11c0-1.1-.9-2-2-2h-1V8c0-3.86-3.14-7-7-7zm0 2c2.76 0 5 2.24 5 5v1H7V8c0-2.76 2.24-5 5-5zm6 8v10H6V11h12zm-6 3c1.1 0 2-.9 2-2s-.9-2-2-2-2 .9-2 2 .9 2 2 2z\"/>\n" +
                        "                </svg>\n" +
                        "            </div>\n" +
                        "            <p style=\"text-align: center; color: #555555;\">Hemos recibido una solicitud para restablecer la contraseña de tu cuenta. Utiliza el siguiente código de verificación:</p>\n" +
                        "            \n" +
                        "            <div class=\"code-container\">\n" +
                        "                <div class=\"verification-code\">" + user.getCodigo() + "</div>\n" +
                        "            </div>\n" +
                        "            \n" +
                        "            <p style=\"text-align: center; color: #555555;\">Si no has solicitado este cambio, por favor ignora este mensaje.</p>\n" +
                        "        </div>\n" +
                        "        <div class=\"footer\">\n" +
                        "            <p>Sistema AssetTracker UTEZ ©</p>\n" +
                        "            <p>Este es un mensaje automático, por favor no respondas a este correo.</p>\n" +
                        "        </div>\n" +
                        "    </div>\n" +
                        "</body>\n" +
                        "</html>");

        return new ResponseEntity<>(new Message("Correo enviado", TypesResponse.SUCCESS), HttpStatus.OK);
    }
}
