package com.example.chaea.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.chaea.entities.Cuestionario;
import com.example.chaea.repositories.CuestionarioRepository;

@Service
public class CuestionarioService {

    @Autowired
    private CuestionarioRepository cuestionarioRepository;

    public Cuestionario crearCuestionario(String nombre, String descripcion, String autor, String version, String siglas) {
        Cuestionario cuestionario = new Cuestionario();
        cuestionario.setNombre(nombre);
        cuestionario.setDescripcion(descripcion);
        cuestionario.setAutor(autor);
        cuestionario.setVersion(version);
        cuestionario.setSiglas(siglas);

        return cuestionarioRepository.save(cuestionario);
    }
}
