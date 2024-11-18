package com.example.chaea;

import com.example.chaea.controllers.HealthController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Map;

@SpringBootTest
public class ChaeaHealthTests {

    @InjectMocks
    private HealthController healthController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(healthController).build();
    }

    @Test
    public void testGetStatus_ShouldReturnStatusWithMessageAndTimestamp() throws Exception {
        // Simular la respuesta del servidor
        Map<String, Object> expectedResponse = Map.of(
            "message", "Servidor en funcionamiento",
            "timestamp", LocalDateTime.now().toString()  // Convierte a string ya que LocalDateTime no puede ser comparado directamente
        );

        // Hacer la solicitud GET al endpoint /health/status
        mockMvc.perform(get("/health/status"))
                .andExpect(status().isOk()) // Espera el estado HTTP 200 OK
                .andExpect(jsonPath("$.message").value("Servidor en funcionamiento"))  // Verifica que el mensaje sea el correcto
                .andExpect(jsonPath("$.timestamp").exists()) // Verifica que el timestamp existe
                .andExpect(jsonPath("$.timestamp").isString()); // Verifica que el timestamp es un String
    }

    @Test
    public void testGetStatus_ShouldReturnCorrectJsonStructure() throws Exception {
        // Realizar una solicitud GET al endpoint /health/status
        mockMvc.perform(get("/health/status"))
                .andExpect(status().isOk()) // Verifica que la respuesta es 200 OK
                .andExpect(jsonPath("$").isMap()) // Verifica que la respuesta sea un Map
                .andExpect(jsonPath("$.message").value("Servidor en funcionamiento"))  // Verifica que el mensaje sea correcto
                .andExpect(jsonPath("$.timestamp").exists()); // Verifica que el timestamp esté presente
    }
}
