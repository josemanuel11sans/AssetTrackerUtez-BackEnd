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

import java.sql.SQLException;

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
    public ResponseEntity<Object> save(RecursosDTO dto){
        //validacion de codigo
        //validaciones de descripcion
        //validaciones de marca
        //validaciones de modelo
        //validaciones de numSerie
        //validaciones de Observaciones
        //validacion del id del inventario al que pertenece
        if(!String.valueOf(dto.getInvetariolevantadoid()).matches("^-?\\d+$")){return new ResponseEntity<>(new Message("El recurso que ser un id entero", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);}
        //validacion del id de la categoria de recurso a la que pertenece
        if(!String.valueOf(dto.getCategoriaRecursoid()).matches("^-?\\d+$")){return new ResponseEntity<>(new Message("La categoria que ser un id entero", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);}
        //validacion del id del el responsable al que le pertenece el recurso
        if(!String.valueOf(dto.getResponsableid()).matches("^-?\\d+$")){return new ResponseEntity<>(new Message("El responsable que ser un id entero", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);}


        logger.info(dto.getInvetariolevantadoid() + "si existe la maldita categoria");

        InventarioLevantado inventarioLevantado = inventarioLevantadoRepository.findById((long)dto.getInvetariolevantadoid()).orElse(null);
        CategoriaRecurso categoriaRecurso = categoriaRecursoRepository.findById( (long)dto.getCategoriaRecursoid()).orElse(null);
        Responsable responsable = responsableRepository.findById( (long)dto.getResponsableid()).orElse(null);


        if (inventarioLevantado == null){return new ResponseEntity<>(new Message("El invenntario no existe", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);}
        if (categoriaRecurso == null){return new ResponseEntity<>(new Message("La categoria no existe", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);}
        if (responsable == null){return new ResponseEntity<>(new Message("El responsable no existe", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);}
        //Creamos un nuevo recurso
        Recurso recurso = new Recurso(
                dto.getCodigo(),
                dto.getDescripcion(),
                dto.getMarca(),
                dto.getModelo(),
                dto.getNumeroSerie(),
                dto.getObservaciones(),
                true,
                inventarioLevantado,
                categoriaRecurso,
                responsable
        );
        recurso = recursosRepository.saveAndFlush(recurso);
        if ( recurso == null){
            return new ResponseEntity<>(new Message("Erro al guardar el recurso: ", TypesResponse.ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(new Message(recurso,"Recurso guardado", TypesResponse.SUCCESS), HttpStatus.OK);
    }


}
