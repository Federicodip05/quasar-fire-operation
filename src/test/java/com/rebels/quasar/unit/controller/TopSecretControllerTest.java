package com.rebels.quasar.unit.controller;

import com.rebels.quasar.controller.TopSecretController;
import com.rebels.quasar.dto.request.TopSecretRequestDto;
import com.rebels.quasar.dto.request.SatelliteDataDto;
import com.rebels.quasar.dto.response.TopSecretResponseDto;
import com.rebels.quasar.exception.CommunicationException;
import com.rebels.quasar.model.Position;
import com.rebels.quasar.model.Spaceship;
import com.rebels.quasar.service.TopSecretService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 *
 * @author FDip
 */
@ExtendWith(MockitoExtension.class)
class TopSecretControllerTest {

    @Mock
    private TopSecretService topSecretService;

    @InjectMocks
    private TopSecretController topSecretController;

    private TopSecretRequestDto createValidRequest() {
        return new TopSecretRequestDto(List.of(
            new SatelliteDataDto("kenobi", 100.0f, List.of("este", "", "mensaje")),
            new SatelliteDataDto("skywalker", 115.5f, List.of("", "es", "", "secreto")),
            new SatelliteDataDto("sato", 142.7f, List.of("este", "", "un", ""))
        ));
    }

    @Test
    @DisplayName("POST /topsecret con datos válidos → debería retornar 200")
    void shouldReturn200WithResponse() throws Exception {
        Spaceship mockSpaceship = new Spaceship(new Position(-487.3f, 1557.1f), "este es un mensaje secreto");
        when(topSecretService.process(any())).thenReturn(mockSpaceship);

        ResponseEntity<TopSecretResponseDto> response = topSecretController.processTopSecret(createValidRequest());

        assertAll(
            () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
            () -> assertEquals(mockSpaceship.position(), response.getBody().position()),
            () -> assertEquals(mockSpaceship.message(), response.getBody().message())
        );
    }

    @Test
    @DisplayName("Cuando servicio lanza excepción → debería propagarla")
    void shouldPropagateServiceException() {
        when(topSecretService.process(any()))
            .thenThrow(new CommunicationException("Error de servicio"));

        CommunicationException exception = assertThrows(CommunicationException.class,
            () -> topSecretController.processTopSecret(createValidRequest()));

        assertEquals("Error de servicio", exception.getMessage());
    }
}
