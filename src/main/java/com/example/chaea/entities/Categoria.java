package com.example.chaea.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "estilo")
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "cuestionario_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private Cuestionario cuestionario;
    
    @Column(nullable = false)
    private String nombre;
    
    @Column(nullable = false)
    private Double valorMinimo;
    
    @Column(nullable = false)
    private Double valorMaximo;
}
