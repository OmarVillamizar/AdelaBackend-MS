package com.example.chaea.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CategoriaDTO {
    private String nombre;
    private int id; //No es el id de la bd
}
