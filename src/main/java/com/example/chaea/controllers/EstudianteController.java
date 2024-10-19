package com.example.chaea.controllers;

import com.example.chaea.dto.EstudianteDTO;
import com.example.chaea.dto.GrupoResumidoDTO;
import com.example.chaea.entities.Estudiante;
import com.example.chaea.entities.Grupo;
import com.example.chaea.repositories.EstudianteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteController {

    @Autowired
    private EstudianteRepository estudianteRepository;

    @PostMapping
    public Estudiante crearEstudiante(@RequestBody EstudianteDTO estudianteDTO) {
        Estudiante estudiante = new Estudiante(
            estudianteDTO.getEmail(),
            estudianteDTO.getNombre(),
            estudianteDTO.getCodigo(),
            estudianteDTO.getGenero(),
            estudianteDTO.getFechaNacimiento(),
            estudianteDTO.getEstado()
        );
        return estudianteRepository.save(estudiante);
    }

    @GetMapping
    public List<Estudiante> listarEstudiantes() {
        return estudianteRepository.findAll();
    }

    @GetMapping("/{email}")
    public Estudiante consultarPorCorreo(@PathVariable String email) {
        Optional<Estudiante> estudianteOptional = estudianteRepository.findById(email);
        return estudianteOptional.orElse(null);
    }

    @DeleteMapping("/{email}")
    public void eliminarEstudiante(@PathVariable String email) {
        estudianteRepository.deleteById(email);
    }

    @PutMapping("/{email}")
    public Estudiante actualizarEstudiante(@PathVariable String email, @RequestBody EstudianteDTO estudianteDTO) {
        Optional<Estudiante> estudianteOptional = estudianteRepository.findById(email);
        if (estudianteOptional.isPresent()) {
            Estudiante estudianteExistente = estudianteOptional.get();
            estudianteExistente.setNombre(estudianteDTO.getNombre());
            estudianteExistente.setCodigo(estudianteDTO.getCodigo());
            estudianteExistente.setGenero(estudianteDTO.getGenero());
            estudianteExistente.setFecha_nacimiento(estudianteDTO.getFechaNacimiento());
            estudianteExistente.setEstado(estudianteDTO.getEstado());
            return estudianteRepository.save(estudianteExistente);
        }
        return null;
    }

    @GetMapping("/{email}/grupos")
    public List<GrupoResumidoDTO> consultarGruposPorCorreo(@PathVariable String email) {
        Optional<Estudiante> estudianteOptional = estudianteRepository.findById(email);
        if (estudianteOptional.isPresent()) {
            Set<Grupo> grupos = estudianteOptional.get().getGrupos();
            return grupos.stream()
                .map(grupo -> new GrupoResumidoDTO(
                    grupo.getId(),
                    grupo.getNombre(),
                    grupo.getProfesor().getNombre(),
                    grupo.getProfesor().getEmail()
                ))
                .collect(Collectors.toList());
        }
        return null;
    }
}