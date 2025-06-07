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
            () -> assertEquals(now, dto.timestamp(), "El timestamp no coincide"),  
            () -> assertEquals(404, dto.status(), "El status no coincide"),       
            () -> assertEquals("Not Found", dto.error(), "El error no coincide"),  
            () -> assertEquals("Recurso no encontrado", dto.message(), "El mensaje no coincide")  
        );
    }

    @Test
    @DisplayName("Creación con builder → debe permitir construcción flexible")
    void shouldCreateWithBuilder() {
        LocalDateTime now = LocalDateTime.now();
        
        ErrorResponseDto dto = ErrorResponseDto.builder()
            .timestamp(now)
            .status(400)
            .error("Bad Request")
            .message("Input no válido")
            .build();
        
        assertAll(
            () -> assertEquals(now, dto.timestamp(), "El timestamp no coincide"),
            () -> assertEquals(400, dto.status(), "El status no coincide"),
            () -> assertEquals("Bad Request", dto.error(), "El error no coincide"),
            () -> assertEquals("Input no válido", dto.message(), "El mensaje no coincide")
        );
    }
}
