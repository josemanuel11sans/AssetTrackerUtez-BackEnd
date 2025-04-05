package com.integradora.AssetTrackerUtez.recurso.control;

import com.integradora.AssetTrackerUtez.inventarioLevantado.model.InventarioLevantadoDTO;
import com.integradora.AssetTrackerUtez.recurso.model.RecursosDTO;
import com.integradora.AssetTrackerUtez.utils.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/recursos")
//@CrossOrigin(origins = {"*"},methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
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
    @GetMapping("/{codigo}")
    public ResponseEntity<Message> findByCodigo( @PathVariable String codigo){
        return recursosService.findByCodigo(codigo);
    }

    @PostMapping("/save")
    public  ResponseEntity<Object> save(@Validated(RecursosDTO.Register.class) @RequestBody RecursosDTO dto){
        return  recursosService.save(dto);
    }

    @PutMapping("/update")
    public  ResponseEntity<Object> update(@Validated(RecursosDTO.Register.class) @RequestBody RecursosDTO dto){
        return  recursosService.update(dto);
    }
    @GetMapping("/count")
    public long getNumeroDeEdificios() {
        return recursosService.contarRecursos();
    }

    @GetMapping("/inventario/{idInventario}")
    public ResponseEntity<Object> getEspaciosPorEdificio(@PathVariable Long idInventario) {
        return recursosService.findByEspacioId(idInventario);
    }
    @GetMapping("/porcentaje-categorias")
    public ResponseEntity<Object> getPorcentajesPorCategoria() {
        return recursosService.getPorcentajeRecursosPorCategoria();
    }

}
