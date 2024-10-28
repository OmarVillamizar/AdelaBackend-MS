package com.example.chaea.entities;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;

@Entity
public class Pregunta {

    @EmbeddedId
    private PreguntaId id;

    @ManyToOne
    @MapsId("cuestionarioId")
    @JoinColumn(name = "cuestionario_id")
    private Cuestionario cuestionario;

    private String texto;

    // Getters y setters
    public PreguntaId getId() {
        return id;
    }

    public void setId(PreguntaId id) {
        this.id = id;
    }

    public Cuestionario getCuestionario() {
        return cuestionario;
    }

    public void setCuestionario(Cuestionario cuestionario) {
        this.cuestionario = cuestionario;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}

