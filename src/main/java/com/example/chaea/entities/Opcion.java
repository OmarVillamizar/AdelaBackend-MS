package com.example.chaea.entities;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;

@Entity
public class Opcion {

    @EmbeddedId
    private OpcionId id;

    @ManyToOne
    @MapsId("preguntaId")
    @JoinColumns({
        @JoinColumn(name = "cuestionario_id", referencedColumnName = "cuestionarioId"),
        @JoinColumn(name = "pregunta_id", referencedColumnName = "preguntaId")
    })
    private Pregunta pregunta;

    private String texto;

    // Getters y setters
    public OpcionId getId() {
        return id;
    }

    public void setId(OpcionId id) {
        this.id = id;
    }

    public Pregunta getPregunta() {
        return pregunta;
    }

    public void setPregunta(Pregunta pregunta) {
        this.pregunta = pregunta;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}

