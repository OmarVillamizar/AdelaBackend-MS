package com.example.adela.dto;

import com.example.adela.entities.UsuarioEstado;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public abstract class UserDTO {
    private String email;
    
    private String nombre;
    
    private String codigo;
    
    private UsuarioEstado estado;
    
    private UserType tipoUsuario;
    
    public UserDTO(String email, String nombre, String codigo, UsuarioEstado estado, UserType tipoUsuario) {
        this.email = email;
        this.nombre = nombre;
        this.codigo = codigo == null ? "" : codigo;
        this.estado = estado;
        this.tipoUsuario = tipoUsuario;
    }
    
}
