package com.rebels.quasar.controller;

import com.rebels.quasar.dto.request.TopSecretSplitRequestDto;
import com.rebels.quasar.dto.response.ErrorResponseDto;
import com.rebels.quasar.dto.response.TopSecretResponseDto;
import com.rebels.quasar.service.TopSecretSplitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 
 *
 * @author FDip
 */
@Slf4j
@RestController
@RequestMapping("/topsecret_split")
@RequiredArgsConstructor
@Tag(name = "Top Secret Split API", description = "Endpoints para manejo fragmentado de información satelital")
public class TopSecretSplitController {

    private final TopSecretSplitService topSecretSplitService;

    @Operation(
        summary = "Guardar datos de un satélite",
        description = "Almacena la información de distancia y mensaje recibido por un satélite específico"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Datos guardados exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
        }
    )
    @PostMapping("/{satellite_name}")
    public ResponseEntity<Void> saveSatelliteData(
            @PathVariable("satellite_name") String satelliteName,
            @Valid @RequestBody TopSecretSplitRequestDto request) {
        
        log.info("Solicitud POST recibida en /topsecret_split/{}", satelliteName);
        topSecretSplitService.saveSatelliteData(satelliteName, request);
        log.info("Datos guardados para satélite: {}", satelliteName);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Guardar datos de un satélite",
        description = "Almacena la información de distancia y mensaje recibido por un satélite específico"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Datos guardados exitosamente",
                    content = @Content(schema = @Schema(implementation = TopSecretResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "No hay suficiente información para determinar la posición y/o el mensaje",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
        }
    )
    @GetMapping
    public ResponseEntity<TopSecretResponseDto> getSpaceshipData() {
        
        log.info("Solicitud GET recibida en /topsecret_split");
        return topSecretSplitService.getSpaceshipData()
            .map(spaceship -> ResponseEntity.ok(
                new TopSecretResponseDto(
                    spaceship.position(),
                    spaceship.message()
                )
            ))
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
