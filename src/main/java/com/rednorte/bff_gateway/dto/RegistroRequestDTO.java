package com.rednorte.bff_gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistroRequestDTO {
    private String username;
    private String password;
    private String nombre;
    private String rol;
    private Long pacienteId;
}
