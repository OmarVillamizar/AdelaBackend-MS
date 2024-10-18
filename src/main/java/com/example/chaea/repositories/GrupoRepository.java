package com.example.chaea.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.chaea.entities.Grupo;

@Repository
public interface GrupoRepository extends JpaRepository<Grupo, Integer > {
    
}
