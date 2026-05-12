package com.rednorte.bff_gateway.client;


import com.rednorte.bff_gateway.dto.ListaEsperaDTO;
import com.rednorte.bff_gateway.dto.PacienteResumenDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * cliente Feign es para comunicarse con el ms-lista-espera
 * permite al BFF Gateway consultar pacientes en espera y sus posiciones en la lista por especialidad
 */
@FeignClient(name = "ms-lista-espera", url = ("${ms.lista-espera.url}"))
public interface ListaEsperaClient {
    /**
     * obtiene todos los pacientes en lista de espera
     * @return la lista completa de los pacientes
     */

    @GetMapping("/api/lista-espera")
    List<ListaEsperaDTO> obtenerListaCompleta();

    /**
     * obtiene todos los pacientes en espera filtrados por especialidad
     * @param especialidad el nombre de la especialidad
     * @return devuelve lista de pacientes en espera
     */

    @GetMapping("/api/lista-espera/especialidad")
    List<ListaEsperaDTO> obtenerPorEspecialidad(@RequestParam("especialidad") String especialidad);

    /**
     * obtiene el resumen de un paciente en espera
     * @param pacienteId
     * @return entrega el resumen del paciente solicitado con su posicion en la lista de espera
     */

    @GetMapping("/api/lista-espera/paciente/{pacienteId}")
    PacienteResumenDTO obtenerPorPaciente(@PathVariable("pacienteId") Long pacienteId);
}
