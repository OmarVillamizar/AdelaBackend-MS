package com.example.chaea.entities;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Cuestionario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;
    
    @Column(nullable = false)
    private String descripcion;
    
    @Column(nullable = false)
    private String autor;
    
    @Column(nullable = false)
    private String version;
    
    @Column(nullable = false)
    private String siglas;
    
    @OneToMany(mappedBy = "cuestionario")
    private Set<Pregunta> preguntas = new HashSet<>();

    @OneToMany(mappedBy = "cuestionario")
    private Set<Categoria> categorias = new HashSet<>();
}
