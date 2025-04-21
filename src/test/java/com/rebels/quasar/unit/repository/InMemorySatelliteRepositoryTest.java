package com.rebels.quasar.unit.repository;

import com.rebels.quasar.model.Position;
import com.rebels.quasar.model.Satellite;
import com.rebels.quasar.repository.InMemorySatelliteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author FDip
 */
public class InMemorySatelliteRepositoryTest {

    private InMemorySatelliteRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemorySatelliteRepository();
    }

    @Test
    @DisplayName("findByName debería retornar satélite existente")
    void shouldFindExistingSatellite() {
        Optional<Satellite> kenobi = repository.findByName("kenobi");
        
        assertAll(
            () -> assertTrue(kenobi.isPresent()),
            () -> assertEquals("kenobi", kenobi.get().name()),
            () -> assertEquals(-500, kenobi.get().position().x()),
            () -> assertEquals(-200, kenobi.get().position().y())
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "unknown"})
    @DisplayName("findByName debería retornar Optional vacío para nombres no existentes")
        void shouldReturnEmptyForNonExistingSatellite(String invalidName) {
        Optional<Satellite> result = repository.findByName(invalidName);
        assertTrue(result.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"kenobi", "KENOBI", "KeNoBi"})
    @DisplayName("findByName debería encontrar satélite independientemente del casing")
    void shouldFindSatelliteCaseInsensitive(String nameVariation) {
        Optional<Satellite> result = repository.findByName(nameVariation);
    
        assertAll(
            () -> assertTrue(result.isPresent()),
            () -> assertEquals("kenobi", result.get().name()),
            () -> assertEquals(new Position(-500, -200), result.get().position())
        );
    }

    @Test
    @DisplayName("findAll debería retornar todos los satélites")
    void shouldReturnAllSatellites() {
        List<Satellite> satellites = repository.findAll();
        
        assertAll(
            () -> assertEquals(3, satellites.size()),
            () -> assertTrue(satellites.stream().anyMatch(s -> s.name().equals("kenobi"))),
            () -> assertTrue(satellites.stream().anyMatch(s -> s.name().equals("skywalker"))),
            () -> assertTrue(satellites.stream().anyMatch(s -> s.name().equals("sato")))
        );
    }

    @Test
    @DisplayName("findAll debería retornar lista inmutable")
    void shouldReturnImmutableList() {
        List<Satellite> satellites = repository.findAll();
        
        assertThrows(UnsupportedOperationException.class, () -> {
            satellites.add(new Satellite("new", new Position(0, 0)));
        });
    }

    @Test
    @DisplayName("Los datos iniciales deberían tener las posiciones correctas")
    void shouldHaveCorrectInitialPositions() {
        Optional<Satellite> skywalker = repository.findByName("skywalker");
        Optional<Satellite> sato = repository.findByName("sato");
        
        assertAll(
            () -> assertEquals(100, skywalker.get().position().x()),
            () -> assertEquals(-100, skywalker.get().position().y()),
            () -> assertEquals(500, sato.get().position().x()),
            () -> assertEquals(100, sato.get().position().y())
        );
    }
}
