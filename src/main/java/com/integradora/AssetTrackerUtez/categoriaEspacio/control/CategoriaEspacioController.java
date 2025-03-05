package com.integradora.AssetTrackerUtez.categoriaEspacio.control;

import com.integradora.AssetTrackerUtez.categoriaEspacio.model.CategoriaEspacioDto;
import com.integradora.AssetTrackerUtez.utils.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categoriasEspacios")
@CrossOrigin(origins = {"*"},methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class CategoriaEspacioController {
    private final CategoriaEspacioService categoriaEspacioService;

    @Autowired
    public CategoriaEspacioController(CategoriaEspacioService categoriaEspacioService) {
        this.categoriaEspacioService = categoriaEspacioService;
    }

    @GetMapping("/all")
    public ResponseEntity<Message> findAll() {
        return categoriaEspacioService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Message> findById(@PathVariable Long id) {
        return categoriaEspacioService.findById(id);
    }

    @GetMapping("/actives")
    public ResponseEntity<Message> findActives() {
        return categoriaEspacioService.findActives();
    }

    @PostMapping("/save")
    public ResponseEntity<Message> save(@RequestBody CategoriaEspacioDto categoriaEspacioDto) {
        return categoriaEspacioService.save(categoriaEspacioDto);
    }

    @PutMapping("/update")
    public ResponseEntity<Message> update(@RequestBody CategoriaEspacioDto categoriaEspacioDto) {
        return categoriaEspacioService.update(categoriaEspacioDto);
    }

    @PutMapping("/changeStatus")
    public ResponseEntity<Message> changeStatus(@RequestBody CategoriaEspacioDto categoriaEspacioDto) {
        return categoriaEspacioService.changeStatus(categoriaEspacioDto);
    }
}
