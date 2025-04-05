package com.integradora.AssetTrackerUtez.inventarioLevantado.control;

import com.integradora.AssetTrackerUtez.edificio.model.EdificioDTO;
import com.integradora.AssetTrackerUtez.espacio.control.EspacioService;
import com.integradora.AssetTrackerUtez.inventarioLevantado.model.InventarioLevantadoDTO;
import com.integradora.AssetTrackerUtez.utils.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/inventariosLevantados")
//@CrossOrigin(origins = {"*"},methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class InventarioLevantadoController {
    private final InventarioLevantadoService inventarioLevantadoService;
    @Autowired
    public InventarioLevantadoController( InventarioLevantadoService inventarioLevantadoService){
        this.inventarioLevantadoService = inventarioLevantadoService;
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findAll(){
        return inventarioLevantadoService.findAll();
    }
    @GetMapping("/all/enable")
    public ResponseEntity<Object> enable(){
        return inventarioLevantadoService.anable();
    }
    @GetMapping("/all/disable")
    public ResponseEntity<Object> disable(){
        return inventarioLevantadoService.disable();
    }
    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable Long id) {
        return inventarioLevantadoService.findById(id);
    }

    @PostMapping("/save")
    public  ResponseEntity<Object> save(@Validated(InventarioLevantadoDTO.Register.class) @RequestBody InventarioLevantadoDTO dto){
        return  inventarioLevantadoService.save(dto);
    }
    @PutMapping("/status")
    public ResponseEntity<Object> ChangeStatus(@Validated(InventarioLevantadoDTO.ChangeStatus.class) @RequestBody InventarioLevantadoDTO dto){
        return inventarioLevantadoService.changeStatus(dto);
    }
    @PutMapping("/update")
    public ResponseEntity<Object> update(@ModelAttribute InventarioLevantadoDTO dto, @RequestParam("file")MultipartFile file){
        return  inventarioLevantadoService.updateInventario(dto, file);
    }
    @GetMapping("/espacio/{idInventario}")
    public ResponseEntity<Object> getEspaciosPorEdificio(@PathVariable Long idInventario) {
        return inventarioLevantadoService.findByEspacioId(idInventario);
    }

    @GetMapping("/count")
    public long getNumeroDeEdificios() {
        return inventarioLevantadoService.contarInventarios();
    }
    @PostMapping("/duplicate")
    public ResponseEntity<Message> duplicateLast() {
        return inventarioLevantadoService.duplicateLast();
    }



}
