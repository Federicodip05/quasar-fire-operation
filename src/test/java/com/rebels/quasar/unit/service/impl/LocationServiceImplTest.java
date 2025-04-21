package com.rebels.quasar.unit.service.impl;

import com.rebels.quasar.exception.CommunicationException;
import com.rebels.quasar.model.Position;
import com.rebels.quasar.service.impl.LocationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author FDip
 */
class LocationServiceImplTest {

    private LocationServiceImpl locationService;

    @BeforeEach
    void setUp() {
        locationService = new LocationServiceImpl();
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

    private static Stream<Object[]> invalidInputProvider() {
        return Stream.of(
            new Object[]{null, "El mapa de distancias no puede ser nulo"},
            new Object[]{Map.of("kenobi", 100f), "Se requieren distancias para los 3 satélites (kenobi, skywalker, sato)"},
            new Object[]{Map.of("kenobi", 100f, "skywalker", 115.5f), "Se requieren distancias para los 3 satélites (kenobi, skywalker, sato)"}
        );
    }
}