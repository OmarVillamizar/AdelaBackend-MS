package com.example.chaea.entities;

import java.sql.Date;
import java.util.Set;

import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Table(name = "estudiante")
public class Estudiante extends Usuario{
	
    @Enumerated
    @Nullable
	private Genero genero;
	
    @Nullable
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

    public Estudiante(String email, String nombre, String codigo, Genero genero, Date fechaNac, UsuarioEstado estado) {
        super(email, nombre, codigo, estado);
        this.genero = genero;
        this.fecha_nacimiento = fechaNac;
    }

    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    public Date getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(Date fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }
	
    
	
}
