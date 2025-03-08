package com.integradora.AssetTrackerUtez.categoriaRecurso.control;

import com.integradora.AssetTrackerUtez.categoriaRecurso.model.CategoriaRecursoDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/categoriasRecursos")
@CrossOrigin(origins = {"*"},methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class CategoriaRecursoController {
    private final CategoriaRecursoService categoriaRecursoService;

    public CategoriaRecursoController(CategoriaRecursoService categoriaRecursoService) {
        this.categoriaRecursoService = categoriaRecursoService;
    }

    @GetMapping("/all/enable")
    public ResponseEntity<Object> findAllEnable(){
        return categoriaRecursoService.findAllEnable();
    }

    @GetMapping("/all/disable")
    public ResponseEntity<Object> findAllDisable(){
        return categoriaRecursoService.findAllDisable();
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findAll(){
        return categoriaRecursoService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable int id) {
        return categoriaRecursoService.findById(id);
    }

    @PostMapping("/save")
    public ResponseEntity<Object> save(@ModelAttribute CategoriaRecursoDTO dto, @RequestParam("file") MultipartFile file){
        return categoriaRecursoService.save(dto,file);
    }

    @PutMapping("/status")
    public ResponseEntity<Object> changeStatus(@Validated(CategoriaRecursoDTO.ChangeStatus.class) @RequestBody CategoriaRecursoDTO dto){
        return categoriaRecursoService.changeStatus(dto);
    }

}
