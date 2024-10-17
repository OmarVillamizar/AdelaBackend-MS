package com.example.chaea.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.chaea.repositories.RolRepository;
import com.example.chaea.repositories.UsuarioRepository;
import com.example.chaea.security.JwtUtil;

@RestController
@RequestMapping("/test")
public class TestController {
    
    private static final Logger logger = LoggerFactory.getLogger(TestController.class);
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private RolRepository rolRepository;
    
    @GetMapping("/test")
    @PreAuthorize("hasRole('ESTUDIANTE')")
    public String test() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        logger.info("entered test "+auth.getAuthorities().toString());
        return "test"; // Return index.html
    }
    
    
    
}