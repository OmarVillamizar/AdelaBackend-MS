package com.example.chaea.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.chaea.entities.Categoria;
import com.example.chaea.entities.Cuestionario;
import com.example.chaea.repositories.CategoriaRepository;
import com.example.chaea.repositories.CuestionarioRepository;

@Service
public class CategoriaService {
    
    @Autowired
    private CategoriaRepository categoriaRepository;
    
    @Autowired
    private CuestionarioRepository cuestionarioRepository;
    
    public Categoria crearCategoria(Long idCuestionario, String nombre, Double valorMinimo, Double valorMaximo) {
        Cuestionario cuestionario = cuestionarioRepository.findById(idCuestionario)
                .orElseThrow(() -> new RuntimeException("Cuestionario no encontrado con id " + idCuestionario));
        
        Categoria categoria = new Categoria();
        
        categoria.setCuestionario(cuestionario);
        categoria.setNombre(nombre);
        categoria.setValorMaximo(valorMaximo);
        categoria.setValorMinimo(valorMinimo);
        
        return categoriaRepository.save(categoria);
    }
}
