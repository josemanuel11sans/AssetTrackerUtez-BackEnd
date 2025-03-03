package com.integradora.AssetTrackerUtez.Cloudinary.control;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;
    // Método para subir un archivo a Cloudinary
    public String uploadFile(MultipartFile file) {
        try {
            Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            return uploadResult.get("url").toString();
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
    // Método para obtener la URL de un archivo de Cloudinary con un tamaño específico


}