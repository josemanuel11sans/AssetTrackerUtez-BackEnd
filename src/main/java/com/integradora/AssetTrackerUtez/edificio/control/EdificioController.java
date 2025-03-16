package com.integradora.AssetTrackerUtez.edificio.control;

import com.integradora.AssetTrackerUtez.edificio.model.EdificioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/edificios")
@CrossOrigin(origins = {"*"},methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class EdificioController {
    private final EdificioService edificioService;
    @Autowired
    public EdificioController(EdificioService edificioService) {
        this.edificioService = edificioService;
    }
    //Guardar un edificio
    @PostMapping("/save")
    public ResponseEntity<Object> save(@Validated(EdificioDTO.Register.class) @RequestBody EdificioDTO dto){
        return edificioService.GuardarEdificio(dto);
    }
    //Listar todos los edificios
    @GetMapping("/all")
    public ResponseEntity<Object> ListarEdificios(){
        return edificioService.findAll();
    }
    //Listar solo los edificios activos
    @GetMapping("/all/enable")
    public ResponseEntity<Object> ListarEdificiosActivos(){
        return edificioService.findAllEnable();
    }
    //Listar solo los edificios inactivos
    @GetMapping("/all/disable")
    public ResponseEntity<Object> ListarEdificiosInactivos(){
        return edificioService.findAllDisable();
    }
    //Cambiar estado de un edificio
    @PutMapping("/status")
    public ResponseEntity<Object> CambiarEstado(@Validated(EdificioDTO.ChangeStatus.class) @RequestBody EdificioDTO dto){
        return edificioService.changeStatus(dto);
    }
    //Actualizar un edificio
    @PutMapping("/update")
    public ResponseEntity<Object> ActualizarEdificio(@Validated(EdificioDTO.Modify.class) @RequestBody EdificioDTO dto){
        return edificioService.actualizarEdificio(dto);
    }
}
