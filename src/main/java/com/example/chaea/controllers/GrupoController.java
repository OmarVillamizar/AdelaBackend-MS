package com.example.chaea.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.chaea.dto.GrupoDTO;
import com.example.chaea.entities.Estudiante;
import com.example.chaea.entities.Grupo;
import com.example.chaea.entities.Profesor;
import com.example.chaea.repositories.EstudianteRepository;
import com.example.chaea.repositories.GrupoRepository;
import com.example.chaea.repositories.ProfesorRepository;

import java.util.*;

@RestController
@RequestMapping("/api/grupos")
public class GrupoController {

    @Autowired
    private GrupoRepository grupoRepository;

    @Autowired
    private ProfesorRepository profesorRepository;

    @Autowired
    private EstudianteRepository estudianteRepository;

    @PostMapping
    public Grupo crearGrupo(@RequestBody GrupoDTO grupoDTO) {
        // Buscar el profesor por su correo electrónico
        Optional<Profesor> profesorOpt = profesorRepository.findById(grupoDTO.getProfesorEmail());

        if (!profesorOpt.isPresent()) {
            throw new IllegalArgumentException("Profesor no encontrado con el correo: " + grupoDTO.getProfesorEmail());
        }

        // Crear una nueva instancia de Grupo
        Grupo nuevoGrupo = new Grupo();
        nuevoGrupo.setNombre(grupoDTO.getNombre());
        nuevoGrupo.setProfesor(profesorOpt.get());

        // Guardar el nuevo grupo antes de asociar los estudiantes
        nuevoGrupo = grupoRepository.save(nuevoGrupo);

        // Crear un conjunto de estudiantes
        Set<Estudiante> estudiantesAsignados = new HashSet<>();
        for (String email : grupoDTO.getCorreosEstudiantes()) {
            Optional<Estudiante> estudianteOpt = estudianteRepository.findById(email);

            if (estudianteOpt.isPresent()) {
                Estudiante estudiante = estudianteOpt.get();
                estudiantesAsignados.add(estudiante);
                // Añadir el grupo al estudiante
                estudiante.getGrupos().add(nuevoGrupo);
                estudianteRepository.save(estudiante); // Actualizar el estudiante
            } else {
                throw new IllegalArgumentException("Estudiante no encontrado con el correo: " + email);
            }
        }

        // Asignar los estudiantes al grupo
        nuevoGrupo.setEstudiantes(estudiantesAsignados);

        // Actualizar el grupo con los estudiantes
        return grupoRepository.save(nuevoGrupo);
    }

    @GetMapping
    public List<Grupo> listarGrupos() {
        return grupoRepository.findAll();
    }

    @GetMapping("/{id}")
    public Grupo consultarGrupoPorId(@PathVariable int id) {
        return grupoRepository.findById(id).orElse(null);
    }

    @DeleteMapping("/{id}")
    public void eliminarGrupo(@PathVariable int id) {
        Optional<Grupo> grupoOpt = grupoRepository.findById(id);
        
        if (grupoOpt.isPresent()) {
            Grupo grupo = grupoOpt.get();
            // Desvincular los estudiantes del grupo
            for (Estudiante estudiante : grupo.getEstudiantes()) {
                estudiante.getGrupos().remove(grupo);
                estudianteRepository.save(estudiante); // Actualizar el estudiante
            }
            // Ahora se puede eliminar el grupo
            grupoRepository.deleteById(id);
        }
    }

    @PutMapping("/{id}")
    public Grupo actualizarGrupo(@PathVariable int id, @RequestBody GrupoDTO grupoDTO) {
        Optional<Grupo> grupoOptional = grupoRepository.findById(id);

        if (grupoOptional.isPresent()) {
            Grupo grupoExistente = grupoOptional.get();
            grupoExistente.setNombre(grupoDTO.getNombre());

            // Actualizar el profesor si es necesario
            Optional<Profesor> profesorOpt = profesorRepository.findById(grupoDTO.getProfesorEmail());
            if (profesorOpt.isPresent()) {
                grupoExistente.setProfesor(profesorOpt.get());
            }

            // Actualizar los estudiantes
            Set<Estudiante> estudiantesAsignados = new HashSet<>();
            for (String email : grupoDTO.getCorreosEstudiantes()) {
                Optional<Estudiante> estudianteOpt = estudianteRepository.findById(email);

                if (estudianteOpt.isPresent()) {
                    Estudiante estudiante = estudianteOpt.get();
                    estudiantesAsignados.add(estudiante);
                    estudiante.getGrupos().add(grupoExistente);
                    estudianteRepository.save(estudiante); // Actualizar el estudiante
                } else {
                    throw new IllegalArgumentException("Estudiante no encontrado con el correo: " + email);
                }
            }
            grupoExistente.setEstudiantes(estudiantesAsignados);
            return grupoRepository.save(grupoExistente);
        }
        return null;
    }
}