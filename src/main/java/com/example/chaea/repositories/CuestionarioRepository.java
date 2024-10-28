package com.example.chaea.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.chaea.entities.Cuestionario;

public interface CuestionarioRepository extends JpaRepository<Cuestionario, UUID> {
}

