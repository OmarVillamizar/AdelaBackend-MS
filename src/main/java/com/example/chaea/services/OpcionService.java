package com.example.chaea.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.chaea.dto.OpcionDTO;
import com.example.chaea.entities.Categoria;
import com.example.chaea.entities.Opcion;
import com.example.chaea.entities.Pregunta;
import com.example.chaea.repositories.CategoriaRepository;
import com.example.chaea.repositories.CuestionarioRepository;
import com.example.chaea.repositories.OpcionRepository;
import com.example.chaea.repositories.PreguntaRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class OpcionService {
    
    @Autowired
    private CuestionarioRepository cuestionarioRepository;
    
    @Autowired
    private CategoriaRepository categoriaRepository;
    
    @Autowired
    private PreguntaRepository preguntaRepository;
    
    @Autowired
    private OpcionRepository opcionRepository;
    
    public Opcion crearOpcion(Long idPregunta, Long idCategoria, String respuesta, int orden, Double valor) {
        Pregunta pregunta = preguntaRepository.findById(idPregunta)
                .orElseThrow(() -> new EntityNotFoundException("Pregunta no encontrada con id " + idPregunta));
        
        Categoria categoria = categoriaRepository.findById(idCategoria)
                .orElseThrow(() -> new EntityNotFoundException("Categoria no encontrada con id " + idCategoria));
        
        if (pregunta.getCuestionario().getId().equals(categoria.getCuestionario().getId())) {
            Opcion opcion = new Opcion();
            opcion.setOrden(orden);
            opcion.setPregunta(pregunta);
            opcion.setCategoria(categoria);
            opcion.setValor(valor);
            opcion.setRespuesta(respuesta);
            
            return opcionRepository.save(opcion);
        }
        throw new RuntimeException("Inconsistencias en los cuestionarios de pregunta ("
                + pregunta.getCuestionario().getId() + ") y categoria(" + categoria.getCuestionario().getId() + ")");
    }
    
    public Opcion crearOpcion(Pregunta pregunta, Categoria categoria, OpcionDTO opcionDTO) {
        if (pregunta.getCuestionario().getId().equals(categoria.getCuestionario().getId())) {
            Opcion opcion = new Opcion();
            opcion.setPregunta(pregunta);
            opcion.setCategoria(categoria);
            opcion.setValor(opcionDTO.getValor());
            opcion.setOrden(opcionDTO.getOrden());
            opcion.setRespuesta(opcionDTO.getRespuesta());

            return opcionRepository.save(opcion);
        }
        throw new RuntimeException("Inconsistencias en los cuestionarios de pregunta ("
                + pregunta.getCuestionario().getId() + ") y categoria(" + categoria.getCuestionario().getId() + ")");
    }
}
