package com.example.chaea.entities;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;

@Entity
public class Categoria {
    @EmbeddedId
    private CategoriaId id;
    
    @ManyToOne
    @MapsId("cuestionarioId")
    @JoinColumn(name = "cuestionario_id")
    private Cuestionario cuestionario;

    public CategoriaId getId() {
        return id;
    }

    public void setId(CategoriaId id) {
        this.id = id;
    }

    public Cuestionario getCuestionario() {
        return cuestionario;
    }

    public void setCuestionario(Cuestionario cuestionario) {
        this.cuestionario = cuestionario;
    }
    
    
}
