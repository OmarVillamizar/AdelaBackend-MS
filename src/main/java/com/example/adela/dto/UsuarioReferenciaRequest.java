package com.example.adela.dto;

import com.example.adela.enums.TipoUsuario;

import lombok.Data;

@Data
public class UsuarioReferenciaRequest {
    private String email;
    private TipoUsuario tipoUsuario;
}
