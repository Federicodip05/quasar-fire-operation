package com.rebels.quasar.service.impl;

import com.rebels.quasar.exception.CommunicationException;
import com.rebels.quasar.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author FDip
 */
@Slf4j
@Service
public class MessageServiceImpl implements MessageService {

    @Override
    public String decodeMessage(List<List<String>> messages) throws CommunicationException {
        log.info("Iniciando decodificación de mensaje");
        validateInput(messages);
        
        int messageLength = messages.get(0).size();
        List<String> decodedWords = new ArrayList<>();
        
        for (int i = 0; i < messageLength; i++) {
            String word = resolveWord(messages, i);
            decodedWords.add(word);
        }
        
        log.info("Mensaje decodificado exitosamente");
        return String.join(" ", decodedWords);
    }

    private void validateInput(List<List<String>> messages) throws CommunicationException {
        // Validación básica
        if (messages == null || messages.isEmpty()) {
            String errorMsg = "Los mensajes no pueden ser nulos o estar vacíos";
            log.error(errorMsg);
            throw new CommunicationException(errorMsg);    
        }

        // Todos los mensajes deben tener la misma longitud
        int expectedLength = messages.get(0).size();
        for (List<String> message : messages) {
            if (message.size() != expectedLength) {
                String errorMsg = "Todos los mensajes deben tener la misma longitud";
                log.error(errorMsg);
                throw new CommunicationException(errorMsg);
            }
        }
    }

    private String resolveWord(List<List<String>> messages, int position) throws CommunicationException {
        String foundWord = null;
        
        for (List<String> message : messages) {
            String currentWord = message.get(position);
            
            if (currentWord != null && !currentWord.isEmpty()) {
                if (foundWord == null) {
                    foundWord = currentWord;
                } else if (!foundWord.equals(currentWord)) {
                    String errorMsg = String.format(
                        "Conflicto de palabras en la posición %d: '%s' vs '%s'", 
                        position, foundWord, currentWord);
                    log.error(errorMsg);
                    throw new CommunicationException(errorMsg);
                }
            }
        }
        
        if (foundWord == null) {
            String errorMsg = "No se puede decodificar el mensaje - falta la palabra de la posición " + position;
            log.error(errorMsg);
            throw new CommunicationException(errorMsg);    
        }
        
        return foundWord;
    }
}