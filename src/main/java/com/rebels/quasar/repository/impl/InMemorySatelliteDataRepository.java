package com.rebels.quasar.repository.impl;

import com.rebels.quasar.dto.request.SatelliteDataDto;
import com.rebels.quasar.repository.SatelliteDataRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

/**
 *
 * @author FDip
 */
@Repository
public class InMemorySatelliteDataRepository implements SatelliteDataRepository {
    private final Map<String, SatelliteDataDto> dataMap = new ConcurrentHashMap<>();

    @Override
    public void save(String name, SatelliteDataDto data) {
        Objects.requireNonNull(name, "El nombre no puede ser nulo");
        Objects.requireNonNull(data, "Los datos no pueden ser nulos");
        dataMap.put(name.toLowerCase(), data);
    }

    @Override
    public Optional<SatelliteDataDto> findByName(String name) {
        return Optional.ofNullable(dataMap.get(name.toLowerCase()));
    }

    @Override
    public Map<String, SatelliteDataDto> findAll() {
        return new HashMap<>(dataMap);
    }

}
