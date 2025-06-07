package com.rebels.quasar.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.Valid;
import java.util.List;


/**
 *
 * @author FDip
 */
@Schema(description = "Petición que contiene la información de los satélites")
public record TopSecretRequestDto(
    @NotNull(message = "La lista de satélites no puede ser nula")
    @Size(min = 3, max = 3, message = "Se necesitan exactamente 3 satélites")
    List<@Valid SatelliteDataDto> satellites
){}
