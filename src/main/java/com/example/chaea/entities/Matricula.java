package com.example.chaea.entities;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

//@Entity ??????????????????????
@Data
@Table(name = "matricula")
public class Matricula {

	@ManyToOne
	@JoinColumn(name = "id_grupo",nullable = false)
	private Grupo grupo;
	
	@ManyToOne
	@JoinColumn(name = "id_estuidante",nullable = false)
	private Estudiante estudiante;
}
