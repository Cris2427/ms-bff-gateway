package com.rednorte.bff_gateway.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO adaptado del BFF que representa un resumen del paciente optimizado para el frontend.
 *
 * <p>A diferencia del DTO del microservicio de pacientes, este DTO agrega el estado
 * actual en lista de espera directamente, evitando múltiples llamadas desde el frontend.</p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(
    name = "PacienteResumenDTO",
    description = "Resumen de paciente adaptado para el frontend, incluye su posición en lista de espera"
)
public class PacienteResumenDTO {

    /**
     * Identificador único del paciente.
     *
     * @return ID del paciente
     */
    @Schema(description = "Identificador único del paciente", example = "55")
    private Long id;

    /**
     * Nombre completo del paciente para visualización en UI.
     *
     * @return Nombre completo
     */
    @Schema(description = "Nombre completo del paciente", example = "Juan Pérez González")
    private String nombreCompleto;

    /**
     * RUT del paciente (sin puntos, con guión).
     *
     * @return RUT formateado
     */
    @Schema(description = "RUT del paciente", example = "12345678-9")
    private String rut;

    /**
     * Correo de contacto del paciente.
     *
     * @return Email
     */
    @Schema(description = "Email del paciente", example = "juan.perez@mail.com")
    private String email;

    /**
     * Posición actual del paciente en la lista de espera de su especialidad.
     * Será {@code null} si el paciente no está en lista de espera.
     *
     * @return Número de posición en lista (1 = primero)
     */
    @Schema(description = "Posición en lista de espera (null si no está en lista)", example = "3")
    private Integer posicionListaEspera;

    /**
     * Especialidad por la que el paciente está en lista de espera.
     *
     * @return Nombre de la especialidad
     */
    @Schema(description = "Especialidad solicitada en lista de espera", example = "Cardiología")
    private String especialidadEspera;

    /**
     * Estado del paciente en el sistema de listas de espera.
     *
     * @return Estado (EN_ESPERA, ASIGNADO, INACTIVO)
     */
    @Schema(description = "Estado en lista de espera",
            example = "EN_ESPERA",
            allowableValues = {"EN_ESPERA", "ASIGNADO", "INACTIVO"})
    private String estadoListaEspera;
}
