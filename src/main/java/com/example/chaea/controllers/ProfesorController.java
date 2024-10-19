package com.example.chaea.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.chaea.dto.ProfesorDTO;
import com.example.chaea.entities.Profesor;
import com.example.chaea.entities.Rol;
import com.example.chaea.repositories.ProfesorRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/profesores")
public class ProfesorController {

    @Autowired
    private ProfesorRepository profesorRepository;

    @PostMapping
    public Profesor crearProfesor(@RequestBody ProfesorDTO profesorDTO) {
        Rol rol = obtenerRolPorId(profesorDTO.getRolId());
        Profesor profesor = new Profesor(
            profesorDTO.getEmail(),
            profesorDTO.getNombre(),
            profesorDTO.getCodigo(),
            profesorDTO.getCarrera(),
            profesorDTO.getEstado(),
            profesorDTO.getEstadoProfesor()
        );
        profesor.setRol(rol);
        return profesorRepository.save(profesor);
    }

    @GetMapping
    public List<Profesor> listarProfesores() {
        return profesorRepository.findAll();
    }

    @GetMapping("/{email}")
    public Profesor consultarPorCorreo(@PathVariable String email) {
        Optional<Profesor> profesorOptional = profesorRepository.findById(email);
        return profesorOptional.orElse(null);
    }

    @DeleteMapping("/{email}")
    public void eliminarProfesor(@PathVariable String email) {
        profesorRepository.deleteById(email);
    }

    @PutMapping("/{email}")
    public Profesor actualizarProfesor(@PathVariable String email, @RequestBody ProfesorDTO profesorDTO) {
        Optional<Profesor> profesorOptional = profesorRepository.findById(email);
        if (profesorOptional.isPresent()) {
            Profesor profesorExistente = profesorOptional.get();
            profesorExistente.setNombre(profesorDTO.getNombre());
            profesorExistente.setCodigo(profesorDTO.getCodigo());
            profesorExistente.setCarrera(profesorDTO.getCarrera());
            profesorExistente.setRol(obtenerRolPorId(profesorDTO.getRolId()));
            profesorExistente.setEstadoProfesor(profesorDTO.getEstadoProfesor());
            profesorExistente.setEstado(profesorDTO.getEstado());
            return profesorRepository.save(profesorExistente);
        }
        return null;
    }

    private Rol obtenerRolPorId(int rolId) {
        switch (rolId) {
            case 1:
                return new Rol(1, "Profesor Admin");
            case 2:
                return new Rol(2, "Profesor Normal");
            case 3:
                return new Rol(3, "Usuario");
            default:
                throw new IllegalArgumentException("Rol ID inválido: " + rolId);
        }
    }
}