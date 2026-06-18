package com.rednorte.bff_gateway.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO que representa un ítem de la lista de espera de pacientes, adaptado por el BFF
 * para consumo directo del frontend sin procesamiento adicional.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(
    name = "ListaEsperaDTO",
    description = "Ítem de lista de espera con datos del paciente consolidados para el frontend"
)
public class ListaEsperaDTO {

    /**
     * Identificador único del registro en lista de espera.
     * @return ID del registro
     */
    @Schema(description = "ID único del registro en lista de espera", example = "10")
    private Long id;

    /**
     * Posición del paciente en la lista de espera (ordinal, 1 = primero en línea).
     * @return Posición en la lista
     */
    @Schema(description = "Posición en la lista de espera (1 = primero en línea)", example = "2")
    private Integer posicion;

    /**
     * Resumen del paciente en espera, con sus datos básicos y estado.
     * @return Datos del paciente
     */
    @Schema(description = "Resumen del paciente en espera")
    private PacienteResumenDTO paciente;

    /**
     * Especialidad médica para la que el paciente espera atención.
     * @return Nombre de la especialidad
     */
    @Schema(description = "Especialidad médica solicitada", example = "Traumatología")
    private String especialidad;

    /**
     * Nivel de prioridad asignado al paciente según criterios médicos.
     * @return Nivel de prioridad (ALTA, MEDIA, BAJA)
     */
    @Schema(description = "Prioridad médica del paciente en la lista",
            example = "ALTA",
            allowableValues = {"ALTA", "MEDIA", "BAJA"})
    private String prioridad;
    /**
     * Fecha y hora en que el paciente fue inscrito en la lista de espera.
     * @return Fecha de inscripción
     */
    @Schema(description = "Fecha y hora de inscripción en lista de espera", example = "2025-05-20T10:00:00")
    private LocalDateTime fechaInscripcion;

    /**
     * Estado actual del registro en la lista de espera.
     */
    @Schema(description = "Estado del registro en lista de espera",
            example = "ACTIVO",
            allowableValues = {"ACTIVO", "ASIGNADO", "CANCELADO"})
    private String estado;

    @Schema(description = "Días estimados de espera calculados por el BFF", example = "15")
    private Integer diasEsperaEstimados;

    private Long pacienteId;

    private String tipo;

    private java.time.LocalDateTime creadoEn;
}
