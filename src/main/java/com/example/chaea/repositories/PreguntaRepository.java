package com.example.chaea.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.chaea.entities.Cuestionario;
import com.example.chaea.entities.Pregunta;

public interface PreguntaRepository extends JpaRepository<Pregunta, Long> {
    List<Pregunta> findByCuestionario(Cuestionario c);
}
