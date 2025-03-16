package com.integradora.AssetTrackerUtez.responsable.control;

import com.integradora.AssetTrackerUtez.responsable.model.ResponsableDTO;
import com.integradora.AssetTrackerUtez.utils.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/responsables")
@CrossOrigin(origins = {"*"},methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class ResponsableController {
    private final ResponsableService responsableService;
    @Autowired
    public ResponsableController(ResponsableService responsableService) {
        this.responsableService = responsableService;
    }
    @GetMapping("/all")
    public ResponseEntity<Message> findAll() {
        return responsableService.findAll();
    }
    @GetMapping("/{id}")
    public ResponseEntity<Message> findById(@PathVariable Long id) {
        return responsableService.findById(id);
    }
    @GetMapping("/actives")
    public ResponseEntity<Message> findActives() {
        return responsableService.findActives();
    }
    @PostMapping("/save")
    public ResponseEntity<Message> save(@RequestBody ResponsableDTO responsableDTO) {
        return responsableService.save(responsableDTO);
    }
    @PutMapping("/update")
    public ResponseEntity<Message> update(@RequestBody ResponsableDTO responsableDTO) {
        return responsableService.update(responsableDTO);
    }
    @PutMapping("/changeStatus")
    public ResponseEntity<Message> changeStatus(@RequestBody ResponsableDTO responsableDTO) {
        return responsableService.changeStatus(responsableDTO);
    }
}
