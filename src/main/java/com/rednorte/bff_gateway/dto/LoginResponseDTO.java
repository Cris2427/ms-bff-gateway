package com.rednorte.bff_gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponseDTO {
    private Long usuarioId;
    private String username;
    private String nombre;
    private String rol;
    private Long pacienteId;
}
