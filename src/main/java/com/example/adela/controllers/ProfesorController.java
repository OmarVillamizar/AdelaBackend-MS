package com.example.adela.controllers;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.adela.dto.ProfesorDTO;
import com.example.adela.entities.Profesor;
import com.example.adela.entities.ProfesorEstado;
import com.example.adela.entities.Usuario;
import com.example.adela.entities.UsuarioEstado;
import com.example.adela.repositories.ProfesorRepository;
import com.example.adela.repositories.RolRepository;
import com.example.adela.repositories.UsuarioRepository;

@RestController
@RequestMapping("/ms-auth/profesores")
public class ProfesorController {
    
    @Autowired
    private ProfesorRepository profesorRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private RolRepository rolRepository;
        

    
    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<Profesor>> listarProfesores() {
        return ResponseEntity.ok(profesorRepository.findAll());
    }
    
    @GetMapping("/{email}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> consultarPorCorreo(@PathVariable String email) {
        Optional<Profesor> profesorOptional = profesorRepository.findById(email);
        if (!profesorOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Profesor no encontrado con el correo: " + email);
        }
        return ResponseEntity.ok(profesorOptional.get());
    }
    
    @DeleteMapping("/deactivate/{email}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> eliminarProfesor(@PathVariable String email) {
        Optional<Profesor> profesorOptional = profesorRepository.findById(email);
        if (!profesorOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Profesor no encontrado con el correo: " + email);
        }
        Profesor profesor = profesorOptional.get();
        
        profesor.setEstado(UsuarioEstado.INACTIVA);
        return ResponseEntity.ok().body("Profesor eliminado exitosamente.");
    }
    
    @PutMapping("/activate/{email}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> activarCuentaProfesor(@PathVariable String email) {
        Optional<Profesor> profesorOptional = profesorRepository.findById(email);
        if (!profesorOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Profesor no encontrado con el correo: " + email);
        }
        Profesor profesor = profesorOptional.get();
        
        if (profesor.getEstadoProfesor() == ProfesorEstado.ACTIVA) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("La cuenta de profesor " + profesor.getEmail() + " ya está activa");
        }
        
        profesor.setEstadoProfesor(ProfesorEstado.ACTIVA);
        profesor.setRol(rolRepository.findByDescripcion("PROFESOR").get());
        return ResponseEntity.ok(profesorRepository.save(profesor));
    }
    
    @PutMapping("/elevate/{email}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> elevarCuentaProfesor(@PathVariable String email) {
        Optional<Profesor> profesorOptional = profesorRepository.findById(email);
        if (!profesorOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Profesor no encontrado con el correo: " + email);
        }
        
        Profesor profesor = profesorOptional.get();
        
        if (profesor.getEstado() != UsuarioEstado.ACTIVA) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Cuenta de profesor no está activa, no se puede hacer administrador: " + email);
        }
        
        profesor.setRol(rolRepository.findByDescripcion("ADMINISTRADOR").get());
        return ResponseEntity.ok(profesorRepository.save(profesor));
    }
    
    @PutMapping("/demote/{email}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> bajarCuentaProfesor(@PathVariable String email) {
        Optional<Profesor> profesorOptional = profesorRepository.findById(email);
        if (!profesorOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Profesor no encontrado con el correo: " + email);
        }
        
        Profesor profesor = profesorOptional.get();
        Profesor prof = (Profesor) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        if (profesor.getEmail().equalsIgnoreCase(prof.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("No puede quitarse derechos de administrador a usted mismo.");
        }
        
        if (profesor.getEstado() != UsuarioEstado.ACTIVA) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Cuenta de profesor no está activa, no se puede hacer administrador: " + email);
        }
        
        profesor.setRol(rolRepository.findByDescripcion("PROFESOR").get());
        return ResponseEntity.ok(profesorRepository.save(profesor));
    }
    
    @DeleteMapping("/reject/{email}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> rechazarSolicitudCuentaProfesor(@PathVariable String email) {
        Optional<Profesor> profesorOptional = profesorRepository.findById(email);
        if (!profesorOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Profesor no encontrado con el correo: " + email);
        }
        
        Profesor profesor = profesorOptional.get();
        
        if (profesor.getEstado() != UsuarioEstado.INACTIVA) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Cuenta de profesor activa, no se puede rechazar: " + email);
        }
        profesorRepository.delete(profesor);
        return ResponseEntity.ok("Soliciutd de profesor rechazada");
    }
    
    @PutMapping
    @PreAuthorize("hasRole('PROFESOR') or hasRole('PROFESOR_INCOMPLETO') or hasRole('PROFESOR_INACTIVO') or hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> actualizarProfesor(@RequestBody ProfesorDTO profesorDTO) {
        // Validar formato de correo electrónico
        Profesor prof = (Profesor) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        String email = prof.getEmail();
        
        List<String> errores = new LinkedList<String>();
        if (profesorDTO.getCodigo() == null) {
            errores.add("codigo");
        }
        if (profesorDTO.getCarrera() == null) {
            errores.add("carrera");
        }
        
        if (errores.size() > 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Profesor presenta errores en los siguientes campos: " + errores.toString());
        }
        Optional<Profesor> profesorOptional = profesorRepository.findById(email);
        if (!profesorOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Profesor : " + email);
        }
        Optional<Usuario> existente = usuarioRepository.findByCodigo(profesorDTO.getCodigo());
        if (existente.isPresent() && !existente.get().getEmail().equals(email)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Ya hay una usuario registrado con el codigo: " + profesorDTO.getCodigo());
        }
        
        Profesor profesorExistente = profesorOptional.get();
        profesorExistente.setCarrera(profesorDTO.getCarrera());
        profesorExistente.setCodigo(profesorDTO.getCodigo());
        profesorExistente.setEstado(UsuarioEstado.ACTIVA);
        
        return ResponseEntity.ok(profesorRepository.save(profesorExistente));
    }
    
}