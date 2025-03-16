package com.integradora.AssetTrackerUtez.recurso.control;

import com.cloudinary.Cloudinary;
import com.integradora.AssetTrackerUtez.Cloudinary.control.CloudinaryService;
import com.integradora.AssetTrackerUtez.categoriaRecurso.model.CategoriaRecurso;
import com.integradora.AssetTrackerUtez.categoriaRecurso.model.CategoriaRecursoRepository;
import com.integradora.AssetTrackerUtez.inventarioLevantado.model.InventarioLevantado;
import com.integradora.AssetTrackerUtez.inventarioLevantado.model.InventarioLevantadoRepository;
import com.integradora.AssetTrackerUtez.recurso.model.Recurso;
import com.integradora.AssetTrackerUtez.recurso.model.RecursosDTO;
import com.integradora.AssetTrackerUtez.recurso.model.RecursosRepository;
import com.integradora.AssetTrackerUtez.responsable.model.Responsable;
import com.integradora.AssetTrackerUtez.responsable.model.ResponsableRepository;
import com.integradora.AssetTrackerUtez.usuario.control.UsuarioService;
import com.integradora.AssetTrackerUtez.utils.Message;
import com.integradora.AssetTrackerUtez.utils.TypesResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.Map;

@Service
@Transactional
public class RecursosService {
    private static final Logger logger = LoggerFactory.getLogger(RecursosService.class);
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private InventarioLevantadoRepository inventarioLevantadoRepository;
    @Autowired
    private CategoriaRecursoRepository categoriaRecursoRepository;
    @Autowired
    private ResponsableRepository  responsableRepository;
    private final RecursosRepository recursosRepository;
    @Autowired
    public RecursosService(RecursosRepository recursosRepository){
        this.recursosRepository = recursosRepository;
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> finfAll(){
        return  new ResponseEntity<>(new Message(recursosRepository.findAll(), "Listado de recursos", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Object> save(RecursosDTO dto, MultipartFile file){
        // Validación de descripción
        if(dto.getDescripcion() != null) {
            if(dto.getDescripcion().length() < 3){
                return new ResponseEntity<>(new Message("La descripcion no puede tener menos de 3 caracteres ", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
            } else if (dto.getDescripcion().length() > 255){
                return new ResponseEntity<>(new Message("La descripcion no puede tener más de 255 caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
            }
        }

        // Validación de marca
        if(dto.getMarca() != null) {
            if(dto.getMarca().length() < 3){
                return new ResponseEntity<>(new Message("La marca no puede tener menos de 3 caracteres ", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
            } else if (dto.getMarca().length() > 255){
                return new ResponseEntity<>(new Message("La marca no puede tener más de 255 caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
            }
        }

        // Validación de modelo
        if(dto.getModelo() != null) {
            if(dto.getModelo().length() < 1){
                return new ResponseEntity<>(new Message("El modelo no puede tener menos de 1 carácter ", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
            } else if (dto.getModelo().length() > 255){
                return new ResponseEntity<>(new Message("El modelo no puede tener más de 255 caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
            }
        }

        // Validación de número de serie
        if(dto.getNumeroSerie() != null) {
            if(dto.getNumeroSerie().length() < 3){
                return new ResponseEntity<>(new Message("El número de serie no puede tener menos de 3 caracteres ", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
            } else if (dto.getNumeroSerie().length() > 255){
                return new ResponseEntity<>(new Message("El número de serie no puede tener más de 255 caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
            }
        }

        // Validación de observaciones
        if(dto.getObservaciones() != null) {
            if(dto.getObservaciones().length() < 3){
                return new ResponseEntity<>(new Message("Las observaciones no pueden tener menos de 3 caracteres ", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
            } else if (dto.getObservaciones().length() > 255){
                return new ResponseEntity<>(new Message("Las observaciones no pueden tener más de 255 caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
            }
        }

        // Validación de IDs
        if(!String.valueOf(dto.getInvetariolevantadoid()).matches("^-?\\d+$")){
            return new ResponseEntity<>(new Message("El recurso debe tener un ID de inventario válido", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        if(!String.valueOf(dto.getCategoriaRecursoid()).matches("^-?\\d+$")){
            return new ResponseEntity<>(new Message("La categoría debe tener un ID de categoría válido", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        if(!String.valueOf(dto.getResponsableid()).matches("^-?\\d+$")){
            return new ResponseEntity<>(new Message("El responsable debe tener un ID de responsable válido", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }

        // Búsqueda de entidad relacionada
        InventarioLevantado inventarioLevantado = inventarioLevantadoRepository.findById((long)dto.getInvetariolevantadoid()).orElse(null);
        CategoriaRecurso categoriaRecurso = categoriaRecursoRepository.findById( (long)dto.getCategoriaRecursoid()).orElse(null);
        Responsable responsable = responsableRepository.findById( (long)dto.getResponsableid()).orElse(null);

        // Validación de existencia de las entidades relacionadas
        if (inventarioLevantado == null){
            return new ResponseEntity<>(new Message("El inventario no existe", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        if (categoriaRecurso == null){
            return new ResponseEntity<>(new Message("La categoría no existe", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        if (responsable == null){
            return new ResponseEntity<>(new Message("El responsable no existe", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }

        Map<String, String> uploadResult =  cloudinaryService.uploadFile(file);
        String imagenUrl = uploadResult.get("url");
        String publicId = uploadResult.get("public_id");



        // Creación del nuevo recurso
        Recurso recurso = new Recurso(
                dto.getCodigo(),
                dto.getDescripcion() != null ? dto.getDescripcion() : null,  // Si no se pasa, se asigna null
                dto.getMarca() != null ? dto.getMarca() : null,  // Si no se pasa, se asigna null
                dto.getModelo() != null ? dto.getModelo() : null,  // Si no se pasa, se asigna null
                dto.getNumeroSerie() != null ? dto.getNumeroSerie() : null,  // Si no se pasa, se asigna null
                dto.getObservaciones() != null ? dto.getObservaciones() : null,  // Si no se pasa, se asigna null
                true,
                inventarioLevantado,
                categoriaRecurso,
                responsable,
                publicId,
                imagenUrl
        );

        // Guardar el recurso
        recurso = recursosRepository.saveAndFlush(recurso);
        if (recurso == null){
            return new ResponseEntity<>(new Message("Error al guardar el recurso", TypesResponse.ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(new Message(recurso,"Recurso guardado", TypesResponse.SUCCESS), HttpStatus.OK);
    }



}
