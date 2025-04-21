package com.rebels.quasar.repository;

import com.rebels.quasar.dto.request.SatelliteDataDto;
import java.util.Map;
import java.util.Optional;

/**
 *
 * @author FDip
 */
public interface SatelliteDataRepository {
    void save(String name, SatelliteDataDto data);
    Optional<SatelliteDataDto> findByName(String name);
    Map<String, SatelliteDataDto> findAll();
}
