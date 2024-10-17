package com.example.chaea.controllers;

import java.io.IOException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.chaea.entities.Estudiante;
import com.example.chaea.entities.Profesor;
import com.example.chaea.entities.ProfesorEstado;
import com.example.chaea.entities.Usuario;
import com.example.chaea.entities.UsuarioEstado;
import com.example.chaea.repositories.RolRepository;
import com.example.chaea.repositories.UsuarioRepository;
import com.example.chaea.security.JwtUtil;

@RestController
@RequestMapping("/auth")
public class AuthController {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    
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
            newUsuario.setEstado(UsuarioEstado.INCOMPLETA);
            newUsuario.setNombre(authentication.getPrincipal().getAttribute("name"));
            usuarioRepository.save(newUsuario);
            usuario = newUsuario;
        } else {
            logger.info("User found: "+email);
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
            newUsuario.setEstado(UsuarioEstado.INCOMPLETA);
            newUsuario.setEstadoProfesor(ProfesorEstado.INACTIVA);
            usuarioRepository.save(newUsuario);
            usuario = newUsuario;
        } else {
            usuario = usuarioOpt.get();
        }
        
        String token = jwtUtil.generateToken(usuario);
        return token;
    }
    
}