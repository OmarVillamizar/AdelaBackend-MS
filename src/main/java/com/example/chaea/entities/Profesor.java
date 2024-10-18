package com.example.chaea.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Table(name = "profesor")
public class Profesor extends Usuario{
	
	@Column(length = 50,nullable = true)
	@Getter
	@Setter
	private String carrera;
	
	@ManyToOne
	@JoinColumn(name = "rol_id",nullable = true)
	private Rol rol;
	
	@Column(length = 100,nullable = true)
	@Getter
	@Setter
	@Enumerated(EnumType.STRING)
	private ProfesorEstado estadoProfesor;
	
	public Profesor() {
	    
	}

    public Profesor(String email, String nombre, String codigo, String carrera, UsuarioEstado estado, ProfesorEstado estadoProfesor) {
        super(email, nombre, codigo, estado);
        this.carrera = carrera;
        this.estadoProfesor = estadoProfesor;
        this.rol = new Rol();
    }
	
	
}
