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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.chaea.dto.EstudianteDTO;
import com.example.chaea.dto.ProfesorDTO;
import com.example.chaea.entities.Estudiante;
import com.example.chaea.entities.Profesor;
import com.example.chaea.entities.ProfesorEstado;
import com.example.chaea.entities.Rol;
import com.example.chaea.entities.UsuarioEstado;
import com.example.chaea.repositories.ProfesorRepository;
import com.example.chaea.repositories.RolRepository;

@RestController
@RequestMapping("/api/profesores")
public class ProfesorController {
    
    @Autowired
    private ProfesorRepository profesorRepository;
    
    @Autowired
    private RolRepository rolRepository;
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@ufps.edu.co$");
    
    /*
     * No debería de poder crear profesores, ya que estos se registran por el camino
     * de autenticación
     * 
     * @PostMapping public ResponseEntity<?> crearProfesor(@RequestBody ProfesorDTO
     * profesorDTO) { // Validar campos requeridos if (profesorDTO.getEmail() ==
     * null || profesorDTO.getNombre() == null || profesorDTO.getCodigo() == null ||
     * profesorDTO.getCarrera() == null || profesorDTO.getEstado() == null ||
     * profesorDTO.getEstadoProfesor() == null || profesorDTO.getRol() == null) {
     * return ResponseEntity.status(HttpStatus.BAD_REQUEST).
     * body("Faltan campos requeridos."); } // Validar formato de correo electrónico
     * if (!EMAIL_PATTERN.matcher(profesorDTO.getEmail()).matches()) { return
     * ResponseEntity.status(HttpStatus.BAD_REQUEST).
     * body("Formato de correo incorrecto: " + profesorDTO.getEmail()); } //
     * Verificar si el correo ya existe if
     * (profesorRepository.existsById(profesorDTO.getEmail())) { return
     * ResponseEntity.status(HttpStatus.CONFLICT).body("Profesor con el correo " +
     * profesorDTO.getEmail() + " ya existe."); }
     * 
     * Optional<Rol> rolOpt = rolRepository.findByDescripcion(profesorDTO.getRol());
     * 
     * if(rolOpt.isEmpty()) { return ResponseEntity.status(HttpStatus.BAD_REQUEST).
     * body("Rol de profesor inválido."); }
     * 
     * Rol rol = rolOpt.get(); Profesor profesor = new Profesor(
     * profesorDTO.getEmail(), profesorDTO.getNombre(), profesorDTO.getCodigo(),
     * profesorDTO.getCarrera(), profesorDTO.getEstado(),
     * profesorDTO.getEstadoProfesor() ); profesor.setRol(rol); return
     * ResponseEntity.ok(profesorRepository.save(profesor)); }
     */
    
    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<Profesor>> listarProfesores() {
        return ResponseEntity.ok(profesorRepository.findAll());
    }
    
    @GetMapping("/{email}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> consultarPorCorreo(@PathVariable String email) {
        // Validar formato de correo electrónico
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Formato de correo incorrecto: " + email);
        }
        Optional<Profesor> profesorOptional = profesorRepository.findById(email);
        if (!profesorOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Profesor no encontrado con el correo: " + email);
        }
        return ResponseEntity.ok(profesorOptional.get());
    }
    
    @DeleteMapping("/deactivate/{email}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> eliminarProfesor(@PathVariable String email) {
        // Validar formato de correo electrónico
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Formato de correo incorrecto: " + email);
        }
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
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Formato de correo incorrecto: " + email);
        }
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
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Formato de correo incorrecto: " + email);
        }
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
    
    @DeleteMapping("/reject/{email}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> rechazarSolicitudCuentaProfesor(@PathVariable String email) {
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Formato de correo incorrecto: " + email);
        }
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
    @PreAuthorize("hasRole('PROFESOR') or hasRole('PROFESOR_INACTIVO') or hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> actualizarProfesor(@RequestBody ProfesorDTO profesorDTO) {
        // Validar formato de correo electrónico
        Profesor prof = (Profesor) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        String email = prof.getEmail();
        
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Formato de correo incorrecto: " + email);
        }
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
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Profesor no encontrado con el correo: " + email);
        }
        
        Profesor profesorExistente = profesorOptional.get();
        profesorExistente.setCarrera(profesorDTO.getCarrera());
        profesorExistente.setCodigo(profesorDTO.getCodigo());
        profesorExistente.setEstado(UsuarioEstado.ACTIVA);
        
        return ResponseEntity.ok(profesorRepository.save(profesorExistente));
    }
    
}