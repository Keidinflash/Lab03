package pe.edu.unsch.clientes.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Datos de respuesta del cliente")
public record ClienteResponseDTO(
        Long id,
        String dni,
        String nombre,
        String email,
        LocalDateTime fechaCreacion
) {}