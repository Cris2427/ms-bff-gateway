package com.rednorte.bff_gateway.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * DTO del BFF que encapsula la información de una reasignación de cita médica,
 * adaptada para la visualización en el frontend.
 *Agrega datos de la cita, del paciente cancelador y del nuevo paciente asignado
 * en una sola respuesta, evitando múltiples roundtrips desde el frontend.
 * Este DTO es generado por el BFF orquestando llamadas al microservicio
 * {@code ms-reasignacion}, protegido por un Circuit Breaker Resilience4j.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(
    name = "ReasignacionDTO",
    description = "Vista consolidada de una reasignación de cita para el frontend, con datos del paciente y la cita incluidos"
)
public class ReasignacionDTO {

    /**
     * ID del registro de reasignación en el microservicio.
     * @return ID de la reasignación
     */
    @Schema(description = "ID único del registro de reasignación", example = "200")
    private Long reasignacionId;

    /**
     * Estado legible de la reasignación para mostrar en la UI.
     * @return Estado de la reasignación
     */
    @Schema(description = "Estado de la reasignación", example = "COMPLETADA",
            allowableValues = {"PENDIENTE", "COMPLETADA", "FALLIDA", "CANCELADA"})
    private String estado;

    // Datos de la Cita

    /**
     * ID de la cita médica que fue reasignada.
     * @return ID de la cita
     */
    @Schema(description = "ID de la cita médica reasignada", example = "101")
    private Long citaId;

    /**
     * Fecha de la cita reasignada.
     * @return Fecha de la cita
     */
    @Schema(description = "Fecha de la cita reasignada", example = "2025-07-15")
    private LocalDate fechaCita;

    /**
     * Hora de inicio de la cita reasignada.
     * @return Hora de la cita
     */
    @Schema(description = "Hora de la cita reasignada", example = "09:30")
    private LocalTime horaCita;

    /**
     * Nombre del médico que atenderá la cita.
     * @return Nombre del médico
     */
    @Schema(description = "Médico que atenderá la cita", example = "Dra. María López")
    private String nombreMedico;

    /**
     * Especialidad médica de la cita.
     * @return Especialidad
     */
    @Schema(description = "Especialidad de la cita", example = "Cardiología")
    private String especialidad;

    // Datos del nuevo paciente asignado

    /**
     * ID del nuevo paciente que recibe la cita reasignada.
     * @return ID del nuevo paciente
     */
    @Schema(description = "ID del nuevo paciente asignado a la cita", example = "77")
    private Long nuevoPacienteId;

    /**
     * Nombre del nuevo paciente para mostrar en la confirmación.
     * @return Nombre del nuevo paciente
     */
    @Schema(description = "Nombre del nuevo paciente asignado", example = "Pedro Soto Muñoz")
    private String nuevoPacienteNombre;

    // Metadatos

    /**
     * Fecha y hora en que se registró la reasignación.
     * @return Fecha de la reasignación
     */
    @Schema(description = "Fecha y hora en que se registró la reasignación", example = "2025-06-10T14:30:00")
    private LocalDateTime fechaReasignacion;

    /**
     * Mensaje resumen del resultado de la reasignación para mostrar en la UI.
     * @return Mensaje descriptivo
     */
    @Schema(description = "Mensaje resumen del resultado para la UI",
            example = "Cita reasignada exitosamente a Pedro Soto Muñoz")
    private String mensajeResultado;

    //Request para crear reasignaciones desde el BFF

    /**
     * ID de la cita a reasignar (campo de entrada para POST).
     * @return citaId de entrada
     */
    @NotNull(message = "El ID de la cita es obligatorio para iniciar una reasignación")
    @Schema(description = "ID de la cita a reasignar (usar en el body del POST)", example = "101",
            required = true, writeOnly = true)
    private Long citaIdRequest;
}
