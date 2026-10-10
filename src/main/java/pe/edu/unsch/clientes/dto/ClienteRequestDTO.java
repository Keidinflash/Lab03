package pe.edu.unsch.clientes.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos para el registro o edición de un cliente")
public record ClienteRequestDTO(
        @Schema(description = "DNI de 8 dígitos", example = "12345678")
        @NotBlank(message = "El DNI es un campo obligatorio")
        @Size(min = 8, max = 8, message = "El DNI debe tener exactamente 8 caracteres")
        @Pattern(regexp = "^\\d{8}$", message = "El DNI debe contener solo dígitos numéricos")
        String dni,

        @Schema(description = "Nombre completo del cliente", example = "Keidin Leol Redolfo Gavilan")
        @NotBlank(message = "El nombre es un campo obligatorio")
        @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
        String nombre,

        @Schema(description = "Correo electrónico institucional o personal", example = "keidin.redolfo.27@unsch.edu.pe")
        @NotBlank(message = "El correo electrónico es un campo obligatorio")
        @Email(message = "Formato de correo electrónico inválido")
        String email
) {}