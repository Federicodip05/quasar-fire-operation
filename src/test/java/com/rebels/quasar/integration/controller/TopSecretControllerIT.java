package com.rebels.quasar.integration.controller;

import com.rebels.quasar.dto.request.TopSecretRequestDto;
import com.rebels.quasar.dto.request.SatelliteDataDto;
import com.rebels.quasar.dto.response.TopSecretResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author FDip
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TopSecretControllerIT {

    private static final double DELTA = 0.001; // Margen de error para comparaciones de float
    
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("POST /topsecret con solicitud válida → debe retornar posición y mensaje correctos")
    void processTopSecret_WithValidRequest_ShouldReturnPositionAndMessage() {
        // Arrange
        TopSecretRequestDto request = new TopSecretRequestDto(List.of(
            new SatelliteDataDto("kenobi", 100.0f, List.of("este", "", "", "mensaje", "")),
            new SatelliteDataDto("skywalker", 115.5f, List.of("", "es", "", "", "secreto")),
            new SatelliteDataDto("sato", 142.7f, List.of("este", "", "un", "", ""))
        ));

        // Valores esperados
        final float expectedX = -487.3f;
        final float expectedY = 1557.0f;
        final String expectedMessage = "este es un mensaje secreto";

        // Act
        ResponseEntity<TopSecretResponseDto> response = restTemplate.postForEntity(
            "/topsecret",
            request,
            TopSecretResponseDto.class
        );

        // Assert
        assertAll("Verificación de respuesta exitosa",
            () -> assertEquals(HttpStatus.OK, response.getStatusCode(), "Código de estado incorrecto"),
            () -> assertNotNull(response.getBody(), "El cuerpo de la respuesta no debe ser nulo"),
            () -> assertNotNull(response.getBody().position(), "La posición no debe ser nula"),
            () -> assertEquals(expectedX, response.getBody().position().x(), DELTA, 
                "Coordenada X no coincide con lo esperado"),
            () -> assertEquals(expectedY, response.getBody().position().y(), DELTA, 
                "Coordenada Y no coincide con lo esperado"),
            () -> assertEquals(expectedMessage, response.getBody().message(), 
                "El mensaje decodificado no coincide")
        );
    }

    @Test
    @DisplayName("POST /topsecret con solicitud inválida (2 satélites) → debe retornar error")
    void processTopSecret_WithInvalidRequest_ShouldReturnBadRequest() {
        // Arrange
        TopSecretRequestDto invalidRequest = new TopSecretRequestDto(List.of(
            new SatelliteDataDto("kenobi", 100.0f, List.of("mensaje")),
            new SatelliteDataDto("skywalker", 115.5f, List.of("mensaje"))
        ));

        // Act
        ResponseEntity<String> response = restTemplate.postForEntity(
            "/topsecret",
            invalidRequest,
            String.class
        );

        // Assert
        assertAll("Verificación de respuesta de error",
            () -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), 
                "Código de estado incorrecto"),
            () -> assertNotNull(response.getBody(), "El cuerpo del error no debe ser nulo"),
            () -> assertTrue(response.getBody().contains("Se necesitan exactamente 3 satélites"), 
                "Mensaje de error incorrecto")
        );
    }
}

