package com.example.chaea;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.Collections;
import java.util.Optional;
import java.text.SimpleDateFormat;
import java.sql.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.example.chaea.controllers.EstudianteController;
import com.example.chaea.dto.EstudianteDTO;
import com.example.chaea.entities.Estudiante;
import com.example.chaea.entities.Genero;
import com.example.chaea.entities.UsuarioEstado;
import com.example.chaea.repositories.EstudianteRepository;
import com.example.chaea.repositories.GrupoRepository;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(EstudianteController.class)
@Import(TestConfig.class)
public class ChaeaEstudianteTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EstudianteRepository estudianteRepository;

    @MockBean
    private GrupoRepository grupoRepository;

    @InjectMocks
    private EstudianteController estudianteController;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser(roles = "ESTUDIANTE")
    public void testOla() throws Exception {
        mockMvc.perform(get("/api/estudiantes/omero"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hola"));
    }

    @Test
    @WithMockUser(roles = {"PROFESOR", "ADMINISTRADOR"})
    public void testListarEstudiantes() throws Exception {
        when(estudianteRepository.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/estudiantes")
                .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_PROFESOR"))))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @WithMockUser(roles = {"PROFESOR", "ADMINISTRADOR"})
    public void testConsultarPorCorreo() throws Exception {
        Estudiante estudiante = new Estudiante();
        estudiante.setEmail("test@ufps.edu.co");
        estudiante.setNombre("Test Nombre");
        estudiante.setCodigo("12345");
        estudiante.setEstado(UsuarioEstado.ACTIVA);

        when(estudianteRepository.findById("test@ufps.edu.co")).thenReturn(Optional.of(estudiante));

        mockMvc.perform(get("/api/estudiantes/test@ufps.edu.co")
                .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_PROFESOR"))))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(estudiante)));
    }

    @Test
    @WithMockUser(roles = {"PROFESOR", "ADMINISTRADOR"})
    public void testConsultarPorCorreoNotFound() throws Exception {
        when(estudianteRepository.findById("notfound@ufps.edu.co")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/estudiantes/notfound@ufps.edu.co")
                .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_PROFESOR"))))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Estudiante no encontrado con el correo: notfound@ufps.edu.co"));
    }

    @Test
    @WithMockUser(roles = {"ESTUDIANTE", "ESTUDIANTE_INCOMPLETO"})
    public void testActualizarEstudiante() throws Exception {
        EstudianteDTO estudianteDTO = new EstudianteDTO();
        estudianteDTO.setCodigo("12345");
        estudianteDTO.setFechaNacimiento(new Date(new SimpleDateFormat("yyyy-MM-dd").parse("2000-01-01").getTime()));
        estudianteDTO.setGenero(Genero.MASCULINO);

        Estudiante estudiante = new Estudiante();
        estudiante.setEmail("test@ufps.edu.co");
        estudiante.setEstado(UsuarioEstado.INCOMPLETA);

        when(estudianteRepository.findById("test@ufps.edu.co")).thenReturn(Optional.of(estudiante));
        when(estudianteRepository.save(any(Estudiante.class))).thenReturn(estudiante);

        mockMvc.perform(put("/api/estudiantes")
                .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ESTUDIANTE")))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(estudianteDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(estudiante)));
    }

    @Test
    @WithMockUser(roles = {"ESTUDIANTE", "ESTUDIANTE_INCOMPLETO"})
    public void testActualizarEstudianteNotFound() throws Exception {
        EstudianteDTO estudianteDTO = new EstudianteDTO();
        estudianteDTO.setCodigo("12345");
        estudianteDTO.setFechaNacimiento(new Date(new SimpleDateFormat("yyyy-MM-dd").parse("2000-01-01").getTime()));
        estudianteDTO.setGenero(Genero.MASCULINO);

        when(estudianteRepository.findById("test@ufps.edu.co")).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/estudiantes")
                .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ESTUDIANTE")))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(estudianteDTO)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Estudiante no encontrado con el correo: test@ufps.edu.co"));
    }
}
