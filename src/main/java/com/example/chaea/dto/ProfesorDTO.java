package com.example.chaea.dto;

import com.example.chaea.entities.ProfesorEstado;
import com.example.chaea.entities.UsuarioEstado;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfesorDTO {
	private String email;
    private String nombre;
    private String codigo;
    private String carrera;
    private int rolId;
    private ProfesorEstado estadoProfesor;
    private UsuarioEstado estado;
}
