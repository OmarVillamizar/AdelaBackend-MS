package com.example.chaea.controllers;

import java.io.IOException;
import java.sql.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.chaea.entities.Estudiante;
import com.example.chaea.entities.Profesor;
import com.example.chaea.entities.Usuario;
import com.example.chaea.repositories.RolRepository;
import com.example.chaea.repositories.UsuarioRepository;
import com.example.chaea.util.JwtUtil;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private RolRepository rolRepository;
    
    @GetMapping("/")
    public String index() {
        return "index"; // Return index.html
    }
    
    
    @GetMapping("/login/success/estud")
    public String loginSuccessEstudiante(OAuth2AuthenticationToken authentication) throws IOException {
        String email = authentication.getPrincipal().getAttribute("email");
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        Usuario usuario;
        if (usuarioOpt.isEmpty()) {
            // Si el usuario no existe, lo creamos con el rol por defecto
            Estudiante newUsuario = new Estudiante();
            newUsuario.setEmail(email);
            newUsuario.setCodigo("sas");
            newUsuario.setGenero((byte) 3);
            newUsuario.setFecha_nacimiento(new Date(System.currentTimeMillis()));
            newUsuario.setNombre(authentication.getPrincipal().getAttribute("name"));
            usuarioRepository.save(newUsuario);
            usuario = newUsuario;
        } else {
            usuario = usuarioOpt.get();
        }
        
        String token = jwtUtil.generateToken(usuario);
        return token;
    }
    
    @GetMapping("/login/success/prof")
    public String loginSuccessProfesor(OAuth2AuthenticationToken authentication) throws IOException {
        String email = authentication.getPrincipal().getAttribute("email");
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        Usuario usuario;
        if (usuarioOpt.isEmpty()) {
            // Si el usuario no existe, lo creamos con el rol por defecto
            Profesor newUsuario = new Profesor();
            newUsuario.setEmail(email);
            newUsuario.setCodigo("sas");
            newUsuario.setCarrera("sistemas");
            newUsuario.setEstado("ok");
            newUsuario.setRol(rolRepository.findById(1).get());
            newUsuario.setNombre(authentication.getPrincipal().getAttribute("name"));
            usuarioRepository.save(newUsuario);
            usuario = newUsuario;
        } else {
            usuario = usuarioOpt.get();
        }
        
        String token = jwtUtil.generateToken(usuario);
        return token;
    }
    
}