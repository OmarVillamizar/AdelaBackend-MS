package com.example.chaea.entities;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.Embeddable;

@Embeddable
public class CategoriaId implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private UUID cuestionarioId;
    private UUID categoriaId;

    // Constructor, getters, setters, equals y hashCode
    public CategoriaId() {}

    public CategoriaId(UUID cuestionarioId, UUID categoriaId) {
        this.cuestionarioId = cuestionarioId;
        this.categoriaId = categoriaId;
    }

    public UUID getCuestionarioId() {
        return cuestionarioId;
    }

    public void setCuestionarioId(UUID cuestionarioId) {
        this.cuestionarioId = cuestionarioId;
    }

    public UUID getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(UUID categoriaId) {
        this.categoriaId = categoriaId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cuestionarioId, categoriaId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CategoriaId other = (CategoriaId) obj;
        return Objects.equals(cuestionarioId, other.cuestionarioId) && Objects.equals(categoriaId, other.categoriaId);
    }

   
}
