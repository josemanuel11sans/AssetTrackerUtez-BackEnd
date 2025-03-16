package com.integradora.AssetTrackerUtez.categoriaEspacio.control;

import com.integradora.AssetTrackerUtez.categoriaEspacio.model.CategoriaEspacio;
import com.integradora.AssetTrackerUtez.categoriaEspacio.model.CategoriaEspacioDto;
import com.integradora.AssetTrackerUtez.categoriaEspacio.model.CategoriaEspacioRepository;
import com.integradora.AssetTrackerUtez.utils.Message;
import com.integradora.AssetTrackerUtez.utils.TypesResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CategoriaEspacioService {
    private static final Logger logger = LoggerFactory.getLogger(CategoriaEspacioService.class);
    private final CategoriaEspacioRepository  categoriaEspacioRepository;

    @Autowired
    public CategoriaEspacioService(CategoriaEspacioRepository categoriaEspacioRepository) {
        this.categoriaEspacioRepository = categoriaEspacioRepository;
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Message> findAll() {
        List<CategoriaEspacio> categoriaEspacio = categoriaEspacioRepository.findAll();
        logger.info("La búsqueda de todas las categorias de espacio han sido realizadas correctamente");
        return new ResponseEntity<>(new Message(categoriaEspacio, "Listado de categorias de espacio", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Message> findById(Long id) {
        Optional<CategoriaEspacio> catEspacioOptional = categoriaEspacioRepository.findById(id);
        if (catEspacioOptional.isPresent()) {
            CategoriaEspacio buscarCatEspacio = catEspacioOptional.get();
            return new ResponseEntity<>(new Message(buscarCatEspacio, "Categoria de espacio encontrada", TypesResponse.SUCCESS), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new Message("La categoria de espacio no existe", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Message> findActives(){
        List<CategoriaEspacio> respuestas = categoriaEspacioRepository.findAllByEstadoIsTrue();
        logger.info("Lista de categorias de espacio activas");
        return new ResponseEntity<>(new Message(respuestas, "Categorias de espacio con estado activo", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    @Transactional (rollbackFor = {SQLException.class})
    public ResponseEntity<Message> save(CategoriaEspacioDto dto) {
        // Validación de longitudes
        if (dto.getNombre().length() > 100) {
            return new ResponseEntity<>(new Message("El nombre excede el número de caracteres permitidos", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        if (dto.getDescripcion().length() > 255) {
            return new ResponseEntity<>(new Message("La descripción excede el número de caracteres permitidos", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        CategoriaEspacio categoriaEspacio = new CategoriaEspacio(dto.getNombre(), dto.getDescripcion(), true, dto.getFechaCreacion(),dto.getUltimaActualizacion());
        categoriaEspacio = categoriaEspacioRepository.saveAndFlush(categoriaEspacio);

        if(categoriaEspacio == null) {
            return new ResponseEntity<>(new Message("La categoria de espacio no se registró", TypesResponse.ERROR), HttpStatus.BAD_REQUEST);
        }
        logger.info("La categoria de espacio se registró correctamente");
        return new ResponseEntity<>(new Message(categoriaEspacio, "Categoria de espacio registrado correctamente", TypesResponse.SUCCESS), HttpStatus.CREATED);
    }

    @Transactional (rollbackFor = {SQLException.class})
    public ResponseEntity<Message> update(CategoriaEspacioDto dto) {
        if (dto.getId() == null) {
            return new ResponseEntity<>(new Message("El id de la categoria de espacio no puede ser nulo", TypesResponse.ERROR), HttpStatus.BAD_REQUEST);
        }
        // Validación de longitudes
        if (dto.getNombre().length() > 100) {
            return new ResponseEntity<>(new Message("El nombre excede el número de caracteres permitidos", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        if (dto.getDescripcion().length() > 255) {
            return new ResponseEntity<>(new Message("La descripción excede el número de caracteres permitidos", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        Optional<CategoriaEspacio> catEspacioOptional = categoriaEspacioRepository.findById(dto.getId());
        System.out.println(dto.getId());
        if (!catEspacioOptional.isPresent()) {
            return new ResponseEntity<>(new Message("La categoria de espacio no existe", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }
        CategoriaEspacio categoriaEspacio = catEspacioOptional.get();
        categoriaEspacio.setNombre(dto.getNombre());
        categoriaEspacio.setDescripcion(dto.getDescripcion());
        categoriaEspacio.setUltimaActualizacion(new Date());
        categoriaEspacio = categoriaEspacioRepository.saveAndFlush(categoriaEspacio);
        if (categoriaEspacio == null) {
            return new ResponseEntity<>(new Message("La categoria de espacio no se actualizó", TypesResponse.ERROR), HttpStatus.BAD_REQUEST);
        }
        logger.info("La actualización de la categoria de espacio ha sido realizada correctamente");
        return new ResponseEntity<>(new Message(categoriaEspacio.getNombre(), "Categoria de espacio actualizado correctamente", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Message> changeStatus(CategoriaEspacioDto dto) {
        Optional<CategoriaEspacio> catEspacioOptional = categoriaEspacioRepository.findById(dto.getId());
        if (!catEspacioOptional.isPresent()) {
            return new ResponseEntity<>(new Message("La categoria de espacio no existe", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }
        CategoriaEspacio categoriaEspacio = catEspacioOptional.get();
        categoriaEspacio.setEstado(!categoriaEspacio.isEstado());
        categoriaEspacio = categoriaEspacioRepository.saveAndFlush(categoriaEspacio);
        if (categoriaEspacio == null) {
            return new ResponseEntity<>(new Message("El estado de la categoria de espacio no se actualizó", TypesResponse.ERROR), HttpStatus.BAD_REQUEST);
        }
        logger.info("El estado de la categoria de espacio se actualizó correctamente");
        return new ResponseEntity<>(new Message(categoriaEspacio, "Estado de la categoria de espacio actualizado correctamente", TypesResponse.SUCCESS), HttpStatus.OK);
    }
}
