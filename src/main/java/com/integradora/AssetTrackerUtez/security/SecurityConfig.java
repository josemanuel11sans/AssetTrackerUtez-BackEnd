package com.integradora.AssetTrackerUtez.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class SecurityConfig {
    private final JwtRequestFilter jwtRequestFilter;
    @Autowired
    public SecurityConfig(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors() // Habilita CORS usando el CorsConfigurationSource definido
                .and()
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(//todos (faltan)
                                "/auth/login",
                                "/usuarios/save",
                               // "/usuarios/change-password",
                                "/usuarios/send-email"
                                //"/usuarios/verify-code",
                                //"/usuarios/change-password",
                                //"/usuarios/verify-password",
                                //"/usuarios/change-password-gral"
                        ).permitAll()
                        .requestMatchers( //admin e inspector
                                //usuarios
                                "auth/login",
                                "usuarios/{id}",
                                "usuarios/verify-code",
                                "usuarios/change-password",
                                "usuarios/verify-password",
                                "usuarios/change-password",
                                "usuarios/change-password-gral",
                                //responsables
                                "responsables/all",
                                "responsables/{id}",
                                "responsables/actives",
                                "responsables/save",
                                "responsables/update",
                                "responsables/changeStatus",
                                //recursos
                                "recursos/all",
                                "recursos/{codigo}",
                                "recursos/save",
                                "recursos/update",
                                "recursos/inventario/{idInventario}",
                                //inventario levantado
                                "inventariosLevantados/all",
                                "inventariosLevantados/all/enable",
                                "inventariosLevantados/all/disable",
                                "inventariosLevantados/{id}",
                                "inventariosLevantados/save",
                                "inventariosLevantados/status",
                                "inventariosLevantados/duplicate",
                                "inventariosLevantados/espacio/{idInventario}",
                                //espacios
                                "espacios/all/enable",
                                "espacios/all",
                                "espacios/{id}",
                                //edificios
                                "edificios/all",
                                "edificios/all/enable",
                                "edificios/all/disable",
                                "edificios/{id}",
                                //categoria de recursos
                                "categoriasRecursos/all",
                                "categoriasRecursos/all/enable",
                                "categoriasRecursos/all/disable",
                                "categoriasRecursos/{id}",
                                "categoriasRecursos/save",
                                "categoriasRecursos/update",
                                "categoriasRecursos/status",

                                //categoria espacios
                                "categoriasEspacios/all",
                                "categoriasEspacios/{id}",
                                "categoriasEspacios/actives"
                        ).hasAnyAuthority("ROLE_ADMIN_ACCESS","ROLE_INSPECTOR_ACCESS")
                        .requestMatchers( //admin
                                //usuarios
                                "auth/login",
                                "usuarios/all",
                                "usuarios/{id}",
                                "usuarios/update",
                                "usuarios/changeStatus",
                                "usuarios/actives",
                                "usuarios/delete",
                                "usuarios/verify-code",
                                "usuarios/change-password",
                                "usuarios/change-password-gral",
                                //responsables
                                "responsables/all",
                                "responsables/{id}",
                                "responsables/actives",
                                "responsables/save",
                                "responsables/update",
                                "responsables/changeStatus",
                                //recursos (falta)
                                "recursos/all",
                                "recursos/save",
                                "recursos/count",
                                "recursos/update",
                                "recursos/inventario/{idInventario}",
                                //notificaciones
                                "notificaciones/all","notificaciones/{id}",
                                "notificaciones/pendientes",
                                "notificaciones/crear",
                                "notificaciones/actualizarEstado",
                                "notificaciones/aprobar",
                                "notificaciones/rechazar",
                                //inventarios - levanados
                                "inventariosLevantados/all",
                                "inventariosLevantados/save",
                                "inventariosLevantados/all/enable", "inventariosLevantados/all/disable",
                                "inventariosLevantados/status",
                                "inventariosLevantados/update",
                                "inventariosLevantados/espacio/{idInventario}",
                                "inventariosLevantados/count",
                                "inventariosLevantados/duplicate",
                                //espacios
                                "espacios/all","espacios/{id}",
                                "espacios/all/enable", "espacios/all/disable",
                                "espacios/changeStatus","espacios/update",
                                "espacios/save",
                                //edificios
                                "edificios/all",
                                "edificios/save",
                                "edificios/all/enable", "edificios/all/disable",
                                "edificios/status","edificios/update","edificios/count",
                                //categoria de recursos
                                "categoriasRecursos/all",
                                "categoriasRecursos/all/enable","categoriasRecursos/all/disable",
                                "categoriasRecursos/{id}",
                                "categoriasRecursos/save",
                                "categoriasRecursos/status",
                                "categoriasRecursos/porcentaje-categorias",
                                "categoriasRecursos/update",
                                //categorias de espacios
                                "categoriasRecursos/all", "categoriasEspacios/{id}",
                                "categoriasEspacios/actives", "categoriasEspacios/save",
                                "categoriasEspacios/update", "categoriasEspacios/changeStatus"

                        ).hasAnyAuthority("ROLE_ADMIN_ACCESS")

                        .anyRequest().authenticated()

                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Especifica el origen permitido explícitamente, en lugar de "*"
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setExposedHeaders(Arrays.asList("Authorization")); // Permite exponer ciertos headers
        configuration.setAllowCredentials(false); // Permite el envío de credenciales como cookies

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
