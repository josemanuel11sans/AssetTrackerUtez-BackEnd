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
import java.util.Map;
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
        // Subir imagen a Cloudinary y obtener URL y Public ID
        Map<String, String> uploadResult = cloudinaryService.uploadFile(file);
        String imageUrl = uploadResult.get("url");
        String publicId = uploadResult.get("public_id");
        // Formatear nombre
        dto.setNombre(capitalizarPrimeraLetra(dto.getNombre().trim()));
        // Crear y guardar el objeto Espacio con Public ID
        Espacio espacio = new Espacio(dto.getNombre(), dto.getNumeroPlanta(), imageUrl, publicId, true);
        espacio = espacioRepository.saveAndFlush(espacio);
        if(espacio == null){
            return new ResponseEntity<>(new Message(espacio, "Error al registrar el espacio", TypesResponse.ERROR), HttpStatus.OK);
        }
        return new ResponseEntity<>(new Message(espacio, "Espacio registrado", TypesResponse.SUCCESS), HttpStatus.OK);
    }


    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Object> update(EspaciosDTO dto, MultipartFile file) {
        // Buscar el espacio en la base de datos
        Optional<Espacio> optionalEspacio = espacioRepository.findById((long) dto.getId());
        if (optionalEspacio.isEmpty()) {
            return new ResponseEntity<>(new Message(null, "El espacio no existe", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }
        Espacio espacio = optionalEspacio.get();

        // Validar y actualizar el nombre si se envía
        if (dto.getNombre() != null && !dto.getNombre().isBlank()) {
            String nuevoNombre = dto.getNombre().trim();
            if (nuevoNombre.length() > 50 || nuevoNombre.length() < 3) {
                return new ResponseEntity<>(new Message(null, "El nombre debe tener entre 3 y 50 caracteres", TypesResponse.ERROR), HttpStatus.BAD_REQUEST);
            }
            if (!nuevoNombre.matches("^[a-zA-Z0-9 ]*$")) {
                return new ResponseEntity<>(new Message(null, "El nombre solo puede contener letras y números", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
            }
            if (espacioRepository.existsByNombre(nuevoNombre) && !espacio.getNombre().equalsIgnoreCase(nuevoNombre)) {
                return new ResponseEntity<>(new Message(null, "El nombre del espacio ya está en uso", TypesResponse.ERROR), HttpStatus.CONFLICT);
            }
            espacio.setNombre(capitalizarPrimeraLetra(nuevoNombre));
        }

        // Validar y actualizar el número de planta si se envía
        if (dto.getNumeroPlanta() != null) {
            if (dto.getNumeroPlanta() < 1) {
                return new ResponseEntity<>(new Message(null, "El número de planta debe ser mayor a 0", TypesResponse.ERROR), HttpStatus.BAD_REQUEST);
            }
            espacio.setNumeroPlanta(dto.getNumeroPlanta());
        }

        // Si hay un nuevo archivo, validamos y subimos la nueva imagen
        if (file != null && !file.isEmpty()) {
            if (!file.getContentType().startsWith("image/")) {
                return new ResponseEntity<>(new Message(null, "El archivo debe ser una imagen (JPG, PNG, etc.)", TypesResponse.ERROR), HttpStatus.BAD_REQUEST);
            }

            // Obtener el publicId actual o generar uno nuevo si no existe
            String publicId = espacio.getPublicId() != null ? espacio.getPublicId() : "espacios/" + dto.getId();

            // Subir la imagen y obtener el resultado (URL y public_id actualizado)
            Map<String, String> uploadResult = cloudinaryService.updateFile(file, publicId);

            // Guardar los nuevos valores en el objeto
            espacio.setUrlImagen(uploadResult.get("url"));
            espacio.setPublicId(uploadResult.get("public_id"));
        }

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
