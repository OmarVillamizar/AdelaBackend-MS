package com.example.chaea.entities;

import java.sql.Date;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "estudiante")
public class Estudiante extends Usuario{
	
	private byte genero;
	
	private Date fecha_nacimiento;
	
	@ManyToMany
	@JoinTable(
	  name = "matricula", 
	  joinColumns = @JoinColumn(name = "estudiante_id"), 
	  inverseJoinColumns = @JoinColumn(name = "curso_id"))
	private Set<Grupo> grupos;
	
	
	public Estudiante() {
	    super();
	}

    public Estudiante(String email, String nombre, String codigo, byte genero, Date fechaNac) {
        super(email, nombre, codigo);
        this.genero = genero;
        this.fecha_nacimiento = fechaNac;
    }

    public byte getGenero() {
        return genero;
    }

    public void setGenero(byte genero) {
        this.genero = genero;
    }

    public Date getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(Date fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }
	
    
	
}
