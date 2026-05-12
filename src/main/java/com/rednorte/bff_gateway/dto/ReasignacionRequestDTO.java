package com.rednorte.bff_gateway.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ReasignacionRequestDTO", description = "Datos requeridos para iniciar una reasignacion")
public class ReasignacionRequestDTO {
    @NotNull(message = "El ID de la cita es obligatorio")
    @Schema(description = "ID de la cita cancelada a reasignar", example = "101",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private Long citaId;

    @NotNull(message = "El ID del paciente que cancelo es obligatorio")
    @Schema(description = "ID del paciente que cancelo", example = "55",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private Long pacienteCanceladorId;

    @Size(max = 500, message = "El motivo no puede superar los 500 caracteres")
    @Schema(description = "Motivo de la cancelacion", example = "Imprevisto de trabajo")
    private String motivoCancelacion;

    @Schema(description = "Notificar automaticamente al nuevo paciente", example = "true")
    @Builder.Default
    private Boolean notificarNuevoPaciente = true;
}
