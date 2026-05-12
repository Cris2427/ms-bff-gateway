package com.rednorte.bff_gateway.client;


import com.rednorte.bff_gateway.dto.ReasignacionDTO;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "ms-reasignacion", url = "${ms.reasignacion.url}")
public interface ReasignacionClient {

    @PostMapping("/api/reasignaciones")
    ReasignacionDTO crearReasignacion(@RequestBody ReasignacionDTO request);

    @GetMapping("/api/reasignaciones/{id}")
    ReasignacionDTO obtenerPorId(@PathVariable("id") Long id);

    @GetMapping("/api/reasignaciones")
    List<ReasignacionDTO> listarTodas();

    @GetMapping("/api/reasignaciones/estado")
    List<ReasignacionDTO> listarPorEstado(@RequestParam("estado") String estado);

}
