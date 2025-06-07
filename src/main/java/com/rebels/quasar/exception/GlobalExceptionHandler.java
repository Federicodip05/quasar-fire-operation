package com.rebels.quasar.exception;

import com.rebels.quasar.dto.response.ErrorResponseDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;

/**
 *
 * @author FDip
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CommunicationException.class)
    public ResponseEntity<ErrorResponseDto> handleCommunication(CommunicationException ex) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalArgument(IllegalArgumentException ex) {
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationExceptions(
        MethodArgumentNotValidException ex) {
    
        String errorMessage = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(FieldError::getDefaultMessage)
            .findFirst()
            .orElse("Datos de entrada inválidos");
    
        return buildErrorResponse(
            new IllegalArgumentException(errorMessage),
            HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleConstraintViolation(
        ConstraintViolationException ex) {
    
        String errorMessage = ex.getConstraintViolations()
            .stream()
            .map(ConstraintViolation::getMessage)
            .findFirst()
            .orElse("Datos de entrada inválidos");
    
        return buildErrorResponse(
            new IllegalArgumentException(errorMessage),
            HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGeneralException(Exception ex) {
        return buildErrorResponse(
            new IllegalArgumentException("Error interno del servidor"),
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
    
    private ResponseEntity<ErrorResponseDto> buildErrorResponse(
            Exception ex, HttpStatus status) {
        return ResponseEntity.status(status)
            .body(ErrorResponseDto.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(ex.getMessage())
                .build());
    }
}