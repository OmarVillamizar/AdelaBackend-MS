package com.example.chaea.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/test")
public class TestController {
    
    private static final Logger logger = LoggerFactory.getLogger(TestController.class);
    
    @GetMapping("/test/est")
    @PreAuthorize("hasRole('ESTUDIANTE')")
    public String test() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        logger.info("entered test "+auth.getAuthorities().toString());
        return "estudiante"; // Return index.html
    }
    
    @GetMapping("/test/prof")
    @PreAuthorize("hasRole('PROFESOR') or hasRole('ADMINISTRADOR')")
    public String testProf() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        logger.info("entered test "+auth.getAuthorities().toString());
        return "profe"; // Return index.html
    }
    
    @GetMapping("/test/admin")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public String testAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        logger.info("entered test "+auth.getAuthorities().toString());
        return "admin"; // Return index.html
    }
    
}