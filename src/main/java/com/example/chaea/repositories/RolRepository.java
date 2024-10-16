package com.example.chaea.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.chaea.entities.Rol;

public interface RolRepository extends JpaRepository<Rol, Integer> {
    
}
