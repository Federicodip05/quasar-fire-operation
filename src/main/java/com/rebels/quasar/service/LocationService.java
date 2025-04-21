package com.rebels.quasar.service;

import com.rebels.quasar.exception.CommunicationException;
import com.rebels.quasar.model.Position;
import java.util.Map;

/**
 *
 * @author FDip
 */
public interface LocationService {
    Position calculatePosition(Map<String, Float> satelliteDistances) throws CommunicationException;
}
