package com.rebels.quasar.integration.controller;

import com.rebels.quasar.dto.request.TopSecretSplitRequestDto;
import com.rebels.quasar.dto.response.TopSecretResponseDto;
import org.junit.jupiter.api.*;
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
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Tests de Integración para TopSecretSplitController")
class TopSecretSplitControllerIT {

    private static final float EXPECTED_X = -487.3f;
    private static final float EXPECTED_Y = 1557.0f;
    private static final String EXPECTED_MESSAGE = "este es un mensaje secreto";
    private static final double POSITION_DELTA = 0.001;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @Order(1)
    @DisplayName("POST /topsecret_split/kenobi → debe almacenar correctamente los datos del satélite")
    void saveKenobiData_ShouldStoreDataCorrectly() {
        // Arrange
        TopSecretSplitRequestDto kenobiData = new TopSecretSplitRequestDto(
            100.0f, List.of("este", "", "", "mensaje", "")
        );

        // Act
        ResponseEntity<Void> response = restTemplate.postForEntity(
            "/topsecret_split/kenobi",
            kenobiData,
            Void.class
        );

        // Assert
        assertAll("Verificación de almacenamiento de datos de Kenobi",
            () -> assertEquals(HttpStatus.OK, response.getStatusCode(), 
                "Debería retornar estado 200 OK"),
            () -> assertNull(response.getBody(), 
                "El cuerpo de la respuesta debería ser nulo para éxito")
        );
    }

    @Test
    @Order(2)
    @DisplayName("POST /topsecret_split/skywalker → debe almacenar correctamente los datos del satélite")
    void saveSkywalkerData_ShouldStoreDataCorrectly() {
        // Arrange
        TopSecretSplitRequestDto skywalkerData = new TopSecretSplitRequestDto(
            115.5f, List.of("", "es", "", "", "secreto")
        );

        // Act
        ResponseEntity<Void> response = restTemplate.postForEntity(
            "/topsecret_split/skywalker",
            skywalkerData,
            Void.class
        );

        // Assert
        assertAll("Verificación de almacenamiento de datos de Skywalker",
            () -> assertEquals(HttpStatus.OK, response.getStatusCode(), 
                "Debería retornar estado 200 OK"),
            () -> assertNull(response.getBody(), 
                "El cuerpo de la respuesta debería ser nulo para éxito")
        );
    }

    @Test
    @Order(3) // Ejecutar después de guardar solo 2 satélites
    @DisplayName("GET /topsecret_split con solo 2 satélites → debe retornar error 404")
    void getSpaceshipData_WithTwoSatellites_ShouldReturnNotFound() {
        // Act - Solo tenemos 2 satélites cargados (kenobi y skywalker)
        ResponseEntity<String> response = restTemplate.getForEntity(
            "/topsecret_split",
            String.class
        );
        
        // Assert
        assertAll("Verificación de respuesta con datos incompletos",
            () -> assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(),
                "Debería retornar estado 404 Not Found"),
            () -> assertNotNull(response.getBody(),
                "El cuerpo del error no debería ser nulo"),
            () -> assertTrue(response.getBody().contains("Información insuficiente: Se requieren datos de 3 satélites (actuales: 2)"),
                "El mensaje de error debería indicar falta de información")
        );
    }
    
    @Test
    @Order(4)
    @DisplayName("POST /topsecret_split/sato → debe almacenar correctamente los datos del satélite")
    void saveSatoData_ShouldStoreDataCorrectly() {
        // Arrange
        TopSecretSplitRequestDto satoData = new TopSecretSplitRequestDto(
            142.7f, List.of("este", "", "un", "", "")
        );

        // Act
        ResponseEntity<Void> response = restTemplate.postForEntity(
            "/topsecret_split/sato",
            satoData,
            Void.class
        );

        // Assert
        assertAll("Verificación de almacenamiento de datos de Sato",
            () -> assertEquals(HttpStatus.OK, response.getStatusCode(), 
                "Debería retornar estado 200 OK"),
            () -> assertNull(response.getBody(), 
                "El cuerpo de la respuesta debería ser nulo para éxito")
        );
    }

    @Test
    @Order(5)
    @DisplayName("GET /topsecret_split con datos completos → debe retornar posición y mensaje correctos")
    void getSpaceshipData_WithCompleteData_ShouldReturnPositionAndMessage() {
        // Act (los datos ya fueron guardados en los tests anteriores)
        ResponseEntity<TopSecretResponseDto> response = restTemplate.getForEntity(
            "/topsecret_split",
            TopSecretResponseDto.class
        );

        // Assert
        assertAll("Verificación de respuesta con datos completos",
            () -> assertEquals(HttpStatus.OK, response.getStatusCode(), 
                "Debería retornar estado 200 OK"),
            () -> assertNotNull(response.getBody(), 
                "El cuerpo de la respuesta no debería ser nulo"),
            () -> assertNotNull(response.getBody().position(), 
                "La posición no debería ser nula"),
            () -> assertEquals(EXPECTED_X, response.getBody().position().x(), POSITION_DELTA,
                "La coordenada X no coincide con lo esperado"),
            () -> assertEquals(EXPECTED_Y, response.getBody().position().y(), POSITION_DELTA,
                "La coordenada Y no coincide con lo esperado"),
            () -> assertEquals(EXPECTED_MESSAGE, response.getBody().message(),
                "El mensaje decodificado no coincide")
        );
    }
}
