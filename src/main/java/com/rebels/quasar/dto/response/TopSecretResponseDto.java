package com.rebels.quasar.dto.response;

import com.rebels.quasar.model.Position;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 *
 * @author FDip
 */
@Schema(description = "Respuesta con la posición calculada de la nave y el mensaje decodificado")
public record TopSecretResponseDto(
    Position position,
    String message
){}