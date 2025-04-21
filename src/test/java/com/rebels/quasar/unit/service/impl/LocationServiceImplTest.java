package com.rebels.quasar.unit.service.impl;

import com.rebels.quasar.exception.CommunicationException;
import com.rebels.quasar.model.Position;
import com.rebels.quasar.model.Satellite;
import com.rebels.quasar.repository.SatelliteStaticRepository;
import com.rebels.quasar.service.impl.LocationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.provider.Arguments;
import org.mockito.Mock;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 *
 * @author FDip
 */
@ExtendWith(MockitoExtension.class)
class LocationServiceImplTest {

    @Mock
    private SatelliteStaticRepository satelliteStaticRepository;

    private LocationServiceImpl locationService;

    @BeforeEach
    void setUp() {
        locationService = new LocationServiceImpl(satelliteStaticRepository);

        // Setup por defecto para los 3 satélites
        lenient().when(satelliteStaticRepository.findByName("kenobi"))
            .thenReturn(Optional.of(new Satellite("kenobi", new Position(-500, -200))));
        lenient().when(satelliteStaticRepository.findByName("skywalker"))
            .thenReturn(Optional.of(new Satellite("skywalker", new Position(100, -100))));
        lenient().when(satelliteStaticRepository.findByName("sato"))
            .thenReturn(Optional.of(new Satellite("sato", new Position(500, 100))));
    }

    @Test
    @DisplayName("calculatePosition con distancias válidas → debería retornar posición correcta")
    void shouldCalculateCorrectPosition() throws CommunicationException {
        Map<String, Float> distances = Map.of(
            "kenobi", 100.0f,
            "skywalker", 115.5f,
            "sato", 142.7f
        );

        Position result = locationService.calculatePosition(distances);

        assertAll(
            () -> assertEquals(-487.3f, result.x(), 0.1f, "Coordenada X incorrecta"),
            () -> assertEquals(1557.1f, result.y(), 0.1f, "Coordenada Y incorrecta")
        );
    }

    @Test
    @DisplayName("calculatePosition debería redondear a 1 decimal")
    void shouldRoundToSingleDecimal() throws CommunicationException {
        Map<String, Float> distances = Map.of(
            "kenobi", 100.123f,
            "skywalker", 115.456f,
            "sato", 142.789f
        );

        Position result = locationService.calculatePosition(distances);

        assertAll(
            () -> {
                String xStr = String.valueOf(result.x());
                int decimalPlaces = xStr.substring(xStr.indexOf(".") + 1).length();
                assertTrue(decimalPlaces <= 1, "Coordenada X debe tener máximo 1 decimal");
            },
            () -> {
                String yStr = String.valueOf(result.y());
                int decimalPlaces = yStr.substring(yStr.indexOf(".") + 1).length();
                assertTrue(decimalPlaces <= 1, "Coordenada Y debe tener máximo 1 decimal");
            }
        );
    }

    @ParameterizedTest
    @MethodSource("invalidInputProvider")
    @DisplayName("calculatePosition con entradas inválidas → debería lanzar CommunicationException")
    void shouldThrowExceptionWithInvalidInput(Map<String, Float> invalidInput, String expectedMessage) {
        CommunicationException exception = assertThrows(CommunicationException.class,
            () -> locationService.calculatePosition(invalidInput));

        assertEquals(expectedMessage, exception.getMessage());
    }

    private static Stream<Arguments> invalidInputProvider() {
        return Stream.of(
            Arguments.of(null, "El mapa de distancias no puede ser nulo"),
            Arguments.of(Map.of("kenobi", 100f), "Se requieren distancias para los 3 satélites (kenobi, skywalker, sato)"),
            Arguments.of(Map.of("kenobi", 100f, "skywalker", 115.5f), "Se requieren distancias para los 3 satélites (kenobi, skywalker, sato)")
        );
    }

    @Test
    @DisplayName("Si falta algún satélite en el repositorio → debe lanzar CommunicationException")
    void shouldThrowIfSatelliteNotFoundInRepository() {
        when(satelliteStaticRepository.findByName("kenobi")).thenReturn(Optional.empty());

        Map<String, Float> distances = Map.of(
            "kenobi", 100f,
            "skywalker", 115.5f,
            "sato", 142.7f
        );

        CommunicationException exception = assertThrows(CommunicationException.class,
            () -> locationService.calculatePosition(distances));

        assertEquals("No se encontró el satélite kenobi", exception.getMessage());
    }
}