package com.rebels.quasar.unit.service.impl;

import com.rebels.quasar.exception.CommunicationException;
import com.rebels.quasar.service.impl.MessageServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.List;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author FDip
 */
class MessageServiceImplTest {

    private MessageServiceImpl messageService;

    @BeforeEach
    void setUp() {
        messageService = new MessageServiceImpl();
    }

    @Test
    @DisplayName("decodeMessage con mensajes válidos → debería decodificar correctamente")
    void shouldDecodeValidMessages() throws CommunicationException {
        List<List<String>> messages = List.of(
            List.of("este", "", "", "test", ""),
            List.of("", "es", "", "", "secreto"),
            List.of("este", "", "un", "", "")
        );

        String result = messageService.decodeMessage(messages);
        assertEquals("este es un test secreto", result);
    }

    @ParameterizedTest
    @MethodSource("invalidInputProvider")
    @DisplayName("decodeMessage con entradas inválidas → debería lanzar CommunicationException")
    void shouldThrowExceptionWithInvalidInput(List<List<String>> invalidInput, String expectedMessage) {
        CommunicationException exception = assertThrows(CommunicationException.class,
            () -> messageService.decodeMessage(invalidInput));

        assertEquals(expectedMessage, exception.getMessage());
    }

    private static Stream<Object[]> invalidInputProvider() {
        return Stream.of(
            new Object[]{
                List.of(
                    List.of("", "palabra"),
                    List.of("", "")
                ),
                "No se puede decodificar el mensaje - falta la palabra de la posición 0"
            },
            new Object[]{
                List.of(
                    List.of("uno", "", ""),
                    List.of("", "dos", ""),
                    List.of("", "", "")
                ),
                "No se puede decodificar el mensaje - falta la palabra de la posición 2"
            }
        );
    }

    @Test
    @DisplayName("decodeMessage con mensajes de diferente longitud → debería lanzar excepción")
    void shouldRejectDifferentLengthMessages() {
        List<List<String>> messages = List.of(
            List.of("este", "", ""),
            List.of("", "mensaje", ""),
            List.of("", "", "es", "de", "prueba")
        );

        CommunicationException exception = assertThrows(CommunicationException.class,
            () -> messageService.decodeMessage(messages));

        assertEquals("Todos los mensajes deben tener la misma longitud", exception.getMessage());
    }

    @Test
    @DisplayName("decodeMessage con palabras contradictorias → debería lanzar excepción")
    void shouldRejectConflictingWords() {
        List<List<String>> messages = List.of(
            List.of("este", "", "perro"),
            List.of("", "es", ""),
            List.of("", "", "gato")
        );

        CommunicationException exception = assertThrows(CommunicationException.class,
            () -> messageService.decodeMessage(messages));

        assertEquals("Conflicto de palabras en la posición 2: 'perro' vs 'gato'", exception.getMessage());
    }
}