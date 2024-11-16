package com.example.chaea.entities;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
    
    @OneToMany(mappedBy = "cuestionario", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @JsonManagedReference
    private Set<Pregunta> preguntas = new HashSet<>();

    @OneToMany(mappedBy = "cuestionario", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @JsonManagedReference
    private Set<Categoria> categorias = new HashSet<>();
}
