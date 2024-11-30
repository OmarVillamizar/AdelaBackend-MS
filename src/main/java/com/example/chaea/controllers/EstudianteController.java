package com.example.chaea.controllers;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.chaea.dto.EstudianteDTO;
import com.example.chaea.entities.Estudiante;
import com.example.chaea.entities.UsuarioEstado;
import com.example.chaea.repositories.EstudianteRepository;

@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteController {
    
    @Autowired
    private EstudianteRepository estudianteRepository;
    
    // Expresión regular para validar correos electrónicos
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@ufps.edu.co$");
    
    @GetMapping("/omero")
    @PreAuthorize("hasRole('ESTUDIANTE')")
    public String ola() {
        return "Hola";
    }
    
    /*
     * No puedo crear estudiantes siendo profesor /
     * 
     * @PostMapping
     * 
     * @PreAuthorize("hasRole('PROFESOR') or hasRole('ADMINISTRADOR')") public
     * ResponseEntity<?> crearEstudiante(@RequestBody EstudianteDTO estudianteDTO) {
     * // Validar campos requeridos if (estudianteDTO.getEmail() == null ||
     * estudianteDTO.getNombre() == null || estudianteDTO.getCodigo() == null) {
     * return ResponseEntity.status(HttpStatus.BAD_REQUEST).
     * body("Faltan campos requeridos."); } // Validar formato de correo electrónico
     * if (!EMAIL_PATTERN.matcher(estudianteDTO.getEmail()).matches()) { return
     * ResponseEntity.status(HttpStatus.BAD_REQUEST)
     * .body("Formato de correo incorrecto: " + estudianteDTO.getEmail()); } //
     * Verificar si el correo ya existe if
     * (estudianteRepository.existsById(estudianteDTO.getEmail())) { return
     * ResponseEntity.status(HttpStatus.CONFLICT) .body("Estudiante con el correo "
     * + estudianteDTO.getEmail() + " ya existe."); }
     * 
     * Estudiante estudiante = new Estudiante();
     * estudiante.setCodigo(estudianteDTO.getCodigo());
     * estudiante.setEmail(estudianteDTO.getEmail());
     * estudiante.setNombre(estudianteDTO.getNombre());
     * estudiante.setEstado(UsuarioEstado.INCOMPLETA); return
     * ResponseEntity.ok(estudianteRepository.save(estudiante)); }
     */
    @GetMapping
    @PreAuthorize("hasRole('PROFESOR') or hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<Estudiante>> listarEstudiantes() {
        return ResponseEntity.ok(estudianteRepository.findAll());
    }
    
    @GetMapping("/{email}")
    @PreAuthorize("hasRole('PROFESOR') or hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> consultarPorCorreo(@PathVariable String email) {
        // Validar formato de correo electrónico
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Formato de correo incorrecto: " + email);
        }
        
        Optional<Estudiante> estudianteOptional = estudianteRepository.findById(email);
        if (!estudianteOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Estudiante no encontrado con el correo: " + email);
        }
        
        return ResponseEntity.ok(estudianteOptional.get());
    }
    
    /*
     * @DeleteMapping("/{email}") public ResponseEntity<?>
     * eliminarEstudiante(@PathVariable String email) { // Validar formato de correo
     * electrónico if (!EMAIL_PATTERN.matcher(email).matches()) { return
     * ResponseEntity.status(HttpStatus.BAD_REQUEST).
     * body("Formato de correo incorrecto: " + email); }
     * 
     * Optional<Estudiante> estudianteOptional =
     * estudianteRepository.findById(email); if (!estudianteOptional.isPresent()) {
     * return ResponseEntity.status(HttpStatus.NOT_FOUND).
     * body("Estudiante no encontrado con el correo: " + email); }
     * 
     * estudianteRepository.deleteById(email); return
     * ResponseEntity.ok().body("Estudiante eliminado exitosamente."); }
     */
    
    @PutMapping
    @PreAuthorize("hasRole('ESTUDIANTE') or hasRole('ESTUDIANTE_INCOMPLETO')")
    public ResponseEntity<?> actualizarEstudiante(@RequestBody EstudianteDTO estudianteDTO) {
        // Validar formato de correo electrónico
        Estudiante estud = (Estudiante) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        String email = estud.getEmail();
        
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Formato de correo incorrecto: " + email);
        }
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