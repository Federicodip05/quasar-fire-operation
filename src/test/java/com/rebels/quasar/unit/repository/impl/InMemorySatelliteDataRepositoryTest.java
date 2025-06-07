package com.rebels.quasar.unit.repository.impl;

import com.rebels.quasar.dto.request.SatelliteDataDto;
import com.rebels.quasar.repository.impl.InMemorySatelliteDataRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 *
 * @author FDip
 */
class InMemorySatelliteDataRepositoryTest {

    private InMemorySatelliteDataRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemorySatelliteDataRepository();
    }

    @Test
    @DisplayName("save debería guardar datos correctamente")
    void shouldSaveSatelliteData() {
        SatelliteDataDto data = new SatelliteDataDto("kenobi", 100.5f, List.of("este", "", "mensaje"));
        repository.save("kenobi", data);

        Optional<SatelliteDataDto> result = repository.findByName("kenobi");

        assertAll(
            () -> assertTrue(result.isPresent()),
            () -> assertEquals("kenobi", result.get().name()),
            () -> assertEquals(100.5f, result.get().distance()),
            () -> assertEquals(List.of("este", "", "mensaje"), result.get().message())
        );
    }

    @Test
    @DisplayName("save debería lanzar excepción si el nombre es nulo")
    void shouldThrowIfNameIsNull() {
        SatelliteDataDto data = new SatelliteDataDto("kenobi", 100f, List.of("msg"));

        NullPointerException exception = assertThrows(NullPointerException.class,
            () -> repository.save(null, data));

        assertEquals("El nombre no puede ser nulo", exception.getMessage());
    }

    @Test
    @DisplayName("save debería lanzar excepción si los datos son nulos")
    void shouldThrowIfDataIsNull() {
        NullPointerException exception = assertThrows(NullPointerException.class,
            () -> repository.save("kenobi", null));

        assertEquals("Los datos no pueden ser nulos", exception.getMessage());
    }
    
    @Test
    @DisplayName("findByName debería ignorar mayúsculas/minúsculas")
    void shouldFindDataCaseInsensitive() {
        SatelliteDataDto data = new SatelliteDataDto("KenObi", 123.4f, List.of("msg"));
        repository.save("KenObi", data);

        Optional<SatelliteDataDto> result = repository.findByName("kenobi");

        assertTrue(result.isPresent());
        assertEquals(123.4f, result.get().distance());
    }

    @Test
    @DisplayName("findByName debería retornar vacío si no existe")
    void shouldReturnEmptyForUnknownName() {
        Optional<SatelliteDataDto> result = repository.findByName("unknown");

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("findAll debería retornar todos los satélites guardados")
    void shouldReturnAllStoredData() {
        repository.save("kenobi", new SatelliteDataDto("kenobi", 100f, List.of("a")));
        repository.save("sato", new SatelliteDataDto("sato", 200f, List.of("b")));

        Map<String, SatelliteDataDto> all = repository.findAll();

        assertAll(
            () -> assertEquals(2, all.size()),
            () -> assertTrue(all.containsKey("kenobi")),
            () -> assertTrue(all.containsKey("sato"))
        );
    }

    @Test
    @DisplayName("findAll debería devolver una copia modificable (no afecta al repositorio original)")
    void shouldReturnDefensiveCopy() {
        repository.save("kenobi", new SatelliteDataDto("kenobi", 100f, List.of("msg")));

        Map<String, SatelliteDataDto> all = repository.findAll();
        all.clear(); // No debería afectar al repo original

        assertFalse(repository.findAll().isEmpty(), "El repositorio no debe verse afectado por cambios externos");
    }
}
