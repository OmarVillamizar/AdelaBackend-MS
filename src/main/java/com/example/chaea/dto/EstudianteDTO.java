package com.example.chaea.dto;

import java.sql.Date;

import com.example.chaea.entities.Genero;
import com.example.chaea.entities.UsuarioEstado;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class EstudianteDTO extends UserDTO{

    private Genero genero;
    
    private Date fechaNacimiento;
        
    public EstudianteDTO(String email, String nombre, String codigo, UsuarioEstado estado, Genero genero, Date fechaNacimiento) {
        super(email, nombre, codigo, estado, UserType.ESTUDIANTE);
        this.genero = genero;
        this.fechaNacimiento = fechaNacimiento;
    }
}
