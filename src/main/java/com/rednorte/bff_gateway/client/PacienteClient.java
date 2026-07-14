package com.rednorte.bff_gateway.client;

import com.rednorte.bff_gateway.dto.PacienteDTO;
import com.rednorte.bff_gateway.dto.PacienteRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "ms-pacientes", url = "${ms.pacientes.url}")
public interface PacienteClient {

    @GetMapping("/api/pacientes")
    List<PacienteDTO> obtenerTodos();

    @PostMapping("/api/pacientes")
    PacienteDTO crear(@RequestBody PacienteRequestDTO request);
}