package com.rebels.quasar.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;

import java.util.List;


/**
 *
 * @author FDip
 */
@Builder
@Schema(description = "Información individual de un satélite con la distancia y los fragmentos del mensaje recibido")
public record SatelliteDataDto(
    @NotEmpty(message = "El nombre del satélite no puede estar vacío")
    String name,
    
    @NotNull(message = "La distancia a la nave no puede ser nula")
    @PositiveOrZero(message = "La distancia no puede ser negativa")
    Float distance,
    
    @NotEmpty(message = "El mensaje no puede estar vacío")
    List<String> message
) {}
