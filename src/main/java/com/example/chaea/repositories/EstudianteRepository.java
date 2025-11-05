package com.example.chaea.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.chaea.entities.Estudiante;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, String> {
    
    Optional<Estudiante> findByCodigo(String codigo);
    
}