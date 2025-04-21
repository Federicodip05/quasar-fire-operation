package com.rebels.quasar.unit.model;

import com.rebels.quasar.model.Position;
import com.rebels.quasar.model.Spaceship;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author FDip
 */
class SpaceshipTest {
    
    @Test
    @DisplayName("Creación de Spaceship → debe contener posición y mensaje correctos")
    void shouldCreateRecordWithComponents() {
        Position position = new Position(-100.0f, 100.0f);
        Spaceship spaceship = new Spaceship(position, "este es un mensaje de test");
        
        assertAll(
            () -> assertEquals(position, spaceship.position(), "La posición no coincide"),
            () -> assertEquals("este es un mensaje de test", spaceship.message(), "El mensaje no coincide")
        );
    }
}
