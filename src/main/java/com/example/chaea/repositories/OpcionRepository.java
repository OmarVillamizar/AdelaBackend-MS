package com.example.chaea.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.chaea.entities.Opcion;
import com.example.chaea.entities.OpcionId;

public interface OpcionRepository extends JpaRepository<Opcion, OpcionId> {
}

