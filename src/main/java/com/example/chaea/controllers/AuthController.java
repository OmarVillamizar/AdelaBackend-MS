package com.example.chaea.controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @GetMapping("/")
    public String index() {
        return "index"; // Return index.html
    }

    @GetMapping("/login")
    public String login() {
        return "juadjuad"; // Return login.html
    }

    @GetMapping("/home")
    public ResponseEntity<?> home(OAuth2AuthenticationToken authentication, Model model) {
        model.addAttribute("user", authentication.getPrincipal().getAttributes());
        
        String email = authentication.getPrincipal().getAttribute("email");
        
        if(!email.endsWith("@ufps.edu.co")) {
            return ResponseEntity.badRequest().body(String.format("El correo %s no es válido, debe ser institucional de la UFPS", email));
        }
        
        return ResponseEntity.ok().body(model); // Return home.html
    }

}