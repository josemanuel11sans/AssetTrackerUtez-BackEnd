package com.integradora.AssetTrackerUtez.Cloudinary.control;


import com.integradora.AssetTrackerUtez.Cloudinary.model.Asset;
import com.integradora.AssetTrackerUtez.Cloudinary.model.AssetRepository;
import com.integradora.AssetTrackerUtez.Cloudinary.control.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class AssetService {

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    // Crear un nuevo Asset
    public Asset createAsset(String name, String description, MultipartFile file) {
        String imageUrl = cloudinaryService.uploadFile(file);
        Asset asset = new Asset();
        asset.setName(name);
        asset.setDescription(description);
        asset.setImageUrl(imageUrl);
        return assetRepository.save(asset);
    }

    // Obtener todos los Assets
    public List<Asset> getAllAssets() {
        return assetRepository.findAll();
    }

    // Obtener un Asset por ID
    public Optional<Asset> getAssetById(Long id) {
        return assetRepository.findById(id);
    }

    // Actualizar un Asset
    public Asset updateAsset(Long id, String name, String description, MultipartFile file) {
        Optional<Asset> optionalAsset = assetRepository.findById(id);
        if (optionalAsset.isPresent()) {
            Asset asset = optionalAsset.get();
            asset.setName(name);
            asset.setDescription(description);
            if (file != null && !file.isEmpty()) {
                String imageUrl = cloudinaryService.uploadFile(file);
                asset.setImageUrl(imageUrl);
            }
            return assetRepository.save(asset);
        }
        return null;
    }

    // Eliminar un Asset
    public void deleteAsset(Long id) {
        assetRepository.deleteById(id);
    }
}