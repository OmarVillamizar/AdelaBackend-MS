package com.example.chaea.dto;

import java.sql.Date;

import com.example.chaea.entities.Estudiante;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResultadoCuestionarioDTO {
    private CuestionarioResumidoDTO cuestionario;
    private EstudianteDTO estudiante;
    private Date fechaAplicacion;
    private Date fechaResolucion;
    private GrupoResumidoDTO grupo;
    private Long id;
}
