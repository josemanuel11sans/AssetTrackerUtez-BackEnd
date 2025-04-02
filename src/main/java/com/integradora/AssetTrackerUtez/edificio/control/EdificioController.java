package com.integradora.AssetTrackerUtez.edificio.control;

import com.integradora.AssetTrackerUtez.edificio.model.EdificioDTO;
import com.integradora.AssetTrackerUtez.espacio.model.EspaciosDTO;
import com.integradora.AssetTrackerUtez.utils.Message;
import com.integradora.AssetTrackerUtez.utils.TypesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/edificios")
//@CrossOrigin(origins = {"*"},methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class EdificioController {
    private final EdificioService edificioService;
    @Autowired
    public EdificioController(EdificioService edificioService) {
        this.edificioService = edificioService;
    }
    //Guardar un edificio
    @PostMapping("/save")
    public ResponseEntity<Object> save(@ModelAttribute @Validated(EdificioDTO.Register.class) EdificioDTO dto,  @RequestParam("file") MultipartFile file){
        return edificioService.GuardarEdificio(dto, file);
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
    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable int id) {
        return edificioService.findById(id);
    }
    //Actualizar un edificio
    @PutMapping("/update")
    public ResponseEntity<Object> ActualizarEdificio( @ModelAttribute @Validated(EdificioDTO.Modify.class)EdificioDTO dto,@RequestParam(value = "file", required = false) MultipartFile file, BindingResult bindingResult){
        // Si hay errores de validación, devolverlos
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(new Message(null, "Errores de validación", TypesResponse.ERROR), HttpStatus.BAD_REQUEST);
        }
        return edificioService.actualizarEdificio(dto, file);
    }

    @GetMapping("/count")
    public long getNumeroDeEdificios() {
        return edificioService.contarEdificios();
    }
}
