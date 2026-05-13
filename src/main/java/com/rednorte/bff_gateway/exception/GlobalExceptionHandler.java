package com.rednorte.bff_gateway.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

/**
 * Manejador global de excepciones
 * Intercepta las excepciones lanzadas en cualquier controlador y los convierte en respuestas HTTP
 */

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja errores de validacion de campos y retorna HTTP 400
     * @param ex excepcion de validacion
     * @return ResponseEntity con estdao 400
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationErrors(
            MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining(", "));
        log.error("Error de validacion en BFF: {}", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponseDTO.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .message(errors)
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    /**
     * Maneja cualquier excepcion no controlada y retorna HTTP 503
     * @param ex excepcion geneerica
     * @return ResponseEntity con estado 503
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(Exception ex) {
        log.error("Error inesperado en BFF: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(ErrorResponseDTO.builder()
                        .status(HttpStatus.SERVICE_UNAVAILABLE.value())
                        .message("Servicio temporalmente no disponible")
                        .timestamp(LocalDateTime.now())
                        .build());
    }
}
