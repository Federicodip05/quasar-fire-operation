package com.rebels.quasar.service;

import com.rebels.quasar.exception.CommunicationException;
import com.rebels.quasar.dto.request.SatelliteDataDto;
import com.rebels.quasar.dto.request.TopSecretSplitRequestDto;
import com.rebels.quasar.model.Position;
import com.rebels.quasar.model.Spaceship;
import com.rebels.quasar.repository.SatelliteDataRepository;
import com.rebels.quasar.repository.SatelliteStaticRepository;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 *
 * @author FDip
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TopSecretSplitService {
    
    private final SatelliteDataRepository satelliteDataRepository;
    private final SatelliteStaticRepository satelliteStaticRepository;
    private final LocationService locationService;
    private final MessageService messageService;

    public void saveSatelliteData(String satelliteName, @Valid TopSecretSplitRequestDto request) {
        Objects.requireNonNull(satelliteName, "El nombre del satélite no puede ser nulo");
        Objects.requireNonNull(request, "Los datos del satélite no pueden ser nulos");

        String normalizedName = satelliteName.toLowerCase();
        
        satelliteStaticRepository.findByName(normalizedName)
            .orElseThrow(() -> {
                log.warn("Intento de guardar datos para satélite desconocido: {}", normalizedName);
                return new IllegalArgumentException("Satélite desconocido: " + satelliteName);
            });
        
        SatelliteDataDto data = new SatelliteDataDto(
            normalizedName,
            request.getDistance(),
            request.getMessage()
        );
        
        satelliteDataRepository.save(normalizedName, data);
        log.info("Datos actualizados para satélite: {}", normalizedName);
    }

    public Optional<Spaceship> getSpaceshipData() {
        Map<String, SatelliteDataDto> allData = satelliteDataRepository.findAll();
        
        if (allData.size() < 3) {
            String errorMsg = String.format(
                "Información insuficiente: Se requieren datos de 3 satélites, actuales: %d (%s)",
                allData.size(),
                String.join(", ", allData.keySet())
            );
            log.warn(errorMsg);
            throw new CommunicationException(errorMsg);
        }

        try {
            Map<String, Float> distances = allData.entrySet().stream()
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    e -> e.getValue().getDistance()
                ));

            List<List<String>> messages = allData.values().stream()
                .map(SatelliteDataDto::getMessage)
                .collect(Collectors.toList());

            Position position = locationService.calculatePosition(distances);
            String message = messageService.decodeMessage(messages);

            log.debug("Datos calculados - Posición: {}, Mensaje: {}", position, message);
            return Optional.of(new Spaceship(position, message));
            
        } catch (CommunicationException e) {
            log.error("Error al procesar datos satelitales: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado procesando datos: ", e);
            throw new CommunicationException("Error interno al calcular posición/mensaje");
        }
    }
}