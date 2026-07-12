package com.rednorte.bff_gateway.client;

import com.rednorte.bff_gateway.dto.CitaDTO;
import com.rednorte.bff_gateway.dto.CitaRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "ms-citas", url = "${ms.citas.url}")
public interface CitaClient {

    @GetMapping("/api/v1/citas")
    List<CitaDTO> obtenerTodas();

    @PostMapping("/api/v1/citas")
    CitaDTO crear(@RequestBody CitaRequestDTO request);

    @GetMapping("/api/v1/citas/paciente/{pacienteId}")
    List<CitaDTO> obtenerPorPaciente(@PathVariable Long pacienteId);

    @GetMapping("/api/v1/citas/medico")
    List<CitaDTO> obtenerPorMedico(@RequestParam String nombreMedico);

    @GetMapping("/api/v1/citas/{id}")
    CitaDTO obtenerPorId(@PathVariable("id") Long id);

    @PatchMapping("/api/v1/citas/{id}/cancelar")
    CitaDTO cancelar(@PathVariable("id") Long id);
}