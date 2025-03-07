package com.integradora.AssetTrackerUtez.categoriaRecurso.control;

import com.integradora.AssetTrackerUtez.Cloudinary.control.CloudinaryService;
import com.integradora.AssetTrackerUtez.categoriaRecurso.model.CategoriaRecurso;
import com.integradora.AssetTrackerUtez.categoriaRecurso.model.CategoriaRecursoDTO;
import com.integradora.AssetTrackerUtez.categoriaRecurso.model.CategoriaRecursoRepository;
import com.integradora.AssetTrackerUtez.utils.Message;
import com.integradora.AssetTrackerUtez.utils.TypesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
@Transactional
public class CategoriaRecursoService {
   @Autowired
   private CloudinaryService cloudinaryService;

   private final CategoriaRecursoRepository categoriaRecursoRepository;
   @Autowired
   public CategoriaRecursoService(CategoriaRecursoRepository categoriaRecursoRepository) {
         this.categoriaRecursoRepository = categoriaRecursoRepository;
    }

   //metodos
   //tarer todas las categorias de recurso
    @Transactional(readOnly = true)
    public ResponseEntity<Object> findAll() {
        return new ResponseEntity<>(new Message(categoriaRecursoRepository.findAll(), "Listado de categorias de recurso", TypesResponse.SUCCESS), HttpStatus.OK);
    }
    //traer todas las categorias de recurso habilitadas
    @Transactional(readOnly = true)
    public ResponseEntity<Object> findAllEnable() {
        return new ResponseEntity<>(new Message(categoriaRecursoRepository.findAllByStatusOrderByNombre(true), "Listado de categorias de recurso", TypesResponse.SUCCESS), HttpStatus.OK);
    }
    //traer todas las categorias de recurso deshabilitadas
    @Transactional(readOnly = true)
    public ResponseEntity<Object> findAllDisable() {
        return new ResponseEntity<>(new Message(categoriaRecursoRepository.findAllByStatusOrderByNombre(false), "Listado de categorias de recurso", TypesResponse.SUCCESS), HttpStatus.OK);
    }
    //traer una categoria de recurso por id
    @Transactional(readOnly = true)
    public ResponseEntity<Object> findById(int id) {
        return new ResponseEntity<>(new Message(categoriaRecursoRepository.findById((long) id), "Categoria de recurso encontrada", TypesResponse.SUCCESS), HttpStatus.OK);
    }
    //guardar una categoria de recurso
    @Transactional(rollbackFor = {Exception.class})
    public ResponseEntity<Object> save(CategoriaRecursoDTO dto, MultipartFile file) {
        //validaciones
        //valida que el nombre no exista
        if(categoriaRecursoRepository.existsByNombre(dto.getNombre())){
            return new ResponseEntity<>(new Message(null, "El nombre de la categoria de recurso ya existe", TypesResponse.ERROR), HttpStatus.OK);
        }
        //valida que el nombre no sea tan grande
        if(dto.getNombre().length() > 100){
            return new ResponseEntity<>(new Message(null, "El nombre de la categoria de recurso es muy grande", TypesResponse.ERROR), HttpStatus.OK);
        }
        //valida que el nombre no sea tan pequeño
        if(dto.getNombre().length() < 3){
            return new ResponseEntity<>(new Message(null, "El nombre de la categoria de recurso es muy pequeño", TypesResponse.ERROR), HttpStatus.OK);
        }
        //valida que el material no sea tan grande
        if(dto.getMaterial().length() > 100){
            return new ResponseEntity<>(new Message(null, "El material de la categoria de recurso es muy grande", TypesResponse.ERROR), HttpStatus.OK);
        }
        //valida qeu el material no sea ran peqiueño
        if(dto.getMaterial().length() < 3){
            return new ResponseEntity<>(new Message(null, "El material de la categoria de recurso es muy pequeño", TypesResponse.ERROR), HttpStatus.OK);
        }
        //validacion de caracteres no permitidos en nombre
        if(!dto.getNombre().matches("^[a-zA-Z0-9 ]*$")){
            return new ResponseEntity<>(new Message("El nombre solo puede contener letras y numeros", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        //validacion de caracteres no permitidos en material
        if(!dto.getMaterial().matches("^[a-zA-Z0-9 ]*$")){
            return new ResponseEntity<>(new Message("El material solo puede contener letras y numeros", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }

        //valida que la imagen sea una imagen
        if (!file.getContentType().startsWith("image/")) {
            return new ResponseEntity<>(new Message(null, "El archivo debe ser una imagen (JPG, PNG, etc.)", TypesResponse.ERROR), HttpStatus.BAD_REQUEST);
        }
        //subir la imagen a cloudinary y obtener la url y el public id
        Map<String, String> uploadResult = cloudinaryService.uploadFile(file);
        String imageUrl = uploadResult.get("url");
        String publicId = uploadResult.get("public_id");
        //capitalizar la primera letra del nombre y material y quitar espacios al inicio y al final
        dto.setNombre(capitalizarPrimeraLetra(dto.getNombre().trim()));
        dto.setMaterial(capitalizarPrimeraLetra(dto.getMaterial().trim()));
        //
        //guardar la categoria de recurso
        CategoriaRecurso categoriaRecurso = new CategoriaRecurso(dto.getNombre(), dto.getMaterial(), imageUrl, publicId, true);
        categoriaRecurso = categoriaRecursoRepository.saveAndFlush(categoriaRecurso);
        if(categoriaRecurso == null){
            return new ResponseEntity<>(new Message(categoriaRecurso, "Error al registrar la categoria de recurso", TypesResponse.ERROR), HttpStatus.OK);
        }
        return new ResponseEntity<>(new Message(categoriaRecurso, "Categoria de recurso guardada", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    //funcion para capitalizar la primera letra de un texto
    public static String capitalizarPrimeraLetra(String texto) {
        if (texto == null || texto.isEmpty()) {
            return texto;  // Si el texto está vacío o es nulo, retornamos el mismo valor.
        }
        // Capitaliza solo la primera letra y mantiene el resto igual
        return texto.substring(0, 1).toUpperCase() + texto.substring(1);
    }

}
