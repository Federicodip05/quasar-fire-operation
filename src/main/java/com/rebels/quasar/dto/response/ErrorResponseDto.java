package com.rebels.quasar.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 *
 * @author FDip
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Respuesta estándar para errores en la API")
public class ErrorResponseDto {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
}
