package com.example.chaea.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.chaea.entities.Cuestionario;
import com.example.chaea.entities.Pregunta;
import com.example.chaea.entities.PreguntaId;
import com.example.chaea.repositories.CuestionarioRepository;
import com.example.chaea.repositories.PreguntaRepository;

@Service
public class PreguntaService {

    @Autowired
    private PreguntaRepository preguntaRepository;

    @Autowired
    private CuestionarioRepository cuestionarioRepository;

    public Pregunta crearPregunta(UUID cuestionarioId, String textoPregunta) {
        // Buscar el cuestionario
        Cuestionario cuestionario = cuestionarioRepository.findById(cuestionarioId)
            .orElseThrow(() -> new RuntimeException("Cuestionario no encontrado"));

        // Crear la clave compuesta con UUID
        PreguntaId preguntaId = new PreguntaId(cuestionarioId, UUID.randomUUID());

        Pregunta pregunta = new Pregunta();
        pregunta.setId(preguntaId);
        pregunta.setCuestionario(cuestionario);
        pregunta.setTexto(textoPregunta);

        return preguntaRepository.save(pregunta);
    }
}
