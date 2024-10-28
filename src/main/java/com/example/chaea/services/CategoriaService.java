package com.example.chaea.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.chaea.entities.Categoria;
import com.example.chaea.entities.CategoriaId;
import com.example.chaea.entities.Cuestionario;
import com.example.chaea.repositories.CategoriaRepository;
import com.example.chaea.repositories.CuestionarioRepository;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private CuestionarioRepository cuestionarioRepository;

    public Categoria crearCategoria(UUID cuestionarioId, String textoCategoria) {
        // Buscar el cuestionario
        Cuestionario cuestionario = cuestionarioRepository.findById(cuestionarioId)
            .orElseThrow(() -> new RuntimeException("Cuestionario no encontrado"));

        // Crear la clave compuesta con UUID
        CategoriaId categoriaId = new CategoriaId(cuestionarioId, UUID.randomUUID());

        Categoria categoria = new Categoria();
        categoria.setId(categoriaId);

        return categoriaRepository.save(categoria);
    }
}
