package com.example.chaea.dto;

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
}