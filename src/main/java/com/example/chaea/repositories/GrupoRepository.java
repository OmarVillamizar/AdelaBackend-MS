package com.example.chaea.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.chaea.entities.Grupo;
import com.example.chaea.entities.Profesor;

@Repository
public interface GrupoRepository extends JpaRepository<Grupo, Integer> {
    List<Grupo> findByNombre(String nombre);
    
    List<Grupo> findByProfesor(Profesor profesor);
    
    Optional<Grupo> findByProfesorAndId(Profesor profesor, int id);
}
