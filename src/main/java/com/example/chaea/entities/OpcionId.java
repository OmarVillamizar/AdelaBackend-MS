package com.example.chaea.entities;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.Embeddable;


@Embeddable
public class OpcionId implements Serializable {

    private UUID cuestionarioId;
    private UUID preguntaId;
    private UUID opcionId;

    // Constructor, getters, setters, equals y hashCode
    public OpcionId() {}

    public OpcionId(UUID cuestionarioId, UUID preguntaId, UUID opcionId) {
        this.cuestionarioId = cuestionarioId;
        this.preguntaId = preguntaId;
        this.opcionId = opcionId;
    }

    // Getters y setters
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

    public UUID getOpcionId() {
        return opcionId;
    }

    public void setOpcionId(UUID opcionId) {
        this.opcionId = opcionId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cuestionarioId, opcionId, preguntaId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        OpcionId other = (OpcionId) obj;
        return Objects.equals(cuestionarioId, other.cuestionarioId) && Objects.equals(opcionId, other.opcionId)
                && Objects.equals(preguntaId, other.preguntaId);
    }

    
}

