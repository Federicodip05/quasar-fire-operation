package com.rebels.quasar.service;

import com.rebels.quasar.dto.request.TopSecretSplitRequestDto;
import com.rebels.quasar.model.Spaceship;

import java.util.Optional;

/**
 *
 * @author FDip
 */
public interface TopSecretSplitService {
    void saveSatelliteData(String satelliteName, TopSecretSplitRequestDto request);
    Optional<Spaceship> getSpaceshipData();
}
