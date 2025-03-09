package com.integradora.AssetTrackerUtez.inventarioLevantado.control;

import com.integradora.AssetTrackerUtez.edificio.model.EdificioDTO;
import com.integradora.AssetTrackerUtez.espacio.control.EspacioService;
import com.integradora.AssetTrackerUtez.inventarioLevantado.model.InventarioLevantadoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventariosLevantados")
@CrossOrigin(origins = {"*"},methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
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

    @PostMapping("/save")
    public  ResponseEntity<Object> save(@Validated(InventarioLevantadoDTO.Register.class) @RequestBody InventarioLevantadoDTO dto){
        return  inventarioLevantadoService.save(dto);
    }
    @PutMapping("/status")
    public ResponseEntity<Object> ChangeStatus(@Validated(InventarioLevantadoDTO.ChangeStatus.class) @RequestBody InventarioLevantadoDTO dto){
        return inventarioLevantadoService.changeStatus(dto);
    }




}
