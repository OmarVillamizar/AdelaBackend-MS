package com.example.chaea.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import jakarta.persistence.Column;

@Entity
@Data
@Table(name = "usuario")
public class Usuario {
	
	@Id
	@Column(length = 100,nullable = false)
	private String email;
	
	@Column(length = 100,nullable = false)
	private String nombre;
	
	@Column(length = 8,nullable = false)
	private String codigo;
}
