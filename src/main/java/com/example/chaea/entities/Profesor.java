package com.example.chaea.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "profesor")
public class Profesor extends Usuario{
	
	@Column(length = 50,nullable = false)
	private String carrera;
	
	@ManyToOne
	@JoinColumn(name = "id_rol",nullable = false)
	private Rol rol;
	
	@Column(length = 100,nullable = false)
	private String estado;
	
	public Profesor() {
	    
	}

    public Profesor(String email, String nombre, String codigo, String carrera, String estado) {
        super(email, nombre, codigo);
        this.carrera = carrera;
        this.estado = estado;
        this.rol = new Rol();
    }
	
	
}
