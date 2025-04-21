package com.rebels.quasar.unit.service;

import com.rebels.quasar.dto.request.SatelliteDataDto;
import com.rebels.quasar.dto.request.TopSecretSplitRequestDto;
import com.rebels.quasar.exception.CommunicationException;
import com.rebels.quasar.model.Position;
import com.rebels.quasar.model.Satellite;
import com.rebels.quasar.model.Spaceship;
import com.rebels.quasar.repository.SatelliteDataRepository;
import com.rebels.quasar.repository.SatelliteStaticRepository;
import com.rebels.quasar.service.LocationService;
import com.rebels.quasar.service.MessageService;
import com.rebels.quasar.service.TopSecretSplitService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

/**
 *
 * @author FDip
 */
@ExtendWith(MockitoExtension.class)
class TopSecretSplitServiceTest {

    @Mock
    private SatelliteStaticRepository satelliteStaticRepository;

    @Mock
    private SatelliteDataRepository satelliteDataRepository;

    @Mock
    private LocationService locationService;

    @Mock
    private MessageService messageService;

    @InjectMocks
    private TopSecretSplitService topSecretSplitService;

    private final String SATELLITE_1 = "kenobi";
    private final String SATELLITE_2 = "skywalker";
    private final String SATELLITE_3 = "sato";

    @Test
    @DisplayName("saveSatelliteData con satélite válido → debería guardar datos sin errores")
    void shouldSaveSatelliteDataSuccessfully() {
        when(satelliteStaticRepository.findByName(SATELLITE_1))
            .thenReturn(Optional.of(new Satellite(SATELLITE_1, new Position(0, 0))));

        TopSecretSplitRequestDto request = new TopSecretSplitRequestDto(
            100.0f, 
            Arrays.asList("este", "", "", "mensaje", "")
        );

        assertDoesNotThrow(() -> 
            topSecretSplitService.saveSatelliteData(SATELLITE_1, request)
        );

        verify(satelliteStaticRepository).findByName(SATELLITE_1);
        verify(satelliteDataRepository).save(eq(SATELLITE_1.toLowerCase()), any(SatelliteDataDto.class));
    }

    @Test
    @DisplayName("getSpaceshipData con datos completos → debería retornar Spaceship")
    void shouldReturnSpaceshipWithCompleteData() {
        Map<String, SatelliteDataDto> fakeDataMap = Map.of(
            SATELLITE_1, new SatelliteDataDto(SATELLITE_1, 100.0f, Arrays.asList("este", "", "", "mensaje", "")),
            SATELLITE_2, new SatelliteDataDto(SATELLITE_2, 115.5f, Arrays.asList("", "es", "", "", "secreto")),
            SATELLITE_3, new SatelliteDataDto(SATELLITE_3, 142.7f, Arrays.asList("este", "", "un", "", ""))
        );

        when(satelliteDataRepository.findAll()).thenReturn(fakeDataMap);
        when(locationService.calculatePosition(anyMap()))
            .thenReturn(new Position(-487.3f, 1557.0f));
        when(messageService.decodeMessage(anyList()))
            .thenReturn("este es un mensaje secreto");

        Optional<Spaceship> result = assertDoesNotThrow(() -> 
            topSecretSplitService.getSpaceshipData()
        );

        assertTrue(result.isPresent());
        assertEquals(-487.3f, result.get().position().x(), 0.01f);
        assertEquals(1557.0f, result.get().position().y(), 0.01f);
        assertEquals("este es un mensaje secreto", result.get().message());
    }

    @Test
    @DisplayName("getSpaceshipData con menos de 3 satélites → debería lanzar excepción")
    void shouldThrowExceptionWithInsufficientData() {
        when(satelliteDataRepository.findAll()).thenReturn(Map.of(
            SATELLITE_1, new SatelliteDataDto(SATELLITE_1, 100.0f, List.of("hola"))
        ));

        CommunicationException exception = assertThrows(CommunicationException.class,
            () -> topSecretSplitService.getSpaceshipData());

        assertTrue(exception.getMessage().contains("Información insuficiente"));
    }

    @Test
    @DisplayName("getSpaceshipData con error en locationService → debería propagar excepción")
    void shouldPropagateLocationException() {
        Map<String, SatelliteDataDto> fakeDataMap = Map.of(
            SATELLITE_1, new SatelliteDataDto(SATELLITE_1, 100.0f, List.of("")),
            SATELLITE_2, new SatelliteDataDto(SATELLITE_2, 115.5f, List.of("")),
            SATELLITE_3, new SatelliteDataDto(SATELLITE_3, 142.7f, List.of(""))
        );

        when(satelliteDataRepository.findAll()).thenReturn(fakeDataMap);
        when(locationService.calculatePosition(anyMap()))
            .thenThrow(new CommunicationException("Error de prueba"));

        CommunicationException exception = assertThrows(CommunicationException.class,
            () -> topSecretSplitService.getSpaceshipData());

        assertEquals("Error de prueba", exception.getMessage());
    }
}