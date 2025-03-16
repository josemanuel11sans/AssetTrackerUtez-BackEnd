package com.integradora.AssetTrackerUtez.responsable.control;

import com.integradora.AssetTrackerUtez.responsable.model.Responsable;
import com.integradora.AssetTrackerUtez.responsable.model.ResponsableDTO;
import com.integradora.AssetTrackerUtez.responsable.model.ResponsableRepository;
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
public class ResponsableService {
    private static final Logger logger = LoggerFactory.getLogger(ResponsableService.class);
    private final ResponsableRepository responsableRepository;

    @Autowired
    public ResponsableService(ResponsableRepository responsableRepository) {
        this.responsableRepository = responsableRepository;
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Message> findAll() {
        List<Responsable> responsables = responsableRepository.findAll();
        logger.info("La búsqueda de todos los responsable ha sido realizada correctamente");
        return new ResponseEntity<>(new Message(responsables, "Listado de responsables", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Message> findById(Long id) {
        Optional<Responsable> responsableOptional = responsableRepository.findById(id);
        if (responsableOptional.isPresent()) {
            Responsable buscarResponsable = responsableOptional.get();
            return new ResponseEntity<>(new Message(buscarResponsable, "Responsable encontrada", TypesResponse.SUCCESS), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new Message("El responsable no existe", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Message> findActives(){
        List<Responsable> respuestas = responsableRepository.findAllByEstadoIsTrue();
        logger.info("Lista de responsables activos");
        return new ResponseEntity<>(new Message(respuestas, "Responsables con estado activo", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    @Transactional (rollbackFor = {SQLException.class})
    public ResponseEntity<Message> save(ResponsableDTO dto) {
        // Validación de longitudes
        if (dto.getNombre().length() > 100) {
            return new ResponseEntity<>(new Message("El nombre excede el número de caracteres permitidos", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        if (dto.getDivisionAcademica().length() > 100) {
            return new ResponseEntity<>(new Message("La división acdémica excede el número de caracteres permitidos", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        Responsable responsable = new Responsable(dto.getNombre(), dto.getDivisionAcademica(), true);
        responsable = responsableRepository.saveAndFlush(responsable);

        if(responsable == null) {
            return new ResponseEntity<>(new Message("El responsable no se registró", TypesResponse.ERROR), HttpStatus.BAD_REQUEST);
        }
        logger.info("El responsable se registró correctamente");
        return new ResponseEntity<>(new Message(responsable, "Responsable registrado correctamente", TypesResponse.SUCCESS), HttpStatus.CREATED);
    }

    @Transactional (rollbackFor = {SQLException.class})
    public ResponseEntity<Message> update(ResponsableDTO dto) {
        if (dto.getId() == null) {
            return new ResponseEntity<>(new Message("El id del responsable no puede ser nulo", TypesResponse.ERROR), HttpStatus.BAD_REQUEST);
        }
        // Validación de longitudes
        if (dto.getNombre().length() > 100) {
            return new ResponseEntity<>(new Message("El nombre excede el número de caracteres permitidos", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        if (dto.getDivisionAcademica().length() > 100) {
            return new ResponseEntity<>(new Message("La división acdémica excede el número de caracteres permitidos", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        Optional<Responsable> responsableOptional = responsableRepository.findById(dto.getId());
        System.out.println(dto.getId());
        if (!responsableOptional.isPresent()) {
            return new ResponseEntity<>(new Message("El responsable no existe", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }
        Responsable responsable = responsableOptional.get();
        responsable.setNombre(dto.getNombre());
        responsable.setDivisionAcademica(dto.getDivisionAcademica());
        responsable.setUltimaActualizacion(new Date());
        responsable = responsableRepository.saveAndFlush(responsable);
        if (responsable == null) {
            return new ResponseEntity<>(new Message("El responsable no se actualizó", TypesResponse.ERROR), HttpStatus.BAD_REQUEST);
        }
        logger.info("La actualización del responsable ha sido realizada correctamente");
        return new ResponseEntity<>(new Message(responsable.getNombre(), "Responsable actualizado correctamente", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Message> changeStatus(ResponsableDTO dto) {
        Optional<Responsable> responsableOptional = responsableRepository.findById(dto.getId());
        if (!responsableOptional.isPresent()) {
            return new ResponseEntity<>(new Message("El responsable no existe", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }
        Responsable responsable = responsableOptional.get();
        responsable.setEstado(!responsable.isEstado());
        responsable = responsableRepository.saveAndFlush(responsable);
        if (responsable == null) {
            return new ResponseEntity<>(new Message("El estado del responsable no se actualizó", TypesResponse.ERROR), HttpStatus.BAD_REQUEST);
        }
        logger.info("El estado del responsable se actualizó correctamente");
        return new ResponseEntity<>(new Message(responsable, "Estado del responsable actualizado correctamente", TypesResponse.SUCCESS), HttpStatus.OK);
    }
}
