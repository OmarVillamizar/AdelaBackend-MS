package com.example.chaea.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.chaea.entities.Estudiante;

import org.springframework.stereotype.Repository;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, String> {
    
    Optional<Estudiante> findByCodigo(String codigo);
    
}