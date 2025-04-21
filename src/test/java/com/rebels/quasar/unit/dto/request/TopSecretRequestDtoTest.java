package com.rebels.quasar.unit.dto.request;

import com.rebels.quasar.dto.request.SatelliteDataDto;
import com.rebels.quasar.dto.request.TopSecretRequestDto;
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
class TopSecretRequestDtoTest {
    private static Validator validator;

    // Helper method para crear satélites válidos
    private static SatelliteDataDto createValidSatellite(String name) {
        return SatelliteDataDto.builder()
                .name(name)
                .distance(100.0f)
                .message(List.of("este", "", "mensaje"))
                .build();
    }

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldAcceptWhenThreeValidSatellites() {
        TopSecretRequestDto dto = new TopSecretRequestDto(
            List.of(
                createValidSatellite("kenobi"),
                createValidSatellite("skywalker"),
                createValidSatellite("sato")
            )
        );

        Set<ConstraintViolation<TopSecretRequestDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "Debería aceptar 3 satélites válidos");
    }

    @Test
    void shouldRejectWhenLessThanThreeSatellites() {
        TopSecretRequestDto dto = new TopSecretRequestDto(
            List.of(
                createValidSatellite("kenobi"),
                createValidSatellite("skywalker")
            )
        );

        Set<ConstraintViolation<TopSecretRequestDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty(), "Debería rechazar menos de 3 satélites");
        assertEquals("Se necesitan exactamente 3 satélites", violations.iterator().next().getMessage());
    }

    @Test
    void shouldRejectWhenMoreThanThreeSatellites() {
        TopSecretRequestDto dto = new TopSecretRequestDto(
            List.of(
                createValidSatellite("kenobi"),
                createValidSatellite("skywalker"),
                createValidSatellite("sato"),
                createValidSatellite("extra")
            )
        );

        Set<ConstraintViolation<TopSecretRequestDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty(), "Debería rechazar más de 3 satélites");
        assertEquals("Se necesitan exactamente 3 satélites", violations.iterator().next().getMessage());
    }

    @Test
    void shouldRejectWhenAnySatelliteIsInvalid() {
        TopSecretRequestDto dto = new TopSecretRequestDto(
            List.of(
                createValidSatellite("kenobi"),
                SatelliteDataDto.builder()
                    .message(List.of("", "mensaje"))
                    .build(),
                createValidSatellite("sato")
            )
        );

        Set<ConstraintViolation<TopSecretRequestDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty(), "Debería rechazar si algún satélite es inválido");
        assertTrue(violations.stream()
            .anyMatch(v -> v.getMessage().contains("no puede")));
    }


    @Test
    void shouldRejectNullSatellitesList() {
        TopSecretRequestDto dto = new TopSecretRequestDto(null);

        Set<ConstraintViolation<TopSecretRequestDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty(), "Debería rechazar lista nula");
        assertEquals("La lista de satélites no puede ser nula", violations.iterator().next().getMessage());
    }

    @Test
    void shouldRejectWhenSatelliteMessagesAreCompletelyEmpty() {
        TopSecretRequestDto dto = new TopSecretRequestDto(
            List.of(
                SatelliteDataDto.builder()
                    .name("kenobi")
                    .distance(100.0f)
                    .message(List.of())
                    .build(),
                createValidSatellite("skywalker"),
                createValidSatellite("sato")
            )
        );

        Set<ConstraintViolation<TopSecretRequestDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty(), "Debería rechazar mensajes vacíos");
    }
}
