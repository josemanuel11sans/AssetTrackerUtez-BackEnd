package com.integradora.AssetTrackerUtez.espacio.control;

import com.integradora.AssetTrackerUtez.espacio.model.EspaciosDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/enable")
    public ResponseEntity<Object> findAllEnable(){
        return espacioService.findAllEnable();
    }
    @GetMapping("/disable")
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



}
