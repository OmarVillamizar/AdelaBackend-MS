package com.example.adela.dto;

import java.sql.Date;

import com.example.adela.entities.Estudiante;
import com.example.adela.entities.Genero;
import com.example.adela.entities.UsuarioEstado;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class EstudianteDTO extends UserDTO {
    
    private Genero genero;
    
    private Date fechaNacimiento;
    
    public EstudianteDTO(String email, String nombre, String codigo, UsuarioEstado estado, Genero genero,
            Date fechaNacimiento) {
        super(email, nombre, codigo, estado, UserType.ESTUDIANTE);
        this.genero = genero;
        this.fechaNacimiento = fechaNacimiento;
    }
    
    public static EstudianteDTO from(Estudiante e) {
        EstudianteDTO edt = new EstudianteDTO();
        edt.setCodigo(e.getCodigo());
        edt.setEmail(e.getEmail());
        edt.setEstado(e.getEstado());
        edt.setFechaNacimiento(e.getFecha_nacimiento());
        edt.setGenero(e.getGenero());
        edt.setNombre(e.getNombre());
        edt.setTipoUsuario(UserType.ESTUDIANTE);
        return edt;
    }
}
