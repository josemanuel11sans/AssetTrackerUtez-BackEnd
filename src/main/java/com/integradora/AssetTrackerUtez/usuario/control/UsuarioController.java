package com.integradora.AssetTrackerUtez.usuario.control;

import com.integradora.AssetTrackerUtez.usuario.model.UsuarioDto;
import com.integradora.AssetTrackerUtez.utils.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = {"*"},methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class UsuarioController {
    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/all")
    public ResponseEntity<Message> findAll() {
        return usuarioService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Message> findById(@PathVariable Long id) {
        return usuarioService.findById(id);
    }
    @PostMapping("/save")
    public ResponseEntity<Message> save(@RequestBody UsuarioDto usuarioDto) {
        return usuarioService.save(usuarioDto);
    }

    @PutMapping("/update")
    public ResponseEntity<Message> update(@RequestBody UsuarioDto usuarioDto) {
        return usuarioService.update(usuarioDto);
    }
    @PutMapping("/changeStatus")
    public ResponseEntity<Message> changeStatus(@RequestBody UsuarioDto usuarioDto) {
        return usuarioService.changeStatus(usuarioDto);
    }

    @GetMapping("/actives")
    public ResponseEntity<Message> findActives() {
        return usuarioService.findActives();
    }
    @PostMapping("/delete")
    public ResponseEntity<Message> delete(@RequestBody UsuarioDto usuarioDto){
        return usuarioService.delete(usuarioDto);
    }
}
