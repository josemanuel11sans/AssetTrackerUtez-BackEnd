package com.integradora.AssetTrackerUtez.Cloudinary.control;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class CloudinaryService {
    @Autowired
    private Cloudinary cloudinary;
    // Método para subir un archivo a Cloudinary
    public Map<String, String> uploadFile(MultipartFile file) {
        try {
            // Subir archivo a Cloudinary
            Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            // Obtener la URL y el Public ID
            String url = uploadResult.get("url").toString();
            String publicId = uploadResult.get("public_id").toString();
            // Devolver ambos valores en un Map
            Map<String, String> response = new HashMap<>();
            response.put("url", url);
            response.put("public_id", publicId);
            return response;
        } catch (IOException e) {
            throw new RuntimeException("Error al subir el archivo a Cloudinary", e);
        }
    }
    // Método para eliminar un archivo de Cloudinary
    public void deleteFile(String publicId) {
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new RuntimeException("Error al eliminar el archivo de Cloudinary", e);
        }
    }
    // Método para obtener la URL de un archivo de Cloudinary
    public String getFileUrl(String publicId) {
        return cloudinary.url().generate(publicId);
    }
    //metodo para actualizar un archivo
    public Map<String, String> updateFile(MultipartFile file, String publicId) {
        try {
            Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap("public_id", publicId, "overwrite", true));

            String imageUrl = uploadResult.get("url").toString();
            String newPublicId = uploadResult.get("public_id").toString();

            Map<String, String> result = new HashMap<>();
            result.put("url", imageUrl);
            result.put("public_id", newPublicId);

            return result;
        } catch (IOException e) {
            throw new RuntimeException("Error al subir el archivo a Cloudinary", e);
        }
    }

}