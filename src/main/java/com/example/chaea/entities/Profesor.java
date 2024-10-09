package com.example.chaea.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "profesor")
public class Profesor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(length = 100,nullable = false)
	private String usuario_email;
	
	@Column(length = 50,nullable = false)
	private String carrera;
	
	private int id_rol;
	
	@Column(length = 100,nullable = false)
	private String estado;
}
