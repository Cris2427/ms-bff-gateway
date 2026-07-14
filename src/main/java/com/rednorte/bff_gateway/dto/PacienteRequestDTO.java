package com.rednorte.bff_gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PacienteRequestDTO {
    private String rut;
    private String nombre;
    private String contacto;
    private String historial;
}