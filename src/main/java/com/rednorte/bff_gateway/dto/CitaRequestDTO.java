package com.rednorte.bff_gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CitaRequestDTO {
    private Long pacienteId;
    private LocalDate fecha;
    private LocalTime hora;
    private String nombreMedico;
    private String especialidad;
    private String centroSalud;
    private String motivo;
}
