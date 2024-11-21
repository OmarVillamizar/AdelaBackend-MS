package com.example.chaea.dto;

import com.example.chaea.entities.Grupo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GrupoResumidoDTO {
    private int id;
    private String nombre;
    private String profesorNombre;
    private String profesorEmail;
    private int numEstudiantes;
    
    public static GrupoResumidoDTO from(Grupo g) {
        GrupoResumidoDTO gt = new GrupoResumidoDTO();
        gt.setId(g.getId());
        gt.setNombre(g.getNombre());
        gt.setNumEstudiantes(g.getEstudiantes().size());
        gt.setProfesorEmail(g.getProfesor().getEmail());
        gt.setProfesorNombre(g.getProfesor().getNombre());
        return gt;
    }
}