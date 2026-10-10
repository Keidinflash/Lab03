package pe.edu.unsch.clientes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import pe.edu.unsch.clientes.dto.ClienteRequestDTO;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ClienteControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("HU-01 Escenario 1: Creacion exitosa retorna 201 Created y header Location")
    void testCreacionExitosa() throws Exception {
        ClienteRequestDTO dto = new ClienteRequestDTO("70809010", "Ana Torres", "ana.torres@unsch.edu.pe");

        mockMvc.perform(post("/api/v1/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.dni").value("70809010"));
    }

    @Test
    @DisplayName("HU-01 Escenario 2: Intento con DNI duplicado retorna 409 Conflict")
    void testDniDuplicadoRetorna409() throws Exception {
        ClienteRequestDTO dto1 = new ClienteRequestDTO("12345678", "Juan Perez", "juan@unsch.edu.pe");
        ClienteRequestDTO dto2 = new ClienteRequestDTO("12345678", "Maria Lopez", "maria@unsch.edu.pe");

        mockMvc.perform(post("/api/v1/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto1)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto2)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }
}