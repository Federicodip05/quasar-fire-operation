package com.rebels.quasar.service;

import com.rebels.quasar.dto.request.SatelliteDataDto;
import com.rebels.quasar.dto.request.TopSecretRequestDto;
import com.rebels.quasar.exception.CommunicationException;
import com.rebels.quasar.model.Position;
import com.rebels.quasar.model.Spaceship;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author FDip
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TopSecretService {

    private final LocationService locationService;
    private final MessageService messageService;

    public Spaceship process(TopSecretRequestDto request) throws CommunicationException {
        // Validación básica
        if (request.satellites().size() < 3) {
            log.warn("Validación fallida: Se requieren 3 satélites, recibidos {}", request.satellites().size());
            throw new CommunicationException("Se requieren exactamente 3 satélites");
        }

        // Convertir a mapa de distancias por nombre de satélite
        Map<String, Float> distancesMap = request.satellites().stream()
            .collect(Collectors.toMap(
                dto -> dto.name().toLowerCase(),
                SatelliteDataDto::distance          
            ));

        // Obtener todos los mensajes (manteniendo el orden original)
        List<List<String>> messages = request.satellites().stream()
            .map(SatelliteDataDto::message)
            .collect(Collectors.toList());

        // Calcular posición y mensaje
        Position position = locationService.calculatePosition(distancesMap);
        String message = messageService.decodeMessage(messages);

        return new Spaceship(position, message);
    }
}