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
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    @Transactional(readOnly = true)
        public ResponseEntity<Message> findByCodigo(String codigo) {
            Optional<Recurso> recurso = recursosRepository.findFirstByCodigoOrderByFechaCreacionDesc(codigo);
            if (recurso.isPresent()) {
                Message message = new Message(recurso.get(), "Recurso encontrado", TypesResponse.SUCCESS);
                return new ResponseEntity<>(message, HttpStatus.OK);
            } else {
                Message message = new Message(null, "Recurso no encontrado", TypesResponse.WARNING);
                return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
            }
        }


    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Object> save(RecursosDTO dto){
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
        /*
        Map<String, String> uploadResult =  cloudinaryService.uploadFile(file);
        String imagenUrl = uploadResult.get("url");
        String publicId = uploadResult.get("public_id");
        */


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
                responsable//,
                //publicId,
               // imagenUrl
        );

        // Guardar el recurso
        recurso = recursosRepository.saveAndFlush(recurso);
        if (recurso == null){
            return new ResponseEntity<>(new Message("Error al guardar el recurso", TypesResponse.ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(new Message("null","Recurso guardado", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Object> update(RecursosDTO dto) {
        Optional<Recurso> optionalRecurso = recursosRepository.findById(dto.getId());
        if (optionalRecurso.isEmpty()) {
            return new ResponseEntity<>(new Message("El recurso no existe", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }

        Recurso recurso = optionalRecurso.get();

        // Descripción
        if (dto.getDescripcion() != null) {
            if (dto.getDescripcion().length() < 3 || dto.getDescripcion().length() > 255) {
                return new ResponseEntity<>(new Message("La descripción debe tener entre 3 y 255 caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
            }
            recurso.setDescripcion(dto.getDescripcion().trim());
        }

        // Marca
        if (dto.getMarca() != null) {
            if (dto.getMarca().length() < 3 || dto.getMarca().length() > 255) {
                return new ResponseEntity<>(new Message("La marca debe tener entre 3 y 255 caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
            }
            recurso.setMarca(dto.getMarca().trim());
        }

        // Modelo
        if (dto.getModelo() != null) {
            if (dto.getModelo().length() < 1 || dto.getModelo().length() > 255) {
                return new ResponseEntity<>(new Message("El modelo debe tener entre 1 y 255 caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
            }
            recurso.setModelo(dto.getModelo().trim());
        }

        // Número de serie
        if (dto.getNumeroSerie() != null) {
            if (dto.getNumeroSerie().length() < 3 || dto.getNumeroSerie().length() > 255) {
                return new ResponseEntity<>(new Message("El número de serie debe tener entre 3 y 255 caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
            }
            recurso.setNumeroSerie(dto.getNumeroSerie().trim());
        }

        // Observaciones
        if (dto.getObservaciones() != null) {
            if (dto.getObservaciones().length() < 3 || dto.getObservaciones().length() > 255) {
                return new ResponseEntity<>(new Message("Las observaciones deben tener entre 3 y 255 caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
            }
            recurso.setObservaciones(dto.getObservaciones().trim());
        }

        // Código (si quieres permitir editarlo)
        if (dto.getCodigo() != null && !dto.getCodigo().isBlank()) {
            recurso.setCodigo(dto.getCodigo().trim());
        }

        // Relaciones: solo si se mandan nuevos IDs
        if (dto.getInvetariolevantadoid() != 0) {
            InventarioLevantado inv = inventarioLevantadoRepository.findById((long) dto.getInvetariolevantadoid()).orElse(null);
            if (inv == null) {
                return new ResponseEntity<>(new Message("El inventario no existe", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
            }
            recurso.setInventarioLevantado(inv);
        }

        if (dto.getCategoriaRecursoid() != 0) {
            CategoriaRecurso cat = categoriaRecursoRepository.findById((long) dto.getCategoriaRecursoid()).orElse(null);
            if (cat == null) {
                return new ResponseEntity<>(new Message("La categoría no existe", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
            }
            recurso.setCategoriaRecurso(cat);
        }

        if (dto.getResponsableid() != 0) {
            Responsable resp = responsableRepository.findById((long) dto.getResponsableid()).orElse(null);
            if (resp == null) {
                return new ResponseEntity<>(new Message("El responsable no existe", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
            }
            recurso.setResponsable(resp);
        }

        // Guardar cambios
        recurso = recursosRepository.saveAndFlush(recurso);
        return new ResponseEntity<>(new Message(recurso, "Recurso actualizado correctamente", TypesResponse.SUCCESS), HttpStatus.OK);
    }


    @Transactional(readOnly = true)
    public ResponseEntity<Object> findByEspacioId(Long idInventario) {
        List<Recurso> recursos = recursosRepository.findAllByInventarioLevantadoId(idInventario);
        if (recursos.isEmpty()) {
            return new ResponseEntity<>(new Message(null, "No se encontraron recursos para este inventario", TypesResponse.WARNING), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new Message(recursos, "Recursos encontrados", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    @Transactional(rollbackFor = {SQLException.class})
    public long contarRecursos(){
        return  recursosRepository.count();
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> getPorcentajeRecursosPorCategoria() {
        List<Recurso> recursos = recursosRepository.findByStatus(true);

        if (recursos.isEmpty()) {
            logger.info("No se encontraron recursos activos");
            return new ResponseEntity<>(new Message(null, "No hay recursos activos", TypesResponse.WARNING), HttpStatus.NOT_FOUND);
        }

        // Total de recursos activos
        Long total = recursosRepository.count();
        logger.info("Total de recursos activos: {}", total);

        // Agrupar y contar por categoría
        Map<String, Long> conteoPorCategoria = recursos.stream()
                .filter(r -> r.getCategoriaRecurso() != null && r.getCategoriaRecurso().getNombre() != null)
                .collect(
                        java.util.stream.Collectors.groupingBy(
                                r -> r.getCategoriaRecurso().getNombre(),
                                java.util.stream.Collectors.counting()
                        )
                );

        // Log de categorías agrupadas
        logger.info("Conteo por categoría: {}", conteoPorCategoria);

        // Crear la estructura de respuesta con el porcentaje
        List<Map<String, Object>> respuesta = conteoPorCategoria.entrySet().stream()
                .map(entry -> {
                    Map<String, Object> map = new java.util.HashMap<>();
                    map.put("categoria", entry.getKey());

                    // Calculamos el porcentaje de recursos por categoría
                    long categoriaCount = entry.getValue();
                    double porcentaje = Math.round((categoriaCount * 100.0) / total);
                    map.put("porcentaje", porcentaje);

                    logger.info("Categoría: {} - Recursos: {} - Porcentaje: {}", entry.getKey(), categoriaCount, porcentaje);
                    return map;
                }).collect(java.util.stream.Collectors.toList());

        // Log de la respuesta final
        logger.info("Respuesta final: {}", respuesta);

        return new ResponseEntity<>(new Message(respuesta, "Porcentaje de recursos por categoría", TypesResponse.SUCCESS), HttpStatus.OK);
    }



}
