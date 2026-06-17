package com.rednorte.bff_gateway.client;

import com.rednorte.bff_gateway.dto.PacienteDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "ms-pacientes", url = "${ms.pacientes.url}")
public interface PacienteClient {

    @GetMapping("/api/pacientes")
    List<PacienteDTO> obtenerTodos();
}
