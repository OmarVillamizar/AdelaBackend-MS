package com.example.chaea.entities;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Pregunta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "cuestionario_id", referencedColumnName = "id", nullable = false)
    private Cuestionario cuestionario;
    
    @Column(nullable = false)
    private String pregunta;
    
    @Column(nullable = false)
    private int orden;
    
    @OneToMany(mappedBy = "pregunta")
    private Set<Opcion> opciones = new HashSet<>();
}
