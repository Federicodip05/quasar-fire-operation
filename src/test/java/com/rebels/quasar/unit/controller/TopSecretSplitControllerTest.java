package com.rebels.quasar.unit.controller;

import com.rebels.quasar.controller.TopSecretSplitController;
import com.rebels.quasar.dto.request.TopSecretSplitRequestDto;
import com.rebels.quasar.dto.response.TopSecretResponseDto;
import com.rebels.quasar.model.Position;
import com.rebels.quasar.model.Spaceship;
import com.rebels.quasar.service.TopSecretSplitService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 *
 * @author FDip
 */
@ExtendWith(MockitoExtension.class)
class TopSecretSplitControllerTest {

    @Mock
    private TopSecretSplitService topSecretSplitService;

    @InjectMocks
    private TopSecretSplitController topSecretSplitController;

    private TopSecretSplitRequestDto createValidRequest() {
        return new TopSecretSplitRequestDto(100.0f, List.of("este", "", "mensaje"));
    }

    @Test
    @DisplayName("POST /topsecret_split/{satellite_name} con datos válidos → debería retornar 200")
    void shouldSaveSatelliteData() {
        // Ejecutar
        ResponseEntity<Void> response = topSecretSplitController.saveSatelliteData(
            "kenobi", createValidRequest());

        // Verificar
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(topSecretSplitService).saveSatelliteData(eq("kenobi"), any(TopSecretSplitRequestDto.class));
    }

    @Test
    @DisplayName("GET /topsecret_split con datos completos → debería retornar 200 con posición y mensaje")
    void shouldGetSpaceshipData() {
        // Configurar mock
        Spaceship mockSpaceship = new Spaceship(new Position(-487.3f, 1557.1f), "este es un mensaje secreto");
        when(topSecretSplitService.getSpaceshipData()).thenReturn(Optional.of(mockSpaceship));

        // Ejecutar
        ResponseEntity<TopSecretResponseDto> response = topSecretSplitController.getSpaceshipData();

        // Verificar
        assertAll(
            () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
            () -> assertEquals(-487.3f, response.getBody().position().x()),
            () -> assertEquals(1557.1f, response.getBody().position().y()),
            () -> assertEquals("este es un mensaje secreto", response.getBody().message())
        );
    }

    @Test
    @DisplayName("GET /topsecret_split sin datos suficientes → debería retornar 404")
    void shouldReturn404WhenNoDataAvailable() {
        // Configurar mock
        when(topSecretSplitService.getSpaceshipData()).thenReturn(Optional.empty());

        // Ejecutar
        ResponseEntity<TopSecretResponseDto> response = topSecretSplitController.getSpaceshipData();

        // Verificar
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}