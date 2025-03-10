package com.integradora.AssetTrackerUtez.utils;

import com.integradora.AssetTrackerUtez.rol.model.Rol;
import com.integradora.AssetTrackerUtez.rol.model.RolRepository;
import com.integradora.AssetTrackerUtez.usuario.model.Usuario;
import com.integradora.AssetTrackerUtez.usuario.model.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UsuarioRepository usuarioRepository, RolRepository rolRepository, PasswordEncoder passwordEncoder) {
        return args -> {


            Optional<Rol> optionalRole = rolRepository.findByNombre("ROLE_ADMIN_ACCESS");
            if (!optionalRole.isPresent()) {
                Rol roleAdmin = new Rol("ROLE_ADMIN_ACCESS");
                rolRepository.saveAndFlush(roleAdmin);

                Optional<Usuario> optionalUser = usuarioRepository.findFirstByCorreo("20233tn083@utez.edu.mx");
                if (!optionalUser.isPresent()) {
                    //Usuario usuarioAdmin = new Usuario("20233tn083@utez.edu.mx", passwordEncoder.encode("12345"));
                    Usuario usuarioAdmin = new Usuario();
                    usuarioAdmin.setApellidos("Rodriguez");
                    usuarioAdmin.setNombre("Rocio");
                    usuarioAdmin.setCorreo("20233tn105@utez.edu.mx");
                    usuarioAdmin.setContrasena(passwordEncoder.encode("12345"));
                    usuarioAdmin.getRol().add(roleAdmin);
                    usuarioRepository.saveAndFlush(usuarioAdmin);
                }
            }

            optionalRole = rolRepository.findByNombre("ROLE_INSPECTOR_ACCESS");
            if (!optionalRole.isPresent()) {
                Rol roleGerente = new Rol("ROLE_INSPECTOR_ACCESS");
                rolRepository.saveAndFlush(roleGerente);

                Optional<Usuario> optionalUser = usuarioRepository.findFirstByCorreo("20233tn093@utez.edu.mx");
                if (!optionalUser.isPresent()) {
                   Usuario userInspector = new Usuario();
                    userInspector.setApellidos("Sanchez");
                    userInspector.setNombre("José");
                    userInspector.setCorreo("20233tn078@utez.edu.mx");
                    userInspector.setContrasena(passwordEncoder.encode("12345"));
                    userInspector.getRol().add(roleGerente);
                    usuarioRepository.saveAndFlush(userInspector);
                }
            }

        };
    }
}
