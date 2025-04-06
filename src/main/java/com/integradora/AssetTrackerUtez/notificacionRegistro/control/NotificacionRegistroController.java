package com.integradora.AssetTrackerUtez.notificacionRegistro.control;

import com.integradora.AssetTrackerUtez.notificacionRegistro.model.EstadoNotificacion;
import com.integradora.AssetTrackerUtez.notificacionRegistro.model.NotificacionRegistroDto;
import com.integradora.AssetTrackerUtez.utils.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notificaciones")
//@CrossOrigin(origins = {"*"}, methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT})
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

    @PutMapping("/actualizarEstado")
    //public ResponseEntity<Message> actualizarEstado(@PathVariable Long id, @RequestParam EstadoNotificacion estado, @RequestParam String comentarios, @RequestParam Long administradorId) {
    public ResponseEntity<Message> actualizarEstado(@RequestBody NotificacionRegistroDto dto) {
        return notificacionRegistroService.actualizarEstado(dto);
    }

    @PutMapping("/aprobar")
    //public ResponseEntity<Message> aprobarUsuario(@PathVariable Long notificacionId, @RequestParam Long administradorId) {
    public ResponseEntity<Message> aprobarUsuario(@RequestBody NotificacionRegistroDto dto) {
        return notificacionRegistroService.aprobarUsuario(dto);
    }

    @PutMapping("/rechazar")
    public ResponseEntity<Message> rechazarUsuario(@RequestBody NotificacionRegistroDto dto) {
        return notificacionRegistroService.rechazarUsuario(dto);
    }
}
