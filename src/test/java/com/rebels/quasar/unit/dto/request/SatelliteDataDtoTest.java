package com.rebels.quasar.unit.dto.request;

import com.rebels.quasar.dto.request.SatelliteDataDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author FDip
 */
class SatelliteDataDtoTest {
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Creación de DTO válido → no debe tener violaciones de validación")
    void shouldCreateValidSatelliteDataDto() {
        SatelliteDataDto dto = SatelliteDataDto.builder()
            .name("kenobi")
            .distance(100.0f)
            .message(List.of("este", "", "", "mensaje", ""))
            .build();

        Set<ConstraintViolation<SatelliteDataDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Nombre nulo → debe fallar validación con mensaje adecuado")
    void shouldFailWhenNameIsNull() {
        SatelliteDataDto dto = SatelliteDataDto.builder()
            .name(null)  // Nombre nulo
            .distance(100.0f)
            .message(List.of("este", "", "mensaje"))
            .build();

        Set<ConstraintViolation<SatelliteDataDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals("El nombre del satélite no puede estar vacío", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Distancia nula → debe fallar validación con mensaje adecuado")
    void shouldFailWhenDistanceIsNull() {
        SatelliteDataDto dto = SatelliteDataDto.builder()
            .name("kenobi")
            .distance(null)  // Distancia nula
            .message(List.of("este", "", "mensaje"))
            .build();

        Set<ConstraintViolation<SatelliteDataDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals("La distancia a la nave no puede ser nula", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Distancia negativa → debe fallar validación con mensaje adecuado")
    void shouldFailWhenDistanceIsNegative() {
        SatelliteDataDto dto = SatelliteDataDto.builder()
            .name("kenobi")
            .distance(-100.0f)  // Distancia negativa
            .message(List.of("este", "", "mensaje"))
            .build();

        Set<ConstraintViolation<SatelliteDataDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals("La distancia no puede ser negativa", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Mensaje con strings vacíos → debe ser válido")
    void shouldAllowAllEmptyStringsInMessage() {
        SatelliteDataDto dto = SatelliteDataDto.builder()
            .name("kenobi")
            .distance(100.0f)
            .message(List.of("", "", ""))  // Strings vacíos permitidos
            .build();

        Set<ConstraintViolation<SatelliteDataDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Mensaje nulo → debe fallar validación con mensaje adecuado")
    void shouldFailWhenMessageIsNull() {
        SatelliteDataDto dto = SatelliteDataDto.builder()
            .name("kenobi")
            .distance(100.0f)
            .message(null)  // Mensaje nulo
            .build();

        Set<ConstraintViolation<SatelliteDataDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals("El mensaje no puede estar vacío", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Mensaje vacío → debe fallar validación con mensaje adecuado")
    void shouldFailWhenMessageIsEmpty() {
        SatelliteDataDto dto = SatelliteDataDto.builder()
            .name("kenobi")
            .distance(100.0f)
            .message(List.of())
            .build();

        Set<ConstraintViolation<SatelliteDataDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals("El mensaje no puede estar vacío", violations.iterator().next().getMessage());
    }
}
