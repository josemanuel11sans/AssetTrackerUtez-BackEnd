package com.integradora.AssetTrackerUtez.espacio.control;

import com.integradora.AssetTrackerUtez.espacio.model.EspaciosDTO;
import com.integradora.AssetTrackerUtez.utils.Message;
import com.integradora.AssetTrackerUtez.utils.TypesResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/espacios")
@CrossOrigin(origins = {"*"},methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class EspacioController {
  private final EspacioService espacioService;
  @Autowired
    public EspacioController(EspacioService espacioService) {
        this.espacioService = espacioService;
    }

    @GetMapping("/all/enable")
    public ResponseEntity<Object> findAllEnable(){
        return espacioService.findAllEnable();
    }
    @GetMapping("/all/disable")
    public ResponseEntity<Object> findAllDisable(){
        return espacioService.findAllDisable();
    }
    @GetMapping("/all")
    public ResponseEntity<Object> findAll(){
        return espacioService.findAll();
    }
    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable int id) {
        return espacioService.findById(id);
    }
    //vambiar status
    @PutMapping("/changeStatus")
    public ResponseEntity<Object> cambierStatus(@Validated(EspaciosDTO.ChangeStatus.class) @RequestBody EspaciosDTO dto){
        return espacioService.changeStatus(dto);
    }
    //guardar datos
    /**
     * e a la pestaña "Body", selecciona "form-data" y agrega los siguientes campos:
     *Key	Value (Ejemplo)	Tipo
     * nombre	"Aula 101"	Text
     * numeroPlanta	1	Text
     * file	(Selecciona una imagen)	File
     */
    @PostMapping("/save")
    public ResponseEntity<Object> save(@ModelAttribute EspaciosDTO dto, @RequestParam("file") MultipartFile file){
        return espacioService.save(dto, file);
    }
    @PutMapping("/update")
    public ResponseEntity<Object> update(@Valid @ModelAttribute EspaciosDTO dto,
                                         @RequestParam(value = "file", required = false) MultipartFile file,
                                         BindingResult bindingResult) {
        // Si hay errores de validación, devolverlos
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(new Message(null, "Errores de validación", TypesResponse.ERROR), HttpStatus.BAD_REQUEST);
        }
        return espacioService.update(dto, file);
    }
}
