package com.example.chaea.controllers;
import org.springframework.web.bind.annotation.*;

import com.example.chaea.dto.ProfesorDTO;
import com.example.chaea.entities.Profesor;
import com.example.chaea.entities.Rol;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/profesores")
public class ProfesorController {

    private List<Profesor> profesores = new ArrayList<>();

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
        profesores.add(profesor);
        return profesor;
    }

    @GetMapping
    public List<Profesor> listarProfesores() {
        return profesores;
    }

    @DeleteMapping("/{email}")
    public void eliminarProfesor(@PathVariable String email) {
        profesores.removeIf(profesor -> profesor.getEmail().equals(email));
    }
    
    @GetMapping("/{email}")
    public Profesor consultarPorEmail(@PathVariable String email) {
        Optional<Profesor> profesorOptional = profesores.stream()
                .filter(profesor -> profesor.getEmail().equals(email))
                .findFirst();

        return profesorOptional.orElse(null);
    }

    @PutMapping("/{email}")
    public Profesor actualizarProfesor(@PathVariable String email, @RequestBody ProfesorDTO profesorDTO) {
        Optional<Profesor> profesorOptional = profesores.stream()
                .filter(profesor -> profesor.getEmail().equals(email))
                .findFirst();

        if (profesorOptional.isPresent()) {
            Profesor profesorExistente = profesorOptional.get();
            profesorExistente.setNombre(profesorDTO.getNombre());
            profesorExistente.setCodigo(profesorDTO.getCodigo());
            profesorExistente.setCarrera(profesorDTO.getCarrera());
            profesorExistente.setRol(obtenerRolPorId(profesorDTO.getRolId()));
            profesorExistente.setEstadoProfesor(profesorDTO.getEstadoProfesor());
            profesorExistente.setEstado(profesorDTO.getEstado());
            return profesorExistente;
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