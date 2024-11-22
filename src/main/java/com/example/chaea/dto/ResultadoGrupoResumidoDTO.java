package com.example.chaea.dto;

import java.sql.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResultadoGrupoResumidoDTO {
    public CuestionarioResumidoDTO cuestionario;
    public Date fechaAplicacion;
    public Date fechaResolucion;
    public GrupoResumidoDTO grupo;    
}