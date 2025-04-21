package com.rebels.quasar.repository;

import com.rebels.quasar.model.Satellite;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author FDip
 */

public interface SatelliteRepository {
    Optional<Satellite> findByName(String name);
    List<Satellite> findAll();
}