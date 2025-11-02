package com.example.adela.dto;

import com.example.adela.entities.ProfesorEstado;
import com.example.adela.entities.Rol;
import com.example.adela.entities.UsuarioEstado;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class ProfesorDTO extends UserDTO {
    
    private String carrera;
    private String rol;
    private ProfesorEstado estadoProfesor;
    
    public ProfesorDTO(String email, String nombre, String codigo, UsuarioEstado estado, String carrera, Rol rol,
            ProfesorEstado profesorEstado) {
        super(email, nombre, codigo, estado, UserType.PROFESOR);
        this.carrera = carrera == null ? "" : carrera;
        this.rol = rol == null ? "INACTIVO" : rol.getDescripcion();
        this.estadoProfesor = profesorEstado;
    }
}
