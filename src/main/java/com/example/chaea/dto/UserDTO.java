package com.example.chaea.dto;

import java.sql.Date;

import com.example.chaea.entities.UsuarioEstado;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class UserDTO {
    private String email;
    
    private String nombre;
    
    private String codigo;

    private UsuarioEstado estado;
    
    private UserType tipoUsuario;
    
}
