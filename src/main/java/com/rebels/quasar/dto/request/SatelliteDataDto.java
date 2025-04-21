package com.rebels.quasar.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 *
 * @author FDip
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información individual de un satélite con la distancia y los fragmentos del mensaje recibido")
public class SatelliteDataDto {
    @NotEmpty(message = "El nombre del satélite no puede estar vacío")
    private String name;
    
    @NotNull(message = "La distancia a la nave no puede ser nula")
    @PositiveOrZero(message = "La distancia no puede ser negativa")
    private Float distance;
    
    @NotEmpty(message = "El mensaje no puede estar vacío")
    private List<String> message;
}
