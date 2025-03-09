package com.integradora.AssetTrackerUtez.recurso.control;

import com.integradora.AssetTrackerUtez.inventarioLevantado.model.InventarioLevantadoDTO;
import com.integradora.AssetTrackerUtez.recurso.model.RecursosDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recursos")
@CrossOrigin(origins = {"*"},methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class RecursosController {
    private  final  RecursosService recursosService;
    @Autowired
    public RecursosController (RecursosService recursosService){
        this.recursosService= recursosService;
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findAll(){
        return recursosService.finfAll();
    }
    @PostMapping("/save")
    public  ResponseEntity<Object> save(@Validated(RecursosDTO.Register.class) @RequestBody RecursosDTO dto){
        return  recursosService.save(dto);
    }
}
