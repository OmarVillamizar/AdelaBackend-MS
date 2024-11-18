package com.example.chaea;

import com.example.chaea.controllers.GrupoController;
import com.example.chaea.dto.GrupoDTO;
import com.example.chaea.entities.Estudiante;
import com.example.chaea.entities.Grupo;
import com.example.chaea.entities.Profesor;
import com.example.chaea.repositories.EstudianteRepository;
import com.example.chaea.repositories.GrupoRepository;
import com.example.chaea.repositories.ProfesorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ChaeaGrupoTests {

    @InjectMocks
    private GrupoController grupoController;

    @Mock
    private GrupoRepository grupoRepository;

    @Mock
    private ProfesorRepository profesorRepository;

    @Mock
    private EstudianteRepository estudianteRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    private Profesor mockProfesor;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockProfesor = new Profesor();
        mockProfesor.setEmail("profesor@ufps.edu.co");

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(mockProfesor);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void testCrearGrupo_ValidInput_ShouldReturnCreatedGrupo() {
        GrupoDTO grupoDTO = new GrupoDTO();
        grupoDTO.setNombre("Grupo 1");
        grupoDTO.setCorreosEstudiantes(new HashSet<>(Arrays.asList("estudiante1@ufps.edu.co"))); // Convertir la lista a un Set

        Estudiante estudiante = new Estudiante();
        estudiante.setEmail("estudiante1@ufps.edu.co");
        Optional<Estudiante> optEstudiante = Optional.of(estudiante);

        when(estudianteRepository.findById("estudiante1@ufps.edu.co")).thenReturn(optEstudiante);
        when(grupoRepository.save(any(Grupo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<?> response = grupoController.crearGrupo(grupoDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Grupo result = (Grupo) response.getBody();
        assertNotNull(result);
        assertEquals("Grupo 1", result.getNombre());
        assertTrue(result.getEstudiantes().contains(estudiante));

        verify(grupoRepository, times(1)).save(any(Grupo.class));
        verify(estudianteRepository, times(1)).saveAll(anySet());
    }

    @Test
    public void testCrearGrupo_InvalidEmailFormat_ShouldReturnBadRequest() {
        GrupoDTO grupoDTO = new GrupoDTO();
        grupoDTO.setNombre("Grupo 2");
        grupoDTO.setCorreosEstudiantes(new HashSet<>(Arrays.asList("invalid-email")));

        ResponseEntity<?> response = grupoController.crearGrupo(grupoDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Los siguientes correos cuentan con un formato erróneo"));
    }

    @Test
    public void testListarGrupos_ShouldReturnListOfGrupos() {
        Grupo grupo1 = new Grupo();
        grupo1.setId(1); 
        grupo1.setNombre("Grupo 1");
        grupo1.setProfesor(mockProfesor);

        Grupo grupo2 = new Grupo();
        grupo2.setId(2);
        grupo2.setNombre("Grupo 2");
        grupo2.setProfesor(mockProfesor);

        when(grupoRepository.findByProfesor(mockProfesor)).thenReturn(Arrays.asList(grupo1, grupo2));

        ResponseEntity<List<Grupo>> response = grupoController.listarGrupos();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<Grupo> grupos = response.getBody();
        assertNotNull(grupos);
        assertEquals(2, grupos.size());
    }

    @Test
    public void testConsultarGrupoPorId_ValidId_ShouldReturnGrupo() {
        Grupo grupo = new Grupo();
        grupo.setId(1);  // Se mantiene la propiedad del ID
        grupo.setNombre("Grupo 1");
        grupo.setProfesor(mockProfesor);

        when(grupoRepository.findByProfesorAndId(mockProfesor, 1)).thenReturn(Optional.of(grupo));

        ResponseEntity<?> response = grupoController.consultarGrupoPorId(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(grupo, response.getBody());
    }

    @Test
    public void testConsultarGrupoPorId_InvalidId_ShouldReturnNotFound() {
        when(grupoRepository.findByProfesorAndId(mockProfesor, 1)).thenReturn(Optional.empty());

        ResponseEntity<?> response = grupoController.consultarGrupoPorId(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Grupo no encontrado con el ID: 1"));
    }

    @Test
    public void testEliminarGrupo_ValidId_ShouldDeleteGrupo() {
        Grupo grupo = new Grupo();
        grupo.setId(1);
        grupo.setNombre("Grupo 1");
        grupo.setProfesor(mockProfesor);
        grupo.setEstudiantes(new HashSet<>());

        when(grupoRepository.findByProfesorAndId(mockProfesor, 1)).thenReturn(Optional.of(grupo));

        ResponseEntity<?> response = grupoController.eliminarGrupo(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(grupoRepository, times(1)).deleteById(1);
    }

    @Test
    public void testActualizarGrupo_ValidId_ShouldUpdateGrupo() {
        Grupo grupo = new Grupo();
        grupo.setId(1);
        grupo.setNombre("Grupo 1");
        grupo.setProfesor(mockProfesor);
        grupo.setEstudiantes(new HashSet<>());

        GrupoDTO grupoDTO = new GrupoDTO();
        grupoDTO.setNombre("Nuevo Nombre");
        grupoDTO.setCorreosEstudiantes(Collections.emptySet());

        when(grupoRepository.findByProfesorAndId(mockProfesor, 1)).thenReturn(Optional.of(grupo));
        when(grupoRepository.save(any(Grupo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<?> response = grupoController.actualizarGrupo(1, grupoDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Grupo updatedGrupo = (Grupo) response.getBody();
        assertNotNull(updatedGrupo);
        assertEquals("Nuevo Nombre", updatedGrupo.getNombre());

        verify(grupoRepository, times(1)).save(grupo);
    }
}
