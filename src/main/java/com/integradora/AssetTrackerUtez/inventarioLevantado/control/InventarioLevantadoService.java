package com.integradora.AssetTrackerUtez.inventarioLevantado.control;

import com.integradora.AssetTrackerUtez.Cloudinary.control.CloudinaryService;
import com.integradora.AssetTrackerUtez.categoriaRecurso.model.CategoriaRecursoDTO;
import com.integradora.AssetTrackerUtez.espacio.model.Espacio;
import com.integradora.AssetTrackerUtez.espacio.model.EspacioRepository;
import com.integradora.AssetTrackerUtez.inventarioLevantado.model.InventarioLevantado;
import com.integradora.AssetTrackerUtez.inventarioLevantado.model.InventarioLevantadoDTO;
import com.integradora.AssetTrackerUtez.inventarioLevantado.model.InventarioLevantadoRepository;
import com.integradora.AssetTrackerUtez.recurso.model.Recurso;
import com.integradora.AssetTrackerUtez.recurso.model.RecursosRepository;
import com.integradora.AssetTrackerUtez.utils.Message;
import com.integradora.AssetTrackerUtez.utils.TypesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class InventarioLevantadoService {

    //Esta servicio no tiene metodo de actualizar porque no se ocupa ya que el uni dato que se podria actualizar es el stado
    //y para eso ya existe una metodo
    @Autowired
    private CloudinaryService  cloudinaryService;

    private final InventarioLevantadoRepository inventarioLevantadoRepository;
    @Autowired
    private EspacioRepository espacioRepository;

    @Autowired
    private RecursosRepository recursosRepository;
    @Autowired
    public InventarioLevantadoService(InventarioLevantadoRepository inventarioLevantadoRepository){
        this.inventarioLevantadoRepository = inventarioLevantadoRepository;
    }

    //Metodos
    //all
    @Transactional(readOnly = true)
    public ResponseEntity<Object> findAll() {
        List<InventarioLevantado> inventarios = inventarioLevantadoRepository.findAllWithEspacioYRecursos();
        return new ResponseEntity<>(new Message(inventarios, "Listado de inventarios", TypesResponse.SUCCESS), HttpStatus.OK);
    }
    @Transactional(readOnly = true)
    public ResponseEntity<Object> findById(Long id) {
        if (!inventarioLevantadoRepository.existsById(id)){
            return new ResponseEntity<>(new Message(null, "Inventario no encontrado", TypesResponse.WARNING), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new Message(inventarioLevantadoRepository.findById(id), "Inventario encontrado", TypesResponse.SUCCESS), HttpStatus.OK);
    }
    //save
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Object> save(InventarioLevantadoDTO dto) {
        // Validación que el espacio sea un id entero
        if (!String.valueOf(dto.getEspacio()).matches("^-?\\d+$")) {
            return new ResponseEntity<>(new Message("El espacio tiene que ser un id entero", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        // Buscar el espacio en la base de datos
        Espacio espacio = espacioRepository.findById((long) dto.getEspacio()).orElse(null);
        // Validar si el espacio no existe
        if (espacio == null) {
            return new ResponseEntity<>(new Message("El espacio no existe", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        // Crear el inventario levantado
        InventarioLevantado inventarioLevantado = new InventarioLevantado(true, espacio);
        try {
            // Guardar el inventario levantado en la base de datos
            inventarioLevantado = inventarioLevantadoRepository.saveAndFlush(inventarioLevantado);
        } catch (Exception e) {
            // Manejo de errores en caso de que la persistencia falle
            return new ResponseEntity<>(new Message("Error al crear el inventario: " + e.getMessage(), TypesResponse.ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        // Retornar respuesta exitosa
        return new ResponseEntity<>(new Message(inventarioLevantado,"Inventario Creado", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    @Transactional(rollbackFor = {Exception.class})
    public ResponseEntity<Object> updateInventario(InventarioLevantadoDTO dto, MultipartFile file) {
        Optional<InventarioLevantado> optionalInventarioLevantado = inventarioLevantadoRepository.findById(dto.getId());

        if (optionalInventarioLevantado.isEmpty()) {
            return new ResponseEntity<>(new Message(null, "El inventario no existe", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }

        InventarioLevantado inventarioLevantado = optionalInventarioLevantado.get();

        // Validar y subir imagen si se envía
        if (file != null && !file.isEmpty()) {
            if (!file.getContentType().startsWith("image/")) {
                return new ResponseEntity<>(new Message(null, "El archivo debe ser una imagen (JPG, PNG, etc.)", TypesResponse.ERROR), HttpStatus.BAD_REQUEST);
            }

            String publicid = inventarioLevantado.getPublicId() != null ? inventarioLevantado.getPublicId() : "inventario/" + inventarioLevantado.getId();
            Map<String, String> uploadResult = cloudinaryService.uploadFile(file);

            inventarioLevantado.setImagenUrl(uploadResult.get("url"));
            inventarioLevantado.setPublicId(uploadResult.get("public_id"));
        }

        // Guardar los cambios
        inventarioLevantado = inventarioLevantadoRepository.saveAndFlush(inventarioLevantado);

        return new ResponseEntity<>(new Message(inventarioLevantado, "Inventario actualizado correctamente", TypesResponse.SUCCESS), HttpStatus.OK);
    }


    //changeStatus
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Object> changeStatus(InventarioLevantadoDTO dto){
        Optional<InventarioLevantado> optional = inventarioLevantadoRepository.findById((long) dto.getId());
        if(optional.isEmpty()){
            return  new ResponseEntity<>(new Message(null, "Invetario no encontrado", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }
        InventarioLevantado inventarioLevantado = optional.get();
        inventarioLevantado.setStatus(!inventarioLevantado.isStatus());
        inventarioLevantado = inventarioLevantadoRepository.saveAndFlush(inventarioLevantado);
        if(inventarioLevantado == null){
            return new ResponseEntity<>(new Message(inventarioLevantado, "Erro al cambiar el estado", TypesResponse.SUCCESS), HttpStatus.OK);
        }
        return new ResponseEntity<>(new Message(inventarioLevantado, "Estado del inventario actualizado", TypesResponse.SUCCESS), HttpStatus.OK);
    }
    @Transactional(readOnly = true)
    public  ResponseEntity<Object> anable(){
        return new ResponseEntity<>(new Message(inventarioLevantadoRepository.findAllByStatus(true), "Listado de invatarios activos", TypesResponse.SUCCESS), HttpStatus.OK);
    }
    @Transactional(readOnly = true)
    public  ResponseEntity<Object> disable(){
        return new ResponseEntity<>(new Message(inventarioLevantadoRepository.findAllByStatus(false), "Listado de invatarios inactivos", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> findByEspacioId(Long idEspacio) {
        List<InventarioLevantado> inventarioLevantados = inventarioLevantadoRepository.findAllByEspacioId(idEspacio);

        if (inventarioLevantados.isEmpty()) {
            return new ResponseEntity<>(new Message(null, "No se encontraron inventarios para este edificio", TypesResponse.WARNING), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new Message(inventarioLevantados, "Espacios encontrados", TypesResponse.SUCCESS), HttpStatus.OK);
    }
    @Transactional(rollbackFor = {SQLException.class})
    public long contarInventarios(){
        return  inventarioLevantadoRepository.count();
    }
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Message> duplicateLast() {
        Optional<InventarioLevantado> ultimoInventarioOpt = inventarioLevantadoRepository.findFirstByOrderByFechaCreacionDesc();

        if (ultimoInventarioOpt.isEmpty()) {
            return new ResponseEntity<>(
                    new Message("No se encontró ningún inventario para duplicar", TypesResponse.WARNING),
                    HttpStatus.BAD_REQUEST
            );
        }

        InventarioLevantado ultimoInventario = ultimoInventarioOpt.get();

        // Crear nuevo inventario
        InventarioLevantado inventarioDuplicado = new InventarioLevantado(
                true,
                ultimoInventario.getEspacio()
        );

        // Guardar el nuevo inventario primero
        inventarioDuplicado = inventarioLevantadoRepository.saveAndFlush(inventarioDuplicado);
        Integer idInventarioCreado = Math.toIntExact(inventarioDuplicado.getId());

        System.out.println("ID generado: " + idInventarioCreado);


        // Clonar recursos
        List<Recurso> recursosDuplicados = new ArrayList<>();
        for (Recurso recursoOriginal : ultimoInventario.getRecursos()) {
            Recurso nuevoRecurso = new Recurso();
            nuevoRecurso.setCodigo(recursoOriginal.getCodigo());
            nuevoRecurso.setDescripcion(recursoOriginal.getDescripcion());
            nuevoRecurso.setMarca(recursoOriginal.getMarca());
            nuevoRecurso.setModelo(recursoOriginal.getModelo());
            nuevoRecurso.setNumeroSerie(recursoOriginal.getNumeroSerie());
            nuevoRecurso.setObservaciones(recursoOriginal.getObservaciones());
            nuevoRecurso.setCategoriaRecurso(recursoOriginal.getCategoriaRecurso());
            nuevoRecurso.setResponsable(recursoOriginal.getResponsable());
            nuevoRecurso.setStatus(recursoOriginal.isStatus());
            nuevoRecurso.setInventarioLevantado(inventarioDuplicado); // Relación

            recursosDuplicados.add(nuevoRecurso);
        }

        // Guardar los recursos clonados
        recursosRepository.saveAll(recursosDuplicados);

        return new ResponseEntity<>(
                new Message(inventarioDuplicado,"Inventario duplicado exitosamente", TypesResponse.SUCCESS),
                HttpStatus.OK
        );
    }

}
