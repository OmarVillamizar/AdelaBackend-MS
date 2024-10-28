package com.example.chaea.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.chaea.entities.Pregunta;

public interface PreguntaRepository extends JpaRepository<Pregunta, Long> {
    
}
