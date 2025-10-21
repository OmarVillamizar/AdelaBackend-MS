package com.example.chaea.dto;

import com.example.chaea.enums.TipoUsuario;
import lombok.Data;

@Data
public class UsuarioReferenciaRequest {
    private String email;
    private TipoUsuario tipoUsuario;
}
