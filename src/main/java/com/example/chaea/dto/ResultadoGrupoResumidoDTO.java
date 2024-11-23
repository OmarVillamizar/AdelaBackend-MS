package com.example.chaea.dto;

import java.sql.Date;

import com.example.chaea.entities.ResultadoCuestionario;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResultadoGrupoResumidoDTO {
    public CuestionarioResumidoDTO cuestionario;
    public Date fechaAplicacion;
    public Date fechaResolucion;
    public GrupoResumidoDTO grupo;    
    
    public static ResultadoGrupoResumidoDTO from(ResultadoCuestionario rc) {
        ResultadoGrupoResumidoDTO rg = new ResultadoGrupoResumidoDTO();
        rg.setFechaAplicacion(rc.getFechaAplicacion());
        rg.setFechaResolucion(rc.getFechaResolucion());
        rg.setCuestionario(CuestionarioResumidoDTO.from(rc.getCuestionario()));
        rg.setGrupo(GrupoResumidoDTO.from(rc.getGrupo()));
        return rg;
    }
}