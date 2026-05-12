package com.rednorte.bff_gateway.client;


import com.rednorte.bff_gateway.dto.ReasignacionDTO;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * cliente Feign que se comunica con el ms-reasignacion
 * Permite al BFF delegar operaciones de reasignacion sin acoplarse directamtne a la implementacion del ms
 */
@FeignClient(name = "ms-reasignacion", url = "${ms.reasignacion.url}")
public interface ReasignacionClient {
    /**
     * crea una nueva reasignacion en el ms
     * @param request datos de la solicitud de reasignacion
     * @return reasignacion creada
     */

    @PostMapping("/api/reasignaciones")
    ReasignacionDTO crearReasignacion(@RequestBody ReasignacionDTO request);

    /**
     * obtiene una reasignacion por el id
     * @param id
     * @return devuelve la reasignacion encontrada
     */

    @GetMapping("/api/reasignaciones/{id}")
    ReasignacionDTO obtenerPorId(@PathVariable("id") Long id);

    /**
     * lista todas las reasignaciones que esten registradas
     * @return lsita completa de la reasignacion
     */

    @GetMapping("/api/reasignaciones")
    List<ReasignacionDTO> listarTodas();

    /**
     * lsita reasignaciones filtradas por estado
     * @param estado el estado que filtra
     * @return la lista ya filtrada
     */

    @GetMapping("/api/reasignaciones/estado")
    List<ReasignacionDTO> listarPorEstado(@RequestParam("estado") String estado);

}
