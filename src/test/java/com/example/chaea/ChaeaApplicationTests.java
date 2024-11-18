package com.example.chaea;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.example.chaea.entities.Cuestionario;
import com.example.chaea.entities.Pregunta;
import com.example.chaea.repositories.CuestionarioRepository;
import com.example.chaea.repositories.PreguntaRepository;
import com.example.chaea.services.OpcionService;
import com.example.chaea.services.PreguntaService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class ChaeaApplicationTests {

    @Test	
    void contextLoads() {
    }
    
    @Mock
    private PreguntaRepository preguntaRepository;

    @Mock
    private CuestionarioRepository cuestionarioRepository;

    @Mock
    private OpcionService opcionService;

    @InjectMocks
    private PreguntaService preguntaService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCrearPreguntaConIdCuestionario() {
        Cuestionario cuestionario = new Cuestionario();
        cuestionario.setId(1L);
        
        when(cuestionarioRepository.findById(1L)).thenReturn(Optional.of(cuestionario));
        when(preguntaRepository.save(any(Pregunta.class))).thenAnswer(i -> i.getArgument(0));

        Pregunta pregunta = preguntaService.crearPregunta(1L, "¿Cuál es tu nombre?", 1);
        
        assertNotNull(pregunta);
        assertEquals(cuestionario, pregunta.getCuestionario());
        assertEquals("¿Cuál es tu nombre?", pregunta.getPregunta());
        assertEquals(1, pregunta.getOrden());
        
        verify(preguntaRepository, times(1)).save(any(Pregunta.class));
    }


    @Test
    public void testCrearPreguntaConIdCuestionario_CuestionarioNoEncontrado() {
        when(cuestionarioRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            preguntaService.crearPregunta(1L, "¿Cuál es tu nombre?", 1);
        });

        String expectedMessage = "Cuestionario no encontrado con id 1";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
