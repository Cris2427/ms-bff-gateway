package com.rednorte.bff_gateway.client;


import com.rednorte.bff_gateway.dto.CitaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "ms-citas", url = "${ms.citas.url}")
public interface CitaClient {
    @GetMapping("/api/v1/citas")
    List<CitaDTO> obtenerTodas();
}
