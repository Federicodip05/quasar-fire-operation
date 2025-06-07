package com.rebels.quasar.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;

/**
 *
 * @author FDip
 */
@Builder
@Schema(description = "Respuesta estándar para errores en la API")
public record ErrorResponseDto (
    LocalDateTime timestamp,
    int status,
    String error,
    String message
){}
