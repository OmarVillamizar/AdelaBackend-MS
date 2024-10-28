package com.example.chaea.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.chaea.entities.Cuestionario;
import com.example.chaea.entities.Pregunta;
import com.example.chaea.repositories.CuestionarioRepository;
import com.example.chaea.repositories.PreguntaRepository;

@Service
public class PreguntaService {
    
    @Autowired
    private PreguntaRepository preguntaRepository;
    
    @Autowired
    private CuestionarioRepository cuestionarioRepository;
    
    public Pregunta crearPregunta(Long idCuestionario, String preguntaStr, int orden) {
        Cuestionario cuestionario = cuestionarioRepository.findById(idCuestionario)
                .orElseThrow(() -> new RuntimeException("Cuestionario no encontrado con id " + idCuestionario));
        Pregunta pregunta = new Pregunta();
        pregunta.setCuestionario(cuestionario);
        pregunta.setPregunta(preguntaStr);
        pregunta.setOrden(orden);
        
        return preguntaRepository.save(pregunta);
    }
}
