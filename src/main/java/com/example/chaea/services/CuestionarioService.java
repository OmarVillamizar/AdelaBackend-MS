package com.example.chaea.services;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.chaea.dto.CategoriaDTO;
import com.example.chaea.dto.CuestionarioDTO;
import com.example.chaea.dto.CuestionarioResumidoDTO;
import com.example.chaea.dto.PreguntaDTO;
import com.example.chaea.entities.Categoria;
import com.example.chaea.entities.Cuestionario;
import com.example.chaea.entities.Pregunta;
import com.example.chaea.repositories.CuestionarioRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CuestionarioService {
    
    @Autowired
    private CuestionarioRepository cuestionarioRepository;
    
    @Autowired
    private CategoriaService categoriaService;
    
    @Autowired
    private PreguntaService preguntaService;
    
    public Cuestionario crearCuestionario(String nombre, String descripcion, String autor, String version,
            String siglas) {
        Cuestionario cuestionario = new Cuestionario();
        cuestionario.setNombre(nombre);
        cuestionario.setDescripcion(descripcion);
        cuestionario.setAutor(autor);
        cuestionario.setVersion(version);
        cuestionario.setSiglas(siglas);
        
        return cuestionarioRepository.save(cuestionario);
    }
    
    @Transactional
    public Cuestionario crearCuestionario(CuestionarioDTO cuestionarioDTO) {
        Cuestionario cuestionarioSave = new Cuestionario();
        cuestionarioSave.setNombre(cuestionarioDTO.getNombre());
        cuestionarioSave.setDescripcion(cuestionarioDTO.getAutor());
        cuestionarioSave.setAutor(cuestionarioDTO.getAutor());
        cuestionarioSave.setSiglas(cuestionarioDTO.getSiglas());
        cuestionarioSave.setVersion(cuestionarioDTO.getVersion());
        
        Cuestionario cuestionario = cuestionarioRepository.save(cuestionarioSave);
        
        Map<Integer, Categoria> idMap = new HashMap<>();
        
        Set<Categoria> categorias = new HashSet<>();
        Set<Pregunta> preguntas = new HashSet<>();
        
        for (CategoriaDTO categoriaDTO : cuestionarioDTO.getCategorias()) {
            int otherId = categoriaDTO.getId();
            Categoria categoria = categoriaService.crearCategoria(cuestionario, categoriaDTO);
            idMap.put(otherId, categoria);
            categorias.add(categoria);
        }
        
        for (PreguntaDTO preguntaDTO : cuestionarioDTO.getPreguntas()) {
            Pregunta pregunta = preguntaService.crearPregunta(cuestionario, idMap, preguntaDTO);
            preguntas.add(pregunta);
        }
        
        categoriaService.guardarCategorias(categorias);
        
        cuestionario.setCategorias(categorias);
        cuestionario.setPreguntas(preguntas);
        
        return cuestionarioRepository.save(cuestionario);
    }
    
    public void eliminarCuestionario(Long id) {
        Cuestionario cuestionario = cuestionarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cuestionario no encontrado con el ID: " + id));
        
        for(Pregunta pregunta : cuestionario.getPreguntas()) {
            preguntaService.eliminarPregunta(pregunta);
        }
        cuestionario.getPreguntas().clear();
        
        for(Categoria categoria : cuestionario.getCategorias()) {
            categoriaService.eliminarCategoria(categoria);
        }
        cuestionario.getPreguntas().clear();
        
        cuestionarioRepository.delete(cuestionario);
    }
    
    public Cuestionario getCuestionarioPorId(Long id) {        
        return cuestionarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cuestionario no encontrado con el ID: " + id));
    }
    
    public List<CuestionarioResumidoDTO> getCuestionarios() {
        List<Cuestionario> cuestionarios = cuestionarioRepository.findAll();
        List<CuestionarioResumidoDTO> cdtos = new LinkedList<>();
        
        for (Cuestionario cuestionario : cuestionarios) {
            CuestionarioResumidoDTO cdto = new CuestionarioResumidoDTO();
            cdto.setId(cuestionario.getId());
            cdto.setAutor(cuestionario.getAutor());
            cdto.setDescripcion(cuestionario.getDescripcion());
            cdto.setNombre(cuestionario.getNombre());
            cdto.setSiglas(cuestionario.getSiglas());
            cdto.setVersion(cuestionario.getVersion());
            cdto.setNumPreguntas(cuestionario.getPreguntas().size());
            cdtos.add(cdto);
        }
        
        return cdtos;
    }
    
}
