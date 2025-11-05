package com.example.chaea.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.chaea.entities.Profesor;
import com.example.chaea.entities.Rol;

import java.util.Optional;

public interface ProfesorRepository extends JpaRepository<Profesor, String> {
    
    // Método para encontrar un profesor por su código
    Optional<Profesor> findByCodigo(String codigo);
    
    Optional<Rol> findByEmail(String email);
}
