package com.example.adela.controllers;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.adela.dto.EstudianteCrearDTO;
import com.example.adela.dto.EstudianteDTO;
import com.example.adela.entities.Estudiante;
import com.example.adela.entities.UsuarioEstado;
import com.example.adela.repositories.EstudianteRepository;

@RestController
@RequestMapping("/ms-auth/estudiantes")
public class EstudianteController {
    
    @Autowired
    private EstudianteRepository estudianteRepository;
        
    @GetMapping
    @PreAuthorize("hasRole('PROFESOR') or hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<Estudiante>> listarEstudiantes() {
        return ResponseEntity.ok(estudianteRepository.findAll());
    }
    
    @GetMapping("/{email}")
    @PreAuthorize("hasRole('PROFESOR') or hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> consultarPorCorreo(@PathVariable String email) {
        
        Optional<Estudiante> estudianteOptional = estudianteRepository.findById(email);
        if (!estudianteOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Estudiante no encontrado con el correo: " + email);
        }
        
        return ResponseEntity.ok(estudianteOptional.get());
    }
    
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> crearCascaras(@RequestBody List<EstudianteCrearDTO> estudiantesDto) {
		List<Estudiante> estudiantesACrear = new LinkedList<>();
		for (EstudianteCrearDTO estudianteDTO : estudiantesDto) {
			Estudiante nuevoEstudiante = new Estudiante();
			nuevoEstudiante.setEmail(estudianteDTO.getEmail());
			nuevoEstudiante.setNombre(estudianteDTO.getNombre());
			nuevoEstudiante.setEstado(UsuarioEstado.INCOMPLETA);
			estudiantesACrear.add(nuevoEstudiante);
		}
		List<Estudiante> estudiantesCreados = estudianteRepository.saveAll(estudiantesACrear);
		List<EstudianteDTO> dtos = estudiantesCreados.stream().map(est -> EstudianteDTO.from(est))
				.toList();
		return ResponseEntity.status(HttpStatus.CREATED).body(dtos);
    }

    @PutMapping
    @PreAuthorize("hasRole('ESTUDIANTE') or hasRole('ESTUDIANTE_INCOMPLETO')")
    public ResponseEntity<?> actualizarEstudiante(@RequestBody EstudianteDTO estudianteDTO) {
        // Validar formato de correo electrónico
        Estudiante estud = (Estudiante) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        String email = estud.getEmail();

        List<String> errores = new LinkedList<String>();
        if (estudianteDTO.getCodigo() == null) {
            errores.add("codigo");
        }
        if (estudianteDTO.getFechaNacimiento() == null) {
            errores.add("fecha de nacimiento");
        }
        if (estudianteDTO.getGenero() == null) {
            errores.add("genero");
        }
        if (errores.size() > 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Estudiante presenta errores en los siguientes campos: " + errores.toString());
        }
        Optional<Estudiante> estudianteOptional = estudianteRepository.findById(email);
        if (!estudianteOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Estudiante no encontrado con el correo: " + email);
        }
        
        Estudiante estudianteExistente = estudianteOptional.get();
        estudianteExistente.setCodigo(estudianteDTO.getCodigo());
        estudianteExistente.setGenero(estudianteDTO.getGenero());
        estudianteExistente.setFecha_nacimiento(estudianteDTO.getFechaNacimiento());
        estudianteExistente.setEstado(UsuarioEstado.ACTIVA);
        
        return ResponseEntity.ok(estudianteRepository.save(estudianteExistente));
    }
    
}