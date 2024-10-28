package com.example.chaea.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.chaea.entities.Categoria;
import com.example.chaea.entities.Opcion;
import com.example.chaea.entities.Pregunta;
import com.example.chaea.repositories.CategoriaRepository;
import com.example.chaea.repositories.CuestionarioRepository;
import com.example.chaea.repositories.OpcionRepository;
import com.example.chaea.repositories.PreguntaRepository;

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
                .orElseThrow(() -> new RuntimeException("Pregunta no encontrada con id " + idPregunta));
        
        Categoria categoria = categoriaRepository.findById(idCategoria)
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada con id " + idCategoria));
        
        if (pregunta.getCuestionario().getId().equals(categoria.getCuestionario().getId())) {
            Opcion opcion = new Opcion();
            opcion.setOrden(orden);
            opcion.setPregunta(pregunta);
            opcion.setCategoria(categoria);
            opcion.setValor(valor);
            
            return opcionRepository.save(opcion);
        }
        throw new RuntimeException("Inconsistencias en los cuestionarios de pregunta ("
                + pregunta.getCuestionario().getId() + ") y categoria(" + categoria.getCuestionario().getId() + ")");
    }
}
