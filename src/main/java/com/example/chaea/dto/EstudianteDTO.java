package com.example.chaea.dto;

import java.sql.Date;

import com.example.chaea.entities.Genero;
import com.example.chaea.entities.UsuarioEstado;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstudianteDTO {
	private String email;
	
    private String nombre;
    
    private String codigo;
    
    private Genero genero;
    
    private Date fechaNacimiento;
    
    private UsuarioEstado estado;
}
