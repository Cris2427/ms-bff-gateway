package com.rednorte.bff_gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistroPacienteDTO {
    private String rut;
    private String nombre;
    private String contacto;
    private String username;
    private String password;
}