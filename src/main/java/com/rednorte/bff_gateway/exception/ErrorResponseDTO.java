package com.rednorte.bff_gateway.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO que representa la estructura estandar de una respuesta de error
 * devuelta por el manejador global de excepciones del BFF Gateway
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ErrorResponseDTO", description = "Estructura estandar de respuesta de error del BFF")
public class ErrorResponseDTO {

    /**
     * Codigo HTTP del error ocurrido
     */
    @Schema(description = "Codigo HTTP del error", example = "503")
    private int status;

    /**
     * Mensaje descriptivo del error para el cliente
     */
    @Schema(description = "Mensaje descriptivo del error", example = "Servicio no disponible")
    private String message;

    /**
     * Marca de tiempo del momento en que ocurrio el error
     */
    @Schema(description = "Timestamp del error", example = "2025-06-10T14:30:00")
    private LocalDateTime timestamp;
}