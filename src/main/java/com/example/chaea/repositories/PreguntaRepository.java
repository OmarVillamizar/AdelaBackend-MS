package com.example.chaea.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.chaea.entities.Pregunta;
import com.example.chaea.entities.PreguntaId;

public interface PreguntaRepository extends JpaRepository<Pregunta, PreguntaId> {
}

