package com.rebels.quasar.unit.dto.response;

import com.rebels.quasar.dto.response.TopSecretResponseDto;
import com.rebels.quasar.model.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author FDip
 */
public class TopSecretResponseDtoTest {
    
    @Test
    @DisplayName("Creación de TopSecretResponseDto → debe contener posición y mensaje correctos")
    void shouldCreateRecordWithComponents() {
        Position position = new Position(10.0f, 20.0f);
        TopSecretResponseDto dto = new TopSecretResponseDto(position, "mensaje secreto");
        
        assertAll(
            () -> assertEquals(position, dto.position(), "La posición no coincide"),
            () -> assertEquals("mensaje secreto", dto.message(), "El mensaje no coincide")
        );
    }
}
