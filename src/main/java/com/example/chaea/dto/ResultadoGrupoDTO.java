package com.example.chaea.dto;

import java.sql.Date;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResultadoGrupoDTO extends ResultadoGrupoResumidoDTO {
    private List<CategoriaResultadoDTO> categorias;
    private List<ResultadoCuestionarioDTO> estudiantes;
}
