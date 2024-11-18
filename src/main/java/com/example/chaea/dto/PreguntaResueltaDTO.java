package com.example.chaea.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PreguntaResueltaDTO {
    private String pregunta;
    private int orden;
    private String respuesta;
}
