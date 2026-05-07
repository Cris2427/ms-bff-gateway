package com.rednorte.bff_gateway.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO envolvente (wrapper) genérico para todas las respuestas del BFF hacia el frontend.
 *
 * <p>Estandariza la estructura de respuesta HTTP del gateway, permitiendo al frontend
 * manejar tanto respuestas exitosas como errores de forma consistente.</p>
 *
 * @param <T> Tipo del payload de datos contenido en la respuesta
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(
    name = "ApiResponseDTO",
    description = "Wrapper estándar para todas las respuestas del BFF. Incluye metadatos de estado y el payload de datos."
)
public class ApiResponseDTO<T> {

    /**
     * Indica si la operación fue exitosa.
     *
     * @return {@code true} si la operación fue exitosa
     */
    @Schema(description = "Indica si la operación fue procesada exitosamente", example = "true")
    private boolean success;

    /**
     * Código HTTP de la respuesta para referencia del cliente.
     *
     * @return Código HTTP
     */
    @Schema(description = "Código HTTP de la respuesta", example = "200")
    private int status;

    /**
     * Mensaje descriptivo del resultado de la operación.
     *
     * @return Mensaje para el frontend
     */
    @Schema(description = "Mensaje descriptivo del resultado", example = "Operación completada exitosamente")
    private String message;

    /**
     * Payload de datos principal de la respuesta. Puede ser un objeto simple,
     * una lista o {@code null} en caso de error.
     *
     * @return Datos de la respuesta
     */
    @Schema(description = "Datos principales de la respuesta (null en caso de error)")
    private T data;

    /**
     * Marca de tiempo del momento en que se generó la respuesta en el BFF.
     *
     * @return Timestamp de la respuesta
     */
    @Schema(description = "Timestamp de generación de la respuesta", example = "2025-06-10T14:30:00")
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    /**
     * Crea una respuesta exitosa con datos.
     *
     * @param <T>     Tipo de los datos
     * @param data    Payload de la respuesta
     * @param message Mensaje descriptivo
     * @return Instancia de {@code ApiResponseDTO} con {@code success=true}
     */
    public static <T> ApiResponseDTO<T> ok(T data, String message) {
        return ApiResponseDTO.<T>builder()
                .success(true)
                .status(200)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Crea una respuesta de error sin datos.
     *
     * @param <T>     Tipo de los datos (usualmente Void o String)
     * @param status  Código HTTP de error
     * @param message Descripción del error
     * @return Instancia de {@code ApiResponseDTO} con {@code success=false}
     */
    public static <T> ApiResponseDTO<T> error(int status, String message) {
        return ApiResponseDTO.<T>builder()
                .success(false)
                .status(status)
                .message(message)
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
