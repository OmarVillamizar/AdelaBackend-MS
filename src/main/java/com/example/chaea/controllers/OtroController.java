package com.example.chaea.controllers;

import java.sql.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.chaea.entities.Estudiante;
import com.example.chaea.entities.Usuario;
import com.example.chaea.repositories.UsuarioRepository;
import com.example.chaea.util.JwtUtil;

@RestController
@RequestMapping("/login")
public class OtroController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/success")
    public String loginSuccess(OAuth2AuthenticationToken authentication) {
        String email = authentication.getPrincipal().getAttribute("email");
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        Usuario usuario;
        if (usuarioOpt.isEmpty()) {
            // Si el usuario no existe, lo creamos con el rol por defecto
            Estudiante newUsuario = new Estudiante();
            newUsuario.setEmail(email);
            newUsuario.setCodigo("sas");
            newUsuario.setGenero((byte)3);
            newUsuario.setFecha_nacimiento(new Date(System.currentTimeMillis()));
            newUsuario.setNombre(authentication.getPrincipal().getAttribute("name"));
            usuarioRepository.save(newUsuario);
            usuario = newUsuario;
        }else {
            usuario = usuarioOpt.get();
        }

        String token = jwtUtil.generateToken(usuario);
        return token;
    }
}