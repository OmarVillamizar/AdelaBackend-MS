package com.example.chaea.services;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.chaea.dto.OpcionDTO;
import com.example.chaea.dto.PreguntaDTO;
import com.example.chaea.entities.Categoria;
import com.example.chaea.entities.Cuestionario;
import com.example.chaea.entities.Opcion;
import com.example.chaea.entities.Pregunta;
import com.example.chaea.repositories.CuestionarioRepository;
import com.example.chaea.repositories.PreguntaRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PreguntaService {
    
    @Autowired
    private PreguntaRepository preguntaRepository;
    
    @Autowired
    private CuestionarioRepository cuestionarioRepository;
    
    @Autowired
    private OpcionService opcionService;
    
    public Pregunta crearPregunta(Long idCuestionario, String preguntaStr, int orden) {
        Cuestionario cuestionario = cuestionarioRepository.findById(idCuestionario)
                .orElseThrow(() -> new EntityNotFoundException("Cuestionario no encontrado con id " + idCuestionario));
        Pregunta pregunta = new Pregunta();
        pregunta.setCuestionario(cuestionario);
        pregunta.setPregunta(preguntaStr);
        pregunta.setOrden(orden);
        
        return preguntaRepository.save(pregunta);
    }
    
    public void eliminarPregunta(Pregunta pregunta) {
        for(Opcion opcion : pregunta.getOpciones()) {
            opcionService.eliminarOpcion(opcion);
        }
        pregunta.getOpciones().clear();
        preguntaRepository.delete(pregunta);
    }
    
    public Pregunta crearPregunta(Cuestionario cuestionario, Map<Integer, Categoria> mapId, PreguntaDTO preguntaDTO) {
        Pregunta preguntaSave = new Pregunta();
        preguntaSave.setCuestionario(cuestionario);
        preguntaSave.setPregunta(preguntaDTO.getPregunta());
        preguntaSave.setOrden(preguntaDTO.getOrden());
        
        Pregunta pregunta = preguntaRepository.save(preguntaSave);
                
        Set<Opcion> opciones = new HashSet<>();

        for(OpcionDTO opcionDTO : preguntaDTO.getOpciones()) {
            Categoria categoria = mapId.get(opcionDTO.getCategoriaId());

            if(categoria == null) {
                throw new RuntimeException("No existe una categoría con id "+opcionDTO.getCategoriaId()+" en la solicitud.");
            }
            Opcion opcion = opcionService.crearOpcion(pregunta, categoria, opcionDTO);
            opciones.add(opcion);
        }
        
        pregunta.setOpciones(opciones);
        
        return preguntaRepository.save(pregunta);
    }
}
