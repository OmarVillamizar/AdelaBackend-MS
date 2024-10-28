package com.example.chaea.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.chaea.entities.Opcion;
import com.example.chaea.entities.OpcionId;
import com.example.chaea.entities.Pregunta;
import com.example.chaea.entities.PreguntaId;
import com.example.chaea.repositories.OpcionRepository;
import com.example.chaea.repositories.PreguntaRepository;

@Service
public class OpcionService {

    @Autowired
    private OpcionRepository opcionRepository;

    @Autowired
    private PreguntaRepository preguntaRepository;

    public Opcion crearOpcion(UUID cuestionarioId, UUID preguntaId, String textoOpcion) {
        // Buscar la pregunta
        PreguntaId preguntaIdCompuesta = new PreguntaId(cuestionarioId, preguntaId);
        Pregunta pregunta = preguntaRepository.findById(preguntaIdCompuesta)
            .orElseThrow(() -> new RuntimeException("Pregunta no encontrada"));

        // Crear la clave compuesta con UUID
        OpcionId opcionId = new OpcionId(cuestionarioId, preguntaId, UUID.randomUUID());

        Opcion opcion = new Opcion();
        opcion.setId(opcionId);
        opcion.setPregunta(pregunta);
        opcion.setTexto(textoOpcion);

        return opcionRepository.save(opcion);
    }
}
