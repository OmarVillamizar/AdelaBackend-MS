package com.example.chaea.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CuestionarioResumidoDTO {
    private Long id;
    private String nombre;
    private String siglas;
    private String descripcion;
    private String autor;
    private String version;
    private int numPreguntas;
}
