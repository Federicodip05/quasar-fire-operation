package com.rebels.quasar.service.impl;

import com.rebels.quasar.exception.CommunicationException;
import com.rebels.quasar.dto.request.SatelliteDataDto;
import com.rebels.quasar.dto.request.TopSecretSplitRequestDto;
import com.rebels.quasar.model.Position;
import com.rebels.quasar.model.Spaceship;
import com.rebels.quasar.repository.SatelliteRepository;
import com.rebels.quasar.service.LocationService;
import com.rebels.quasar.service.MessageService;
import com.rebels.quasar.service.TopSecretSplitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 *
 * @author FDip
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TopSecretSplitServiceImpl implements TopSecretSplitService {

    private final SatelliteRepository satelliteRepository;
    private final LocationService locationService;
    private final MessageService messageService;
    
    private final Map<String, SatelliteDataDto> satelliteDataMap = new ConcurrentHashMap<>();

    @Override
    public void saveSatelliteData(String satelliteName, TopSecretSplitRequestDto request) {
        satelliteRepository.findByName(satelliteName)
        .orElseThrow(() -> {
            log.warn("Intento de guardar datos para satélite desconocido: {}", satelliteName);
            return new IllegalArgumentException("Satélite desconocido: " + satelliteName);
        });
        
        satelliteDataMap.put(satelliteName.toLowerCase(), 
            new SatelliteDataDto(
                satelliteName,
                request.getDistance(),
                request.getMessage()
            ));
    }

    @Override
    public Optional<Spaceship> getSpaceshipData() {
        // Verificación inicial de datos
        if (satelliteDataMap.size() < 3) {
            String errorMsg = "Información insuficiente: Se requieren datos de 3 satélites (actuales: " + 
                satelliteDataMap.size() + ")";
            log.warn(errorMsg);
            throw new CommunicationException(errorMsg);
        }

        try {
            // Crear mapa de distancias por nombre de satélite
            Map<String, Float> distancesMap = satelliteDataMap.entrySet().stream()
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    e -> e.getValue().getDistance() 
                ));

            // Preparar lista de mensajes
            List<List<String>> messages = satelliteDataMap.values().stream()
                .map(SatelliteDataDto::getMessage) 
                .collect(Collectors.toList());

            Position position = locationService.calculatePosition(distancesMap);
            String message = messageService.decodeMessage(messages);

            return Optional.of(new Spaceship(position, message));
        } catch (CommunicationException e) {
            log.error("Error al procesar los datos: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado: {}", e.getMessage());
            throw new CommunicationException("Error inesperado al procesar los datos");
        }
    }
}