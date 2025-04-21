package com.rebels.quasar.controller;

import com.rebels.quasar.dto.request.TopSecretRequestDto;
import com.rebels.quasar.dto.response.ErrorResponseDto;
import com.rebels.quasar.dto.response.TopSecretResponseDto;
import com.rebels.quasar.model.Spaceship;
import com.rebels.quasar.service.TopSecretService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * 
 * @author FDip
 */
@Slf4j
@RestController
@RequestMapping("/topsecret")
@RequiredArgsConstructor
@Tag(name = "Top Secret API", description = "Endopoint para procesamiento de señales satelitales")
public class TopSecretController {

    private final TopSecretService topSecretService;

    @Operation(
        summary = "Procesar señales satelitales",
        description = "Recibe distancias y mensajes fragmentados desde tres satélites y devuelve la posición de la nave y el mensaje reconstruido."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Datos procesados exitosamente",
                content = @Content(schema = @Schema(implementation = TopSecretResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos",
                content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "No hay suficiente información para determinar la posición y/o el mensaje",
                content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PostMapping
    public ResponseEntity<TopSecretResponseDto> processTopSecret(
            @Valid @RequestBody TopSecretRequestDto request) {

        log.info("Solicitud POST recibida en /topsecret");
        
        Spaceship spaceship = topSecretService.process(request);
        
        log.info("Procesamiento completado exitosamente");
        
        return ResponseEntity.ok(
            new TopSecretResponseDto(
                spaceship.position(),
                spaceship.message()
            )
        );
    }
}