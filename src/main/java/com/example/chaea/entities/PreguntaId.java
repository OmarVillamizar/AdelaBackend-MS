package com.example.chaea.entities;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.Embeddable;

@Embeddable
public class PreguntaId implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private UUID cuestionarioId;
    private UUID preguntaId;

    // Constructor, getters, setters, equals y hashCode
    public PreguntaId() {}

    public PreguntaId(UUID cuestionarioId, UUID preguntaId) {
        this.cuestionarioId = cuestionarioId;
        this.preguntaId = preguntaId;
    }

    public UUID getCuestionarioId() {
        return cuestionarioId;
    }

    public void setCuestionarioId(UUID cuestionarioId) {
        this.cuestionarioId = cuestionarioId;
    }

    public UUID getPreguntaId() {
        return preguntaId;
    }

    public void setPreguntaId(UUID preguntaId) {
        this.preguntaId = preguntaId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cuestionarioId, preguntaId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PreguntaId other = (PreguntaId) obj;
        return Objects.equals(cuestionarioId, other.cuestionarioId) && Objects.equals(preguntaId, other.preguntaId);
    }

   
}

