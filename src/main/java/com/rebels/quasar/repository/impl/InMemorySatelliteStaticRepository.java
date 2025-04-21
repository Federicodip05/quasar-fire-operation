package com.rebels.quasar.repository.impl;

import com.rebels.quasar.model.Position;
import com.rebels.quasar.model.Satellite;
import com.rebels.quasar.repository.SatelliteStaticRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author FDip
 */
@Repository
public class InMemorySatelliteStaticRepository implements SatelliteStaticRepository {
    private static final Map<String, Satellite> SATELLITES = new ConcurrentHashMap<>();
    
    static {
        SATELLITES.put("kenobi", new Satellite("kenobi", new Position(-500, -200)));
        SATELLITES.put("skywalker", new Satellite("skywalker", new Position(100, -100)));
        SATELLITES.put("sato", new Satellite("sato", new Position(500, 100)));
    }

    @Override
    public Optional<Satellite> findByName(String name) {
        return Optional.ofNullable(SATELLITES.get(name.toLowerCase()));
    }

    @Override
    public List<Satellite> findAll() {
        return List.copyOf(SATELLITES.values());
    }
}
