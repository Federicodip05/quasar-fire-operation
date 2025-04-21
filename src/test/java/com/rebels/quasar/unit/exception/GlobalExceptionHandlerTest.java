package com.rebels.quasar.unit.exception;

import com.rebels.quasar.exception.GlobalExceptionHandler;
import com.rebels.quasar.exception.CommunicationException;
import com.rebels.quasar.dto.response.ErrorResponseDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author FDip
 */
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("handleCommunication → debería retornar NOT_FOUND con mensaje de error")
    void shouldHandleCommunicationException() {
        // Arrange
        String errorMessage = "Error de comunicación con satélites";
        CommunicationException ex = new CommunicationException(errorMessage);

        // Act
        ResponseEntity<ErrorResponseDto> response = 
            globalExceptionHandler.handleCommunication(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        
        ErrorResponseDto body = response.getBody();
        assertNotNull(body);
        assertEquals(errorMessage, body.getMessage());
        assertEquals(HttpStatus.NOT_FOUND.value(), body.getStatus());
        assertEquals(HttpStatus.NOT_FOUND.getReasonPhrase(), body.getError());
        assertTrue(DateTimeUtils.isWithinLastSecond(body.getTimestamp()));
    }

    @Test
    @DisplayName("handleIllegalArgument → debería retornar BAD_REQUEST con mensaje de error")
    void shouldHandleIllegalArgumentException() {
        // Arrange
        String errorMessage = "Argumento inválido";
        IllegalArgumentException ex = new IllegalArgumentException(errorMessage);

        // Act
        ResponseEntity<ErrorResponseDto> response = 
            globalExceptionHandler.handleIllegalArgument(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        
        ErrorResponseDto body = response.getBody();
        assertNotNull(body);
        assertEquals(errorMessage, body.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST.value(), body.getStatus());
        assertEquals(HttpStatus.BAD_REQUEST.getReasonPhrase(), body.getError());
        assertTrue(DateTimeUtils.isWithinLastSecond(body.getTimestamp()));
    }

    @Test
    @DisplayName("handleValidationExceptions (MethodArgumentNotValid) → debería retornar BAD_REQUEST con mensaje de validación")
    void shouldHandleMethodArgumentNotValidException() {
        // Arrange
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("object", "field", "Mensaje de validación específico");
        
        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        // Act
        ResponseEntity<ErrorResponseDto> response = 
            globalExceptionHandler.handleValidationExceptions(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        
        ErrorResponseDto body = response.getBody();
        assertNotNull(body);
        assertEquals("Mensaje de validación específico", body.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST.value(), body.getStatus());
        assertEquals(HttpStatus.BAD_REQUEST.getReasonPhrase(), body.getError());
        assertTrue(DateTimeUtils.isWithinLastSecond(body.getTimestamp()));
    }

    @Test
    @DisplayName("handleValidationExceptions (MethodArgumentNotValid sin field errors) → debería retornar mensaje genérico")
    void shouldHandleMethodArgumentNotValidWithoutFieldErrors() {
        // Arrange
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        
        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<ErrorResponseDto> response = 
            globalExceptionHandler.handleValidationExceptions(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        
        ErrorResponseDto body = response.getBody();
        assertNotNull(body);
        assertEquals("Datos de entrada inválidos", body.getMessage());
    }

    @Test
    @DisplayName("handleConstraintViolation → debería retornar BAD_REQUEST con mensaje de validación")
    void shouldHandleConstraintViolationException() {
        // Arrange
        ConstraintViolationException ex = mock(ConstraintViolationException.class);
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        
        when(ex.getConstraintViolations()).thenReturn(Set.of(violation));
        when(violation.getMessage()).thenReturn("Mensaje de constraint violado");

        // Act
        ResponseEntity<ErrorResponseDto> response = 
            globalExceptionHandler.handleConstraintViolation(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        
        ErrorResponseDto body = response.getBody();
        assertNotNull(body);
        assertEquals("Mensaje de constraint violado", body.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST.value(), body.getStatus());
        assertEquals(HttpStatus.BAD_REQUEST.getReasonPhrase(), body.getError());
        assertTrue(DateTimeUtils.isWithinLastSecond(body.getTimestamp()));
    }

    @Test
    @DisplayName("handleConstraintViolation sin violations → debería retornar mensaje genérico")
    void shouldHandleConstraintViolationWithoutViolations() {
        // Arrange
        ConstraintViolationException ex = mock(ConstraintViolationException.class);
        when(ex.getConstraintViolations()).thenReturn(Collections.emptySet());

        // Act
        ResponseEntity<ErrorResponseDto> response = 
            globalExceptionHandler.handleConstraintViolation(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        
        ErrorResponseDto body = response.getBody();
        assertNotNull(body);
        assertEquals("Datos de entrada inválidos", body.getMessage());
    }

    @Test
    @DisplayName("handleGeneralException → debería retornar INTERNAL_SERVER_ERROR con mensaje genérico")
    void shouldHandleGeneralException() {
        // Arrange
        Exception ex = new Exception("Error crítico");

        // Act
        ResponseEntity<ErrorResponseDto> response = 
            globalExceptionHandler.handleGeneralException(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        
        ErrorResponseDto body = response.getBody();
        assertNotNull(body);
        assertEquals("Error interno del servidor", body.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), body.getStatus());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), body.getError());
        assertTrue(DateTimeUtils.isWithinLastSecond(body.getTimestamp()));
    }

    // Clase helper para verificar fechas
    private static class DateTimeUtils {
        static boolean isWithinLastSecond(LocalDateTime dateTime) {
            return ChronoUnit.SECONDS.between(dateTime, LocalDateTime.now()) <= 1;
        }
    }
}