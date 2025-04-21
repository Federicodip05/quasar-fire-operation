package com.rebels.quasar.service.impl;

import com.rebels.quasar.exception.CommunicationException;
import com.rebels.quasar.model.Position;
import com.rebels.quasar.model.Satellite;
import com.rebels.quasar.service.LocationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 *
 * @author FDip
 */
@Slf4j
@Service
public class LocationServiceImpl implements LocationService {

    private static final Satellite KENOBI = new Satellite("kenobi", new Position(-500, -200));
    private static final Satellite SKYWALKER = new Satellite("skywalker", new Position(100, -100));
    private static final Satellite SATO = new Satellite("sato", new Position(500, 100));

    @Override
    public Position calculatePosition(Map<String, Float> satelliteDistances) throws CommunicationException {
        
        log.info("Iniciando calculo de la posición de la nave");
        validateInput(satelliteDistances);

        // Obtener distancias por nombre
        float distanceToKenobi = satelliteDistances.get("kenobi");
        float distanceToSkywalker = satelliteDistances.get("skywalker");
        float distanceToSato = satelliteDistances.get("sato");

        // Coeficientes ecuación Kenobi-Skywalker
        float kenobiSkywalkerXCoef = 2 * (SKYWALKER.position().x() - KENOBI.position().x());
        float kenobiSkywalkerYCoef = 2 * (SKYWALKER.position().y() - KENOBI.position().y());
        float kenobiSkywalkerConstTerm = calculateConstantTerm(
            distanceToKenobi, distanceToSkywalker,
            KENOBI.position(), SKYWALKER.position()
        );
        
        // Coeficientes ecuación Skywalker-Sato
        float skywalkerSatoXCoef = 2 * (SATO.position().x() - SKYWALKER.position().x());
        float skywalkerSatoYCoef = 2 * (SATO.position().y() - SKYWALKER.position().y());
        float skywalkerSatoConstTerm = calculateConstantTerm(
            distanceToSkywalker, distanceToSato,
            SKYWALKER.position(), SATO.position()
        );

        // Resolución del sistema lineal
        float determinant = calculateDeterminant(
            kenobiSkywalkerXCoef, kenobiSkywalkerYCoef,
            skywalkerSatoXCoef, skywalkerSatoYCoef
        );
        
        validateDeterminant(determinant);
        
        float spaceshipX = calculateSpaceshipCoordinate(
            kenobiSkywalkerConstTerm, kenobiSkywalkerYCoef,
            skywalkerSatoConstTerm, skywalkerSatoYCoef,
            determinant
        );
        
        float spaceshipY = calculateSpaceshipCoordinate(
            kenobiSkywalkerXCoef, kenobiSkywalkerConstTerm,
            skywalkerSatoXCoef, skywalkerSatoConstTerm,
            determinant
        );

        log.info("Posición calculada exitosamente");
        
        return new Position(round(spaceshipX), round(spaceshipY));
    }

    private void validateInput(Map<String, Float> distances) throws CommunicationException {
        if (distances == null) {
            log.error("El mapa de distancias es nulo");
            throw new CommunicationException("El mapa de distancias no puede ser nulo");
        }

        if (!distances.containsKey("kenobi") || 
            !distances.containsKey("skywalker") || 
            !distances.containsKey("sato")) {
            log.error("Faltan distancias a satélites para poder realizar el calculo");
            throw new CommunicationException("Se requieren distancias para los 3 satélites (kenobi, skywalker, sato)");
        }
    }

    private void validateDeterminant(float determinant) throws CommunicationException {
        if (Math.abs(determinant) < 1e-10) {
            log.error("Determinante inválido para realizar el cálculo");
            throw new CommunicationException("Configuración de satélites inválida para cálculo");
        }
    }

    private float calculateConstantTerm(float distance1, float distance2, 
                                      Position pos1, Position pos2) {
        return square(distance1) - square(distance2) 
               - square(pos1.x()) + square(pos2.x()) 
               - square(pos1.y()) + square(pos2.y());
    }

    private float calculateDeterminant(float a, float b, float c, float d) {
        return a * d - b * c;
    }

    private float calculateSpaceshipCoordinate(float a, float b, float c, float d, float det) {
        return (a * d - b * c) / det;
    }

    private float square(float num) {
        return num * num;
    }

    private float round(float value) {
        return Math.round(value * 10) / 10.0f;
    }
}