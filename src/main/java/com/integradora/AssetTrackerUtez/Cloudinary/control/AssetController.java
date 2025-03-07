package com.integradora.AssetTrackerUtez.Cloudinary.control;



import com.integradora.AssetTrackerUtez.Cloudinary.model.Asset;
import com.integradora.AssetTrackerUtez.Cloudinary.control.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/assets")
public class AssetController {

    @Autowired
    private AssetService assetService;

    // Crear un nuevo Asset
    /*
    @PostMapping
    public Asset createAsset(
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam("file") MultipartFile file) {
        return assetService.createAsset(name, description, file);
    }
*/
    // Obtener todos los Assets
    @GetMapping
    public List<Asset> getAllAssets() {
        return assetService.getAllAssets();
    }

    // Obtener un Asset por ID
    @GetMapping("/{id}")
    public Optional<Asset> getAssetById(@PathVariable Long id) {
        return assetService.getAssetById(id);
    }
    /*
    // Actualizar un Asset
    @PutMapping("/{id}")
    public Asset updateAsset(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        return assetService.updateAsset(id, name, description, file);
    }
    */
    // Eliminar un Asset
    @DeleteMapping("/{id}")
    public void deleteAsset(@PathVariable Long id) {
        assetService.deleteAsset(id);
    }
}