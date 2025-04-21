package com.rebels.quasar.unit.model;

import com.rebels.quasar.model.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author FDip
 */
public class PositionTest {
   
    @Test
    @DisplayName("Creación de Position → debe contener coordenadas x e y correctas")
    void shouldCreateRecordWithComponents() {
        Position position = new Position(-10.0f, 97.5f);
        
        assertAll(
            () -> assertEquals(-10.0f, position.x(), "La coordenada x no coincide"),
            () -> assertEquals(97.5f, position.y(), "La coordenada y no coincide")
        );
    }
}
