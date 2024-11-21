package com.example.chaea.services;

import java.sql.Date;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.chaea.dto.CategoriaResultadoDTO;
import com.example.chaea.dto.CuestionarioResumidoDTO;
import com.example.chaea.dto.EstudianteDTO;
import com.example.chaea.dto.GrupoResumidoDTO;
import com.example.chaea.dto.ListasCuestionariosDTO;
import com.example.chaea.dto.PreguntaResueltaDTO;
import com.example.chaea.dto.RespuestaCuestionarioDTO;
import com.example.chaea.dto.ResultCuestCompletoDTO;
import com.example.chaea.dto.ResultadoCuestionarioDTO;
import com.example.chaea.entities.Categoria;
import com.example.chaea.entities.Cuestionario;
import com.example.chaea.entities.Estudiante;
import com.example.chaea.entities.Grupo;
import com.example.chaea.entities.Opcion;
import com.example.chaea.entities.Pregunta;
import com.example.chaea.entities.ResultadoCuestionario;
import com.example.chaea.entities.ResultadoPregunta;
import com.example.chaea.repositories.CuestionarioRepository;
import com.example.chaea.repositories.EstudianteRepository;
import com.example.chaea.repositories.GrupoRepository;
import com.example.chaea.repositories.OpcionRepository;
import com.example.chaea.repositories.PreguntaRepository;
import com.example.chaea.repositories.ResultadoCuestionarioRepository;
import com.example.chaea.repositories.ResultadoPreguntaRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ResultadoCuestionarioService {
    @Autowired
    private ResultadoCuestionarioRepository resultadoCuestionarioRepository;
    
    @Autowired
    private ResultadoPreguntaRepository resultadoPreguntaRepository;
    
    @Autowired
    private CuestionarioRepository cuestionarioRepository;
    
    @Autowired
    private PreguntaRepository preguntaRepository;
    
    @Autowired
    private OpcionRepository opcionRepository;
    
    @Autowired
    private GrupoRepository grupoRepository;
    
    @Autowired
    private EstudianteRepository estudianteRepository;
    
    @Transactional
    public ResultadoCuestionario responderCuestionario(RespuestaCuestionarioDTO info, Estudiante estudiante) {
        Long cuestionarioId = info.getCuestionarioId();
        Cuestionario cuestionario = cuestionarioRepository.findById(cuestionarioId)
                .orElseThrow(() -> new EntityNotFoundException("No existe el cuestionario con id " + cuestionarioId));
        
        ResultadoCuestionario resC = resultadoCuestionarioRepository
                .findByCuestionarioAndEstudianteAndFechaResolucionIsNull(cuestionario, estudiante)
                .orElseThrow(() -> new EntityNotFoundException("Al estudiante " + estudiante.getEmail()
                        + " no se le fue asignado el cuestionario " + cuestionario.getId()));
        
        if(resC.isBloqueado()) {
            throw new RuntimeException("Este cuestionario está bloqueado y no se puede responder.");
        }
        
        resC.setFechaResolucion(Date.valueOf(LocalDate.now()));
        resC = resultadoCuestionarioRepository.save(resC);
        List<ResultadoPregunta> resultadoPreguntas = new LinkedList<>();
        List<Pregunta> preguntas = preguntaRepository.findByCuestionario(cuestionario);
        Map<Long, Pregunta> answered = new TreeMap<>();
        Map<Long, Pregunta> unAnswered = new TreeMap<>();
        for (Pregunta pregunta : preguntas) {
            unAnswered.put(pregunta.getId(), pregunta);
        }
        for (Long opcionId : info.getOpcionesSeleccionadasId()) {
            ResultadoPregunta rp = responderPregunta(opcionId, resC);
            Long preguntaId = rp.getOpcion().getPregunta().getId();
            if (answered.containsKey(preguntaId)) {
                throw new RuntimeException(
                        "La pregunta " + answered.get(preguntaId).getOrden() + " tuvo mas de una opcion seleccionada("+rp.getOpcion().getOrden()+").");
            }
            answered.put(preguntaId, rp.getOpcion().getPregunta());
            unAnswered.remove(preguntaId);
            resultadoPreguntas.add(rp);
        }
        
        if (!unAnswered.isEmpty()) {
            StringBuilder result = new StringBuilder();
            for (Pregunta value : unAnswered.values()) {
                result.append(value.getOrden());
                result.append(", ");
            } // Eliminar la última coma y espacio
            if (result.length() > 0) {
                result.setLength(result.length() - 2);
            }
            throw new RuntimeException("Las preguntas " + result + " no fueron respondidas");
        }
        
        resultadoPreguntaRepository.saveAll(resultadoPreguntas);
        return resultadoCuestionarioRepository.save(resC);
    }
    
    public ResultadoPregunta responderPregunta(Long opcionId, ResultadoCuestionario resC) {
        Opcion opcion = opcionRepository.findById(opcionId)
                .orElseThrow(() -> new EntityNotFoundException("No existe la opción " + opcionId));
        Pregunta pregunta = opcion.getPregunta();
        Cuestionario cuestionario = resC.getCuestionario();
        if (pregunta.getCuestionario().getId() != cuestionario.getId()) {
            throw new RuntimeException("Inconsistencia: la opcion de id " + opcionId + " no pertenece al cuestionario "
                    + cuestionario.getId());
        }
        ResultadoPregunta rp = new ResultadoPregunta();
        rp.setCuestionario(resC);
        rp.setOpcion(opcion);
        
        return rp;
    }
    
    public void asignarCuestionarioAGrupo(Long cuestionarioId, Integer grupoId) {
        Cuestionario cuestionario = cuestionarioRepository.findById(cuestionarioId)
                .orElseThrow(() -> new EntityNotFoundException("No existe el cuestionario con id " + cuestionarioId));
        
        Grupo grupo = grupoRepository.findById(grupoId)
                .orElseThrow(() -> new EntityNotFoundException("No existe el grupo con id " + grupoId));
        
        Set<Estudiante> estudiantes = grupo.getEstudiantes();
        List<ResultadoCuestionario> asignaciones = new LinkedList<>();
        for (Estudiante estudiante : estudiantes) {
            if (resultadoCuestionarioRepository
                    .findByCuestionarioAndEstudianteAndFechaResolucionIsNull(cuestionario, estudiante).isEmpty()) {
                ResultadoCuestionario rc = new ResultadoCuestionario();
                rc.setCuestionario(cuestionario);
                rc.setEstudiante(estudiante);
                rc.setFechaAplicacion(Date.valueOf(LocalDate.now()));
                rc.setGrupo(grupo);
                asignaciones.add(rc);
            }
        }
        resultadoCuestionarioRepository.saveAll(asignaciones);
    }
    
    public void asignarCuestionarioAEstudiante(Long cuestionarioId, String estudianteEmail) {
        Cuestionario cuestionario = cuestionarioRepository.findById(cuestionarioId)
                .orElseThrow(() -> new EntityNotFoundException("No existe el cuestionario con id " + cuestionarioId));
        
        Estudiante estudiante = estudianteRepository.findById(estudianteEmail)
                .orElseThrow(() -> new EntityNotFoundException("No existe el estudiante con id " + estudianteEmail));
        if (resultadoCuestionarioRepository
                .findByCuestionarioAndEstudianteAndFechaResolucionIsNull(cuestionario, estudiante).isEmpty()) {
            ResultadoCuestionario rc = new ResultadoCuestionario();
            rc.setCuestionario(cuestionario);
            rc.setEstudiante(estudiante);
            rc.setFechaAplicacion(Date.valueOf(LocalDate.now()));
            resultadoCuestionarioRepository.save(rc);
        }
    }
    
    public ListasCuestionariosDTO obtenerCuestionarios(Estudiante estudiante) {
        List<ResultadoCuestionario> info = resultadoCuestionarioRepository.findByEstudiante(estudiante);
        
        List<ResultadoCuestionarioDTO> pendientes = new LinkedList<>();
        List<ResultadoCuestionarioDTO> resueltos = new LinkedList<>();
        
        for (ResultadoCuestionario rc : info) {
            ResultadoCuestionarioDTO rcdto = new ResultadoCuestionarioDTO();
            
            Cuestionario c = rc.getCuestionario();
            
            CuestionarioResumidoDTO cdto = CuestionarioResumidoDTO.from(c);
            
            rcdto.setCuestionario(cdto);
            rcdto.setEstudiante(EstudianteDTO.from(rc.getEstudiante()));
            rcdto.setGrupo(GrupoResumidoDTO.from(rc.getGrupo()));
            rcdto.setFechaAplicacion(rc.getFechaAplicacion());
            rcdto.setFechaResolucion(rc.getFechaResolucion());
            rcdto.setId(rc.getId());
            if (rc.getFechaResolucion() == null) {
                pendientes.add(rcdto);
            } else {
                resueltos.add(rcdto);
            }
        }
        
        ListasCuestionariosDTO lcdto = new ListasCuestionariosDTO();
        
        lcdto.setPendientes(pendientes);
        lcdto.setResueltos(resueltos);
        
        return lcdto;
    }
    
    public ResultCuestCompletoDTO obtenerResultadoCuestionario(Long cuestionarioResueltoId, Estudiante estudiante) {
        ResultCuestCompletoDTO res = new ResultCuestCompletoDTO();
        
        ResultadoCuestionario resC = resultadoCuestionarioRepository.findById(cuestionarioResueltoId)
                .orElseThrow(() -> new EntityNotFoundException("Al estudiante " + estudiante.getEmail()
                        + " el resultado de id " + cuestionarioResueltoId + " no pertenece al estudiante o no existe"));
        
        if (resC.getFechaResolucion() == null) {
            throw new EntityNotFoundException("El id " + cuestionarioResueltoId
                    + " corresponde a una aplicación de un cuestionario que no se ha completado");
        }
        
        Cuestionario c = resC.getCuestionario();
        res.setCuestionario(CuestionarioResumidoDTO.from(c));
        res.setEstudiante(EstudianteDTO.from(resC.getEstudiante()));
        res.setGrupo(GrupoResumidoDTO.from(resC.getGrupo()));
        res.setFechaAplicacion(resC.getFechaAplicacion());
        res.setFechaResolucion(resC.getFechaResolucion());
        res.setId(resC.getId());
        
        Map<Long, CategoriaResultadoDTO> mp = new TreeMap<>();
        List<CategoriaResultadoDTO> categorias = new LinkedList<>();
        List<PreguntaResueltaDTO> preguntas = new LinkedList<>();
        
        for (Categoria categoria : c.getCategorias()) {
            CategoriaResultadoDTO cr = new CategoriaResultadoDTO();
            cr.setNombre(categoria.getNombre());
            cr.setValor(0d);
            cr.setValorMaximo(categoria.getValorMaximo());
            cr.setValorMinimo(categoria.getValorMinimo());
            mp.put(categoria.getId(), cr);
            categorias.add(cr);
        }
        
        for (ResultadoPregunta rep : resC.getPreguntas()) {
            PreguntaResueltaDTO pr = new PreguntaResueltaDTO();
            Opcion o = rep.getOpcion();
            Pregunta p = o.getPregunta();
            pr.setPregunta(p.getPregunta());
            pr.setRespuesta(o.getRespuesta());
            pr.setOrden(p.getOrden());
            CategoriaResultadoDTO cr = mp.get(o.getCategoria().getId());
            cr.setValor(cr.getValor() + o.getValor());
            preguntas.add(pr);
        }
        
        res.setCategorias(categorias);
        res.setPreguntas(preguntas);
        
        return res;
    }
    
}
