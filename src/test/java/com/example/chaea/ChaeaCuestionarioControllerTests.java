package com.example.chaea;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.Collections;

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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.chaea.controllers.CuestionarioController;
import com.example.chaea.dto.CuestionarioDTO;
import com.example.chaea.entities.Cuestionario;
import com.example.chaea.repositories.UsuarioRepository;
import com.example.chaea.services.CuestionarioService;
import com.example.chaea.security.JwtUtil;

import jakarta.persistence.EntityNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(CuestionarioController.class)
@Import(TestConfig.class)
public class ChaeaCuestionarioControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CuestionarioService cuestionarioService;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @MockBean
    private JwtUtil jwtUtil;

    @InjectMocks
    private CuestionarioController cuestionarioController;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(cuestionarioController).build();
    }

    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    public void testCrearCuestionario() throws Exception {
        CuestionarioDTO cuestionarioDTO = new CuestionarioDTO();
        Cuestionario cuestionario = new Cuestionario();
        
        when(cuestionarioService.crearCuestionario(any(CuestionarioDTO.class))).thenReturn(cuestionario);

        mockMvc.perform(post("/api/cuestionarios")
                .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMINISTRADOR")))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(cuestionarioDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(cuestionario)));
    }

    @Test
    @WithMockUser(roles = {"ADMINISTRADOR", "PROFESOR"})
    public void testListarCuestionarios() throws Exception {
        when(cuestionarioService.getCuestionarios()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/cuestionarios")
                .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMINISTRADOR"))))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @WithMockUser(roles = {"ADMINISTRADOR", "PROFESOR", "ESTUDIANTE"})
    public void testObtenerCuestionario() throws Exception {
        Cuestionario cuestionario = new Cuestionario();

        when(cuestionarioService.getCuestionarioPorId(1L)).thenReturn(cuestionario);

        mockMvc.perform(get("/api/cuestionarios/1")
                .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMINISTRADOR"))))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(cuestionario)));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    public void testEliminarCuestionario() throws Exception {
        doNothing().when(cuestionarioService).eliminarCuestionario(1L);

        mockMvc.perform(delete("/api/cuestionarios/1")
                .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMINISTRADOR"))))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    public void testEliminarCuestionarioNotFound() throws Exception {
        doThrow(new EntityNotFoundException()).when(cuestionarioService).eliminarCuestionario(1L);

        mockMvc.perform(delete("/api/cuestionarios/1")
                .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMINISTRADOR"))))
                .andExpect(status().isNotFound());
    }
}
