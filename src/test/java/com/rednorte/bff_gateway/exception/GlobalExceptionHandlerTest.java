package com.rednorte.bff_gateway.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para {@link GlobalExceptionHandler} del BFF Gateway.
 * Verifica que cada handler retorne el status HTTP y mensaje correctos.
 */
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    @DisplayName("handleValidationErrors debe retornar 400 con detalle de campos invalidos")
    void handleValidationErrors_debeRetornar400() {
        BeanPropertyBindingResult bindingResult =
                new BeanPropertyBindingResult(new Object(), "reasignacionRequestDTO");
        bindingResult.addError(new FieldError(
                "reasignacionRequestDTO", "citaId", "El ID de la cita es obligatorio"));

        MethodArgumentNotValidException ex =
                new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<ErrorResponseDTO> response = handler.handleValidationErrors(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().getStatus());
        assertTrue(response.getBody().getMessage().contains("citaId"));
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    @DisplayName("handleGenericException debe retornar 503 con mensaje de servicio no disponible")
    void handleGenericException_debeRetornar503() {
        Exception ex = new Exception("Error de conexion con microservicio");

        ResponseEntity<ErrorResponseDTO> response = handler.handleGenericException(ex);

        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(503, response.getBody().getStatus());
        assertEquals("Servicio temporalmente no disponible", response.getBody().getMessage());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    @DisplayName("ErrorResponseDTO builder debe construir el objeto correctamente")
    void errorResponseDTO_builder_debeFuncionar() {
        ErrorResponseDTO dto = ErrorResponseDTO.builder()
                .status(503)
                .message("Servicio no disponible")
                .build();

        assertEquals(503, dto.getStatus());
        assertEquals("Servicio no disponible", dto.getMessage());
    }
}