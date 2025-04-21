package com.rebels.quasar.unit.dto.request;

import com.rebels.quasar.dto.request.TopSecretSplitRequestDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author FDip
 */
class TopSecretSplitRequestDtoTest {
    private static Validator validator;
    
    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldCreateValidTopSecretSplitRequestDto() {
        TopSecretSplitRequestDto dto = new TopSecretSplitRequestDto(
            100.0f,
            List.of("este", "", "mensaje")
        );

        Set<ConstraintViolation<TopSecretSplitRequestDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldFailWhenDistanceIsNull() {
        TopSecretSplitRequestDto dto = new TopSecretSplitRequestDto(
            null,
            List.of("mensaje")
        );

        Set<ConstraintViolation<TopSecretSplitRequestDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("La distancia a la nave no puede ser nula", violations.iterator().next().getMessage());
    }

    
    @Test
    void shouldFailWhenDistanceIsNegative() {
        TopSecretSplitRequestDto dto = new TopSecretSplitRequestDto(
            -100.0f,
            List.of("mensaje")
        );

        Set<ConstraintViolation<TopSecretSplitRequestDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("La distancia no puede ser negativa", violations.iterator().next().getMessage());
    }

    @Test
    void shouldFailWhenMessageIsEmpty() {
        TopSecretSplitRequestDto dto = new TopSecretSplitRequestDto(
            100.0f,
            List.of()
        );

        Set<ConstraintViolation<TopSecretSplitRequestDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("El mensaje no puede estar vacío", violations.iterator().next().getMessage());
    }
}
