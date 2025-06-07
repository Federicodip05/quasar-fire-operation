package com.rebels.quasar.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 *
 * @author FDip
 */
@Schema(description = "Petición que contiene la información de un satélite")
public record TopSecretSplitRequestDto(
    @NotNull(message = "La distancia a la nave no puede ser nula")
    @PositiveOrZero(message = "La distancia no puede ser negativa")
    Float distance,
    
    @NotEmpty(message = "El mensaje no puede estar vacío")
    List<String> message
){}
