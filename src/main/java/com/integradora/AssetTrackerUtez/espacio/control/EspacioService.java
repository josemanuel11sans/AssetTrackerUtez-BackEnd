package com.integradora.AssetTrackerUtez.espacio.control;

import com.integradora.AssetTrackerUtez.Cloudinary.control.CloudinaryService;
import com.integradora.AssetTrackerUtez.espacio.model.Espacio;
import com.integradora.AssetTrackerUtez.espacio.model.EspacioRepository;
import com.integradora.AssetTrackerUtez.espacio.model.EspaciosDTO;
import com.integradora.AssetTrackerUtez.utils.Message;
import com.integradora.AssetTrackerUtez.utils.TypesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.sql.SQLException;
import java.util.Optional;

@Service
@Transactional
public class EspacioService {
    @Autowired
    private CloudinaryService cloudinaryService;

    private final EspacioRepository espacioRepository;
    @Autowired
    public EspacioService(EspacioRepository espacioRepository) {
        this.espacioRepository = espacioRepository;
    }
    //Métodos
    //Método para buscar todos los espacios habilitados
    @Transactional(readOnly = true)
    public ResponseEntity<Object> findAllEnable() {
        return new ResponseEntity<>(new Message(espacioRepository.findAllByStatusOrderByNombre(true), "Listado de edificios", TypesResponse.SUCCESS), HttpStatus.OK);
    }
    //Método para buscar todos los espacios deshabilitados
    @Transactional(readOnly = true)
    public ResponseEntity<Object> findAllDisable() {
        return new ResponseEntity<>(new Message(espacioRepository.findAllByStatusOrderByNombre(false), "Listado de edificios", TypesResponse.SUCCESS), HttpStatus.OK);
    }
    //Método para buscar todos los espacios
    @Transactional(readOnly = true)
    public ResponseEntity<Object> findAll() {
        return new ResponseEntity<>(new Message(espacioRepository.findAll(), "Listado de edificios", TypesResponse.SUCCESS), HttpStatus.OK);
    }
    //Método para buscar un espacio por id
    @Transactional(readOnly = true)
    public ResponseEntity<Object> findById(int id) {
        return new ResponseEntity<>(new Message(espacioRepository.findById((long) id), "Edificio encontrado", TypesResponse.SUCCESS), HttpStatus.OK);
    }
    @Transactional(rollbackFor ={SQLException.class})
    public ResponseEntity<Object> save(EspaciosDTO dto, MultipartFile file) {

        //validaciones
        //valida que el nombre no exista
        if(espacioRepository.existsByNombre(dto.getNombre())){
            return new ResponseEntity<>(new Message(null, "El nombre del espacio ya existe", TypesResponse.ERROR), HttpStatus.OK);
        }
        //valida que el nombre no sea tan grande
        if(dto.getNombre().length() > 50){
            return new ResponseEntity<>(new Message(null, "El nombre del espacio no puede ser tan grande", TypesResponse.ERROR), HttpStatus.OK);
        }
        //valida que el nombre no sea tan pequeño
        if(dto.getNombre().length() < 3){
            return new ResponseEntity<>(new Message(null, "El nombre del espacio tiene que ser mayor a 3 caracteres", TypesResponse.ERROR), HttpStatus.OK);
        }
        //validacion de caracteres no permitidos
        if(!dto.getNombre().matches("^[a-zA-Z0-9 ]*$")){
            return new ResponseEntity<>(new Message("El nombre solo puede contener letras y numeros", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        //valida que el numero de planta sea mayor a 0
        if(dto.getNumeroPlanta() < 1){
            return new ResponseEntity<>(new Message(null, "El número de planta debe ser mayor a 0", TypesResponse.ERROR), HttpStatus.OK);
        }
        //valida que la imagen no sea nula
        if(file == null){
            return new ResponseEntity<>(new Message(null, "La imagen es requerida", TypesResponse.ERROR), HttpStatus.OK);
        }
        //valida que la imagen sea una imagen
        if (!file.getContentType().startsWith("image/")) {
            return new ResponseEntity<>(new Message(null, "El archivo debe ser una imagen (JPG, PNG, etc.)", TypesResponse.ERROR), HttpStatus.BAD_REQUEST);
        }

        //save
        String imageUrl = cloudinaryService.uploadFile(file);
        //pone la primera letra en mayuscula
        dto.setNombre(capitalizarPrimeraLetra(dto.getNombre()));
        //quita los espacios al inicio y al final
        dto.setNombre(dto.getNombre().trim());
        Espacio espacio = new Espacio(dto.getNombre(), dto.getNumeroPlanta(), imageUrl, true);
        espacio = espacioRepository.saveAndFlush(espacio);
        if(espacio == null){
            return new ResponseEntity<>(new Message(espacio, "Error al regitrar el espacio", TypesResponse.SUCCESS), HttpStatus.OK);
        }
        return new ResponseEntity<>(new Message(espacio, "Espacio registrado", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Object> update( EspaciosDTO dto, MultipartFile file) {

        // Buscar el espacio en la base de datos
        Optional<Espacio> optionalEspacio = espacioRepository.findById((long) dto.getId());
        if (optionalEspacio.isEmpty()) {
            return new ResponseEntity<>(new Message(null, "El espacio no existe", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }

        Espacio espacio = optionalEspacio.get();

        // Validar si el nuevo nombre ya existe en otro espacio
        if (espacioRepository.existsByNombre(dto.getNombre()) && !espacio.getNombre().equalsIgnoreCase(dto.getNombre())) {
            return new ResponseEntity<>(new Message(null, "El nombre del espacio ya está en uso", TypesResponse.ERROR), HttpStatus.CONFLICT);
        }

        // Validaciones del nombre
        if (dto.getNombre().length() > 50 || dto.getNombre().length() < 3) {
            return new ResponseEntity<>(new Message(null, "El nombre debe tener entre 3 y 50 caracteres", TypesResponse.ERROR), HttpStatus.BAD_REQUEST);
        }
        if (!dto.getNombre().matches("^[a-zA-Z0-9 ]*$")) {
            return new ResponseEntity<>(new Message(null, "El nombre solo puede contener letras y números", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        // Validación del número de planta
        if (dto.getNumeroPlanta() < 1) {
            return new ResponseEntity<>(new Message(null, "El número de planta debe ser mayor a 0", TypesResponse.ERROR), HttpStatus.BAD_REQUEST);
        }
        // Si hay un nuevo archivo, validamos y subimos la nueva imagen
        String imageUrl = espacio.getUrlImagen(); // Mantener la imagen existente
        if (file != null && !file.isEmpty()) {
            if (!file.getContentType().startsWith("image/")) {
                return new ResponseEntity<>(new Message(null, "El archivo debe ser una imagen (JPG, PNG, etc.)", TypesResponse.ERROR), HttpStatus.BAD_REQUEST);
            }
            imageUrl = cloudinaryService.uploadFile(file); // Subir nueva imagen
        }
        // Quitar espacios al inicio y al final, y capitalizar el nombre
        dto.setNombre(capitalizarPrimeraLetra(dto.getNombre().trim()));
        // Actualizar los datos del espacio
        espacio.setNombre(dto.getNombre());
        espacio.setNumeroPlanta(dto.getNumeroPlanta());
        espacio.setUrlImagen(imageUrl); // Actualizar imagen solo si se proporcionó una nueva

        // Guardar cambios
        espacio = espacioRepository.saveAndFlush(espacio);

        return new ResponseEntity<>(new Message(espacio, "Espacio actualizado correctamente", TypesResponse.SUCCESS), HttpStatus.OK);
    }




    //funcion para capitalizar la primera letra de un texto
    public static String capitalizarPrimeraLetra(String texto) {
        texto = texto.toLowerCase();
        return texto.substring(0, 1).toUpperCase() + texto.substring(1);
    }
}
