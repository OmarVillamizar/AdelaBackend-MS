package com.example.chaea.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "Usuario")
public abstract class Usuario {
	
	@Id
	@Column(length = 100,nullable = false)
	private String email;
	
	@Column(length = 100,nullable = false)
	private String nombre;
	
	@Column(length = 8,nullable = false)
	private String codigo;
	
	public Usuario() {
	    
	}

    public Usuario(String email, String nombre, String codigo) {
        this.email = email;
        this.nombre = nombre;
        this.codigo = codigo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
	
	
}
