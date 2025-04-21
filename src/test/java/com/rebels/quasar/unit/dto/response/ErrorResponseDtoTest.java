package com.rebels.quasar.unit.dto.response;

import com.rebels.quasar.dto.response.ErrorResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author FDip
 */
class ErrorResponseDtoTest {

    @Test
    @DisplayName("Creación con constructor completo → debe inicializar todos los campos correctamente")
    void shouldCreateWithAllFields() {
        LocalDateTime now = LocalDateTime.now();
        ErrorResponseDto dto = new ErrorResponseDto(now, 404, "Not Found", "Recurso no encontrado");
        
        assertAll(
            () -> assertEquals(now, dto.getTimestamp(), "El timestamp no coincide"),
            () -> assertEquals(404, dto.getStatus(), "El status no coincide"),
            () -> assertEquals("Not Found", dto.getError(), "El error no coincide"),
            () -> assertEquals("Recurso no encontrado", dto.getMessage(), "El mensaje no coincide")
        );
    }

    @Test
    @DisplayName("Creación con constructor vacío y setters → debe permitir asignación de valores")
    void shouldCreateWithNoArgsAndSetters() {
        ErrorResponseDto dto = new ErrorResponseDto();
        LocalDateTime now = LocalDateTime.now();
        
        dto.setTimestamp(now);
        dto.setStatus(400);
        dto.setError("Bad Request");
        dto.setMessage("Input no válido");
        
        assertAll(
            () -> assertEquals(now, dto.getTimestamp(), "El timestamp no coincide"),
            () -> assertEquals(400, dto.getStatus(), "El status no coincide"),
            () -> assertEquals("Bad Request", dto.getError(), "El error no coincide"),
            () -> assertEquals("Input no válido", dto.getMessage(), "El mensaje no coincide")
        );
    }
}
