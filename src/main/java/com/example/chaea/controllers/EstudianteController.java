package com.example.chaea.controllers;

import com.example.chaea.entities.Estudiante;
import com.example.chaea.repositories.EstudianteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteController {

    @Autowired
    private EstudianteRepository estudianteRepository;

    // Método para listar todos los estudiantes
    @GetMapping
    public List<Estudiante> listarEstudiantes() {
        return estudianteRepository.findAll();
    }

    // Método para crear un nuevo estudiante
    @PostMapping
    public ResponseEntity<Estudiante> crearEstudiante(@RequestBody Estudiante estudiante) {
        Estudiante nuevoEstudiante = estudianteRepository.save(estudiante);
        return ResponseEntity.ok(nuevoEstudiante);
    }

    // Método para eliminar un estudiante por su email
    @DeleteMapping("/{email}")
    public ResponseEntity<String> eliminarEstudiante(@PathVariable String email) {
        Optional<Estudiante> estudianteOpt = estudianteRepository.findById(email);
        if (estudianteOpt.isPresent()) {
            estudianteRepository.delete(estudianteOpt.get());
            return ResponseEntity.ok("Estudiante eliminado con éxito.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/email/{email}")
    public ResponseEntity<Estudiante> actualizarEstudiantePorEmail(
            @PathVariable("email") String email,
            @RequestBody Estudiante estudianteActualizado) {
        
        Optional<Estudiante> estudianteOptional = estudianteRepository.findById(email);
        
        if (estudianteOptional.isPresent()) {
            Estudiante estudianteExistente = estudianteOptional.get();

            // Actualizamos solo los campos permitidos (sin el email)
            estudianteExistente.setNombre(estudianteActualizado.getNombre());
            estudianteExistente.setGenero(estudianteActualizado.getGenero());
            estudianteExistente.setFecha_nacimiento(estudianteActualizado.getFecha_nacimiento());
            estudianteExistente.setEstado(estudianteActualizado.getEstado());

            estudianteRepository.save(estudianteExistente);
            return ResponseEntity.ok(estudianteExistente);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // ================== NUEVOS MÉTODOS USANDO CÓDIGO ===================

    // Método para eliminar un estudiante por su código
    @DeleteMapping("/codigo/{codigo}")
    public ResponseEntity<String> eliminarEstudiantePorCodigo(@PathVariable String codigo) {
        Optional<Estudiante> estudianteOpt = estudianteRepository.findByCodigo(codigo);
        if (estudianteOpt.isPresent()) {
            estudianteRepository.delete(estudianteOpt.get());
            return ResponseEntity.ok("Estudiante con código " + codigo + " eliminado con éxito.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/codigo/{codigo}")
    public ResponseEntity<Estudiante> actualizarEstudiantePorCodigo(
            @PathVariable("codigo") String codigo,
            @RequestBody Estudiante estudianteActualizado) {
        
        Optional<Estudiante> estudianteOptional = estudianteRepository.findByCodigo(codigo);
        
        if (estudianteOptional.isPresent()) {
            Estudiante estudianteExistente = estudianteOptional.get();

            // Actualizamos solo los campos permitidos (sin el código)
            estudianteExistente.setNombre(estudianteActualizado.getNombre());
            estudianteExistente.setGenero(estudianteActualizado.getGenero());
            estudianteExistente.setFecha_nacimiento(estudianteActualizado.getFecha_nacimiento());
            estudianteExistente.setEstado(estudianteActualizado.getEstado());

            estudianteRepository.save(estudianteExistente);
            return ResponseEntity.ok(estudianteExistente);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
