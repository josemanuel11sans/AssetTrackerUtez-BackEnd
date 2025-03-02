package com.integradora.AssetTrackerUtez.notificacionRegistro.control;

import com.integradora.AssetTrackerUtez.notificacionRegistro.model.EstadoNotificacion;
import com.integradora.AssetTrackerUtez.notificacionRegistro.model.NotificacionRegistroDto;
import com.integradora.AssetTrackerUtez.utils.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notificaciones")
@CrossOrigin(origins = {"*"}, methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT})
public class NotificacionRegistroController {
    private final NotificacionRegistroService notificacionRegistroService;

    @Autowired
    public NotificacionRegistroController(NotificacionRegistroService notificacionRegistroService) {
        this.notificacionRegistroService = notificacionRegistroService;
    }

    @GetMapping("/all")
    public ResponseEntity<Message> findAll() {
        return notificacionRegistroService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Message> findById(@PathVariable Long id) {
        return notificacionRegistroService.findById(id);
    }

    @GetMapping("/pendientes")
    public ResponseEntity<Message> findPendientes() {
        return notificacionRegistroService.findPendientes();
    }

    @PostMapping("/crear")
    public ResponseEntity<Message> crearNotificacion(@RequestBody NotificacionRegistroDto dto) {
        return notificacionRegistroService.crearNotificacion(dto);
    }

    @PutMapping("/actualizarEstado/{id}")
    public ResponseEntity<Message> actualizarEstado(@PathVariable Long id, @RequestParam EstadoNotificacion estado, @RequestParam String comentarios, @RequestParam Long administradorId) {
        return notificacionRegistroService.actualizarEstado(id, estado, comentarios, administradorId);
    }

    @PutMapping("/aprobar/{notificacionId}")
    public ResponseEntity<Message> aprobarUsuario(@PathVariable Long notificacionId, @RequestParam Long administradorId) {
        return notificacionRegistroService.aprobarUsuario(notificacionId, administradorId);
    }

    @PutMapping("/rechazar/{notificacionId}")
    public ResponseEntity<Message> rechazarUsuario(@PathVariable Long notificacionId, @RequestParam Long administradorId, @RequestParam String motivo) {
        return notificacionRegistroService.rechazarUsuario(notificacionId, administradorId, motivo);
    }
}
