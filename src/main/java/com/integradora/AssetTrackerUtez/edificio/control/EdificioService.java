package com.integradora.AssetTrackerUtez.edificio.control;

import com.integradora.AssetTrackerUtez.edificio.model.Edificio;
import com.integradora.AssetTrackerUtez.edificio.model.EdificioDTO;
import com.integradora.AssetTrackerUtez.edificio.model.EdificioRepository;
import com.integradora.AssetTrackerUtez.utils.TypesResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.integradora.AssetTrackerUtez.utils.Message;

import java.sql.SQLException;
import java.util.Optional;

@Service
@Transactional
public class EdificioService {
    //Logger
    private static final Logger loger = LoggerFactory.getLogger(EdificioService.class);

    private final EdificioRepository edificioRepository;

    @Autowired
    public EdificioService(EdificioRepository edificioRepository) {
        this.edificioRepository = edificioRepository;
    }

    //Trear todos los edificios
    @Transactional(readOnly = true)
    public ResponseEntity<Object> findAll() {
        return new ResponseEntity<>(new Message(edificioRepository.findAll(), "Listado de edificios ", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    //Trear solo los edificios activos
    @Transactional(readOnly = true)
    public ResponseEntity<Object> findAllEnable() {
        return new ResponseEntity<>(new Message(edificioRepository.findAllByStatusOrderByNombreAsc(true), "Listado de edificios activos", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    //Trear solo los edificios inactivos
    @Transactional(readOnly = true)
    public ResponseEntity<Object> findAllDisable() {
        return new ResponseEntity<>(new Message(edificioRepository.findAllByStatusOrderByNombreAsc(false), "Listado de edificios inactivos", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    //Cambiar estado de un edificio
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Object> changeStatus(EdificioDTO dto){
        Optional<Edificio> optional = edificioRepository.findById(dto.getId());
        if(!optional.isPresent()){
            return new ResponseEntity<>(new Message("Edificio no encontrado", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }
        Edificio edificio = optional.get();
        edificio.setStatus(!edificio.isStatus());
        edificio = edificioRepository.saveAndFlush(edificio);
        if(edificio == null){
            return new ResponseEntity<>(new Message("Error al cambiar estado del edificio", TypesResponse.ERROR), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new Message(edificio, "Edificio actualizado", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    //Guardar un edificio
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Object> GuardarEdificio(EdificioDTO dto){
        //validacion del nombre
        if(dto.getNombre().length() < 3){
            return new ResponseEntity<>(new Message("El nombre no puede tener menos de 3 caracteres ", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        } else if (dto.getNombre().length() > 100){
            return new ResponseEntity<>(new Message("El nombre no pede tener mas de 100 caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        //validacion de caracteres no permitidos
        if(!dto.getNombre().matches("^[a-zA-Z0-9 ]*$")){
            return new ResponseEntity<>(new Message("El nombre solo puede contener letras y numeros", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        //validacion del numero de pisos
        if(dto.getNumeroPisos() < 1){
            return new ResponseEntity<>(new Message("El numero de pisos no puede ser menor a 1", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        //validacion de caracteres no permitidos en pisos
        if(!dto.getNumeroPisos().toString().matches("^[0-9]*$")){
            return new ResponseEntity<>(new Message("El numero de pisos solo puede contener numeros", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        //primerta letra mayuscula
        dto.setNombre(capitalizarPrimeraLetra(dto.getNombre()));
        Edificio edificio = new Edificio(dto.getNombre(), dto.getNumeroPisos(), true);
        edificio = edificioRepository.saveAndFlush(edificio);
        if(edificio == null){
            return new ResponseEntity<>(new Message("Error al guardar el edificio", TypesResponse.ERROR), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new Message(edificio, "Edificio guardado", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    //actualizar un edificio
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Object> AcrualizarEdificio(EdificioDTO dto){
        Optional<Edificio> optional = edificioRepository.findById(dto.getId());

        if(!optional.isPresent()){
            return new ResponseEntity<>(new Message("Edificio no encontrado", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }
        //optiene el edificio actual desde la base de datos
        Edificio edificio = optional.get();
        //validacion del nombre
        if(dto.getNombre().length() < 3){
            return new ResponseEntity<>(new Message("El nombre no puede tener menos de 3 caracteres ", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        } else if (dto.getNombre().length() > 100){
            return new ResponseEntity<>(new Message("El nombre no pede tener mas de 100 caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        //validacion de caracteres no permitidos
        if(!dto.getNombre().matches("^[a-zA-Z0-9 ]*$")){
            return new ResponseEntity<>(new Message("El nombre solo puede contener letras y numeros", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        //validacion del numero de pisos
        if(dto.getNumeroPisos() < 1){
            return new ResponseEntity<>(new Message("El numero de pisos no puede ser menor a 1", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        //validacion de caracteres no permitidos en pisos
        if(!dto.getNumeroPisos().toString().matches("^[0-9]*$")){
            return new ResponseEntity<>(new Message("El numero de pisos solo puede contener numeros", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        //primerta letra mayuscula
        dto.setNombre(capitalizarPrimeraLetra(dto.getNombre()));
        edificio.setNombre(dto.getNombre());
        edificio.setNumeroPisos(dto.getNumeroPisos());
        edificio = edificioRepository.saveAndFlush(edificio);
        if(edificio == null){
            return new ResponseEntity<>(new Message("Error al actualizar el edificio", TypesResponse.ERROR), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new Message(edificio, "Edificio actualizado", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    //funcion para capitalizar la primera letra de un texto
    public static String capitalizarPrimeraLetra(String texto) {
        texto = texto.toLowerCase();
        return texto.substring(0, 1).toUpperCase() + texto.substring(1);
    }
}
