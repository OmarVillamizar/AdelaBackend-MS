package com.example.chaea.controllers;

import java.io.IOException;
import java.util.Optional;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.chaea.clients.UsuarioReferenciaClient;
import com.example.chaea.dto.UsuarioReferenciaRequest;
import com.example.chaea.entities.Estudiante;
import com.example.chaea.entities.Profesor;
import com.example.chaea.entities.ProfesorEstado;
import com.example.chaea.entities.Usuario;
import com.example.chaea.entities.UsuarioEstado;
import com.example.chaea.enums.TipoUsuario;
import com.example.chaea.repositories.UsuarioRepository;
import com.example.chaea.security.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private UsuarioReferenciaClient usuarioReferenciaClient;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @GetMapping("/")
    public String index() {
        return "index"; // Return index.html
    }
        
    @GetMapping("/login/success/estud")
    public void loginSuccessEstudiante(HttpServletResponse response, HttpServletRequest request,
            OAuth2AuthenticationToken authentication, @RequestParam String redirect_to) throws IOException {
        String email = authentication.getPrincipal().getAttribute("email");
        
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        Usuario usuario;
        logger.info("will redirecto to " + redirect_to);
        if (usuarioOpt.isEmpty()) {
            // Si el usuario no existe, lo creamos con el rol por defecto
            Estudiante newUsuario = new Estudiante();
            newUsuario.setEmail(email);
            newUsuario.setEstado(UsuarioEstado.INCOMPLETA);
            newUsuario.setNombre(authentication.getPrincipal().getAttribute("name"));
            usuarioRepository.save(newUsuario);
            usuario = newUsuario;
            
            // 📤 Enviar referencia a MS-Grupos
            UsuarioReferenciaRequest ref = new UsuarioReferenciaRequest();
            ref.setEmail(email);
            ref.setTipoUsuario(TipoUsuario.ESTUDIANTE);
            usuarioReferenciaClient.crearReferencia(ref);
            
        } else {
            usuario = usuarioOpt.get();
            if (!(usuario instanceof Estudiante)) {
                response.sendRedirect(
                        redirect_to + "?error=El usuario " + email + " no pertenece a una cuenta de estudiante");
                return;
            }
            
        }
        String token = jwtUtil.generateToken(usuario);
        response.sendRedirect(redirect_to + "?token=" + token);
    }
    
    @GetMapping("/login/success/prof")
    public void loginSuccessProfesor(OAuth2AuthenticationToken authentication, HttpServletResponse response,
            @RequestParam String redirect_to) throws IOException {
        String email = authentication.getPrincipal().getAttribute("email");
        
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        Usuario usuario;
        if (usuarioOpt.isEmpty()) {
            // Si el usuario no existe, lo creamos con el rol por defecto
            Profesor newUsuario = new Profesor();
            newUsuario.setEmail(email);
            newUsuario.setEstado(UsuarioEstado.INCOMPLETA);
            newUsuario.setEstadoProfesor(ProfesorEstado.INACTIVA);
            newUsuario.setNombre(authentication.getPrincipal().getAttribute("name"));
            newUsuario.setRol(null);
            usuarioRepository.save(newUsuario);
            usuario = newUsuario;
            
            // 📤 Enviar referencia a MS-Grupos
            UsuarioReferenciaRequest ref = new UsuarioReferenciaRequest();
            ref.setEmail(email);
            ref.setTipoUsuario(TipoUsuario.PROFESOR);
            usuarioReferenciaClient.crearReferencia(ref);
           
        } else {
            usuario = usuarioOpt.get();
            if (!(usuario instanceof Profesor)) {
                response.sendRedirect(
                        redirect_to + "?error=El usuario " + email + " no pertenece a una cuenta de profesor");
                return;
            }
        }
        String token = jwtUtil.generateToken(usuario);
        response.sendRedirect(redirect_to + "?token=" + token);
    }
    
}