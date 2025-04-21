package com.rebels.quasar.unit.service;

import com.rebels.quasar.dto.request.SatelliteDataDto;
import com.rebels.quasar.dto.request.TopSecretRequestDto;
import com.rebels.quasar.exception.CommunicationException;
import com.rebels.quasar.model.Position;
import com.rebels.quasar.model.Spaceship;
import com.rebels.quasar.service.TopSecretService;
import com.rebels.quasar.service.impl.LocationServiceImpl;
import com.rebels.quasar.service.impl.MessageServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 *
 * @author FDip
 */
@ExtendWith(MockitoExtension.class)
class TopSecretServiceTest {

    @Mock
    private LocationServiceImpl locationService;

    @Mock
    private MessageServiceImpl messageService;

    @InjectMocks
    private TopSecretService topSecretService;

    private TopSecretRequestDto createValidRequest() {
        return new TopSecretRequestDto(List.of(
            new SatelliteDataDto("kenobi", 100.0f, List.of("este", "", "mensaje")),
            new SatelliteDataDto("skywalker", 115.5f, List.of("", "es", "", "secreto")),
            new SatelliteDataDto("sato", 142.7f, List.of("este", "", "un", ""))
        ));
    }

    @Test
    @DisplayName("process() con solicitud válida → debería retornar nave con posición y mensaje")
    void shouldProcessValidRequest() throws CommunicationException {
        // Configurar mocks
        when(locationService.calculatePosition(any(Map.class)))
            .thenReturn(new Position(-100.0f, 75.5f));
        when(messageService.decodeMessage(any(List.class)))
            .thenReturn("este es un mensaje secreto");

        // Ejecutar
        Spaceship result = topSecretService.process(createValidRequest());

        // Verificar
        assertAll(
            () -> assertEquals(-100.0f, result.position().x()),
            () -> assertEquals(75.5f, result.position().y()),
            () -> assertEquals("este es un mensaje secreto", result.message())
        );

        // Verificar interacciones
        verify(locationService).calculatePosition(any(Map.class));
        verify(messageService).decodeMessage(any(List.class));
    }

    @Test
    @DisplayName("process() con menos de 3 satélites → debería lanzar CommunicationException")
    void shouldThrowExceptionWithLessThanThreeSatellites() {
        TopSecretRequestDto invalidRequest = new TopSecretRequestDto(List.of(
            new SatelliteDataDto("kenobi", 100.0f, List.of("mensaje")),
            new SatelliteDataDto("skywalker", 115.5f, List.of("secreto"))
        ));

        CommunicationException exception = assertThrows(CommunicationException.class, () -> {
            topSecretService.process(invalidRequest);
        });

        assertEquals("Se requieren exactamente 3 satélites", exception.getMessage());
        verifyNoInteractions(locationService, messageService);
    }

    @Test
    @DisplayName("process() debería normalizar nombres de satélites a minúsculas")
    void shouldNormalizeSatelliteNames() throws CommunicationException {
        // Configurar request con nombres en diferentes casos
        TopSecretRequestDto request = new TopSecretRequestDto(List.of(
            new SatelliteDataDto("KENOBI", 100.0f, List.of("mensaje")),
            new SatelliteDataDto("SkyWalker", 115.5f, List.of("")),
            new SatelliteDataDto("sato", 142.7f, List.of(""))
        ));

        // Configurar mock para capturar el mapa de distancias
        when(locationService.calculatePosition(any(Map.class)))
            .thenReturn(new Position(0, 0));
        when(messageService.decodeMessage(any(List.class)))
            .thenReturn("mensaje");

        topSecretService.process(request);

        // Verificar que los nombres se normalizaron
        verify(locationService).calculatePosition(
            Map.of(
                "kenobi", 100.0f,
                "skywalker", 115.5f,
                "sato", 142.7f
            )
        );
    }

    @Test
    @DisplayName("process() debería mantener el orden original de los mensajes")
    void shouldPreserveMessageOrder() throws CommunicationException {
        // Configurar mocks
        when(locationService.calculatePosition(any(Map.class)))
            .thenReturn(new Position(0, 0));
        when(messageService.decodeMessage(any(List.class)))
            .thenReturn("mensaje decodificado");

        topSecretService.process(createValidRequest());

        // Verificar que se pasaron los mensajes en el orden correcto
        verify(messageService).decodeMessage(
            List.of(
                List.of("este", "", "mensaje"),
                List.of("", "es", "", "secreto"),
                List.of("este", "", "un", "")
            )
        );
    }
}
