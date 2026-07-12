package com.rednorte.bff_gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReasignacionResultadoDTO {
    private String estado;              // COMPLETADA | FALLIDA
    private Long citaCanceladaId;
    private Long nuevaCitaId;
    private Long pacienteReasignadoId;
    private String prioridad;
    private String mensaje;
}