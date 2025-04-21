package com.rebels.quasar.unit.model;

import com.rebels.quasar.model.Position;
import com.rebels.quasar.model.Satellite;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author FDip
 */
class SatelliteTest {
    
    @Test
    @DisplayName("Creación de Satellite → debe contener nombre y posición correctos")
    void shouldCreateRecordWithComponents() {
        Position position = new Position(100.0f, -200.0f);
        Satellite satellite = new Satellite("kenobi", position);
        
        assertAll(
            () -> assertEquals("kenobi", satellite.name(), "El nombre no coincide"),
            () -> assertEquals(position, satellite.position(), "La posición no coincide")
        );
    }
}
