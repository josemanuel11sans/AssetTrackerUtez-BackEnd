package com.integradora.AssetTrackerUtez.inventarioLevantado.control;

import com.integradora.AssetTrackerUtez.Cloudinary.control.CloudinaryService;
import com.integradora.AssetTrackerUtez.espacio.model.Espacio;
import com.integradora.AssetTrackerUtez.espacio.model.EspacioRepository;
import com.integradora.AssetTrackerUtez.inventarioLevantado.model.InventarioLevantado;
import com.integradora.AssetTrackerUtez.inventarioLevantado.model.InventarioLevantadoDTO;
import com.integradora.AssetTrackerUtez.inventarioLevantado.model.InventarioLevantadoRepository;
import com.integradora.AssetTrackerUtez.utils.Message;
import com.integradora.AssetTrackerUtez.utils.TypesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.sql.SQLException;
import java.util.List;
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
        return new ResponseEntity<>(new Message("Inventario Creado", TypesResponse.SUCCESS), HttpStatus.OK);
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
    @Transactional(rollbackFor = {SQLException.class})
    public long contarInventarios(){
        return  inventarioLevantadoRepository.count();
    }
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Message> duplicateLast() {
        // Obtener el último inventario creado
        Optional<InventarioLevantado> ultimoInventarioOpt = inventarioLevantadoRepository.findFirstByOrderByFechaCreacionDesc();

        // Validar si existe un inventario previamente creado
        if (ultimoInventarioOpt.isEmpty()) {
            return new ResponseEntity<>(
                    new Message("No se encontró ningún inventario para duplicar", TypesResponse.WARNING),
                    HttpStatus.BAD_REQUEST
            );
        }

        InventarioLevantado ultimoInventario = ultimoInventarioOpt.get();

        // Crear el nuevo inventario duplicando los datos del último
        InventarioLevantado inventarioDuplicado = new InventarioLevantado(
                true,
                ultimoInventario.getEspacio() // Espacio del último inventario
        );

        // Duplicar otros atributos que sean necesarios
        inventarioDuplicado.setRecursos(ultimoInventario.getRecursos()); // Si tiene una lista de recursos


        try {
            // Guardar el inventario duplicado en la base de datos
            inventarioLevantadoRepository.saveAndFlush(inventarioDuplicado);
        } catch (Exception e) {
            // Manejo de errores
            return new ResponseEntity<>(
                    new Message("Error al duplicar el inventario: " + e.getMessage(), TypesResponse.ERROR),
                    HttpStatus.BAD_REQUEST
            );
        }

        // Respuesta exitosa
        return new ResponseEntity<>(
                new Message("Inventario duplicado exitosamente", TypesResponse.SUCCESS),
                HttpStatus.OK
        );
    }
}
