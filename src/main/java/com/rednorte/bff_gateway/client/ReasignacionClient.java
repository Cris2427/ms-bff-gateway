package com.rednorte.bff_gateway.client;

import com.rednorte.bff_gateway.dto.ReasignacionDTO;
import com.rednorte.bff_gateway.dto.ReasignacionRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Cliente Feign para comunicarse con el microservicio ms-reasignacion.
 * Permite al BFF Gateway delegar operaciones de reasignacion
 * sin acoplarse directamente a la implementacion del servicio.
 */
@FeignClient(name = "ms-reasignacion", url = "${ms.reasignacion.url}")
public interface ReasignacionClient {

    /**
     * Crea una nueva reasignacion en el microservicio.
     * @param request datos de la solicitud de reasignacion
     * @return reasignacion creada
     */
    @PostMapping("/api/v1/reasignaciones")
    ReasignacionDTO crearReasignacion(@RequestBody ReasignacionRequestDTO request);

    /**
     * Obtiene una reasignacion por su ID.
     * @param id identificador de la reasignacion
     * @return reasignacion encontrada
     */
    @GetMapping("/api/v1/reasignaciones/{id}")
    ReasignacionDTO obtenerPorId(@PathVariable("id") Long id);

    /**
     * Lista todas las reasignaciones registradas.
     * @return lista completa de reasignaciones
     */
    @GetMapping("/api/v1/reasignaciones")
    List<ReasignacionDTO> listarTodas();

    /**
     * Lista reasignaciones filtradas por estado.
     * @param estado estado a filtrar
     * @return lista filtrada de reasignaciones
     */
    @GetMapping("/api/v1/reasignaciones/estado/{estado}")
    List<ReasignacionDTO> listarPorEstado(@RequestParam("estado") String estado);
}