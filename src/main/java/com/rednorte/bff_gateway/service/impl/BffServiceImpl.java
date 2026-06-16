package com.rednorte.bff_gateway.service.impl;

import com.rednorte.bff_gateway.client.ListaEsperaClient;
import com.rednorte.bff_gateway.client.ReasignacionClient;
import com.rednorte.bff_gateway.dto.ApiResponseDTO;
import com.rednorte.bff_gateway.dto.ListaEsperaDTO;
import com.rednorte.bff_gateway.dto.PacienteResumenDTO;
import com.rednorte.bff_gateway.dto.ReasignacionDTO;
import com.rednorte.bff_gateway.dto.ReasignacionRequestDTO;
import com.rednorte.bff_gateway.service.BffService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementacion del servicio BFF Gateway.
 * Coordina las llamadas a los microservicios ms-reasignacion y ms-lista-espera,
 * aplicando Circuit Breaker con Resilience4j para mayor resiliencia.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BffServiceImpl implements BffService {

    private final ReasignacionClient reasignacionClient;
    private final ListaEsperaClient listaEsperaClient;

    // Reasignacion

    /** {@inheritDoc} */
    @Override
    @CircuitBreaker(name = "reasignacionCB", fallbackMethod = "fallbackReasignacion")
    public ApiResponseDTO<ReasignacionDTO> crearReasignacion(ReasignacionRequestDTO request) {
        log.info("BFF: creando reasignacion para cita ID: {}", request.getCitaId());
        ReasignacionDTO dto = reasignacionClient.crearReasignacion(request);
        return ApiResponseDTO.ok(dto, "Reasignacion creada exitosamente");
    }

    /** {@inheritDoc} */
    @Override
    @CircuitBreaker(name = "reasignacionCB", fallbackMethod = "fallbackReasignacion")
    public ApiResponseDTO<ReasignacionDTO> obtenerReasignacion(Long id) {
        log.info("BFF: obteniendo reasignacion ID: {}", id);
        ReasignacionDTO dto = reasignacionClient.obtenerPorId(id);
        return ApiResponseDTO.ok(dto, "Reasignacion encontrada");
    }

    /** {@inheritDoc} */
    @Override
    @CircuitBreaker(name = "reasignacionCB", fallbackMethod = "fallbackListaReasignacion")
    public ApiResponseDTO<List<ReasignacionDTO>> listarReasignaciones() {
        log.info("BFF: listando todas las reasignaciones");
        return ApiResponseDTO.ok(reasignacionClient.listarTodas(), "Lista de reasignaciones obtenida");
    }

    /** {@inheritDoc} */
    @Override
    @CircuitBreaker(name = "reasignacionCB", fallbackMethod = "fallbackListaReasignacion")
    public ApiResponseDTO<List<ReasignacionDTO>> listarPorEstado(String estado) {
        log.info("BFF: listando reasignaciones con estado: {}", estado);
        return ApiResponseDTO.ok(reasignacionClient.listarPorEstado(estado), "Lista filtrada por estado");
    }

    // Lista Espera

    /** {@inheritDoc} */
    @Override
    @CircuitBreaker(name = "listaEsperaCB", fallbackMethod = "fallbackListaEspera")
    public ApiResponseDTO<List<ListaEsperaDTO>> obtenerListaEspera() {
        log.info("BFF: obteniendo lista de espera completa");
        return ApiResponseDTO.ok(listaEsperaClient.obtenerListaCompleta(), "Lista de espera obtenida");
    }

    /** {@inheritDoc} */
    @Override
    @CircuitBreaker(name = "listaEsperaCB", fallbackMethod = "fallbackListaEspera")
    public ApiResponseDTO<List<ListaEsperaDTO>> obtenerListaEsperaPorEspecialidad(String especialidad) {
        log.info("BFF: obteniendo lista de espera para especialidad: {}", especialidad);
        return ApiResponseDTO.ok(
                listaEsperaClient.obtenerPorEspecialidad(especialidad),
                "Lista de espera filtrada por especialidad");
    }

    /** {@inheritDoc} */
    @Override
    @CircuitBreaker(name = "listaEsperaCB", fallbackMethod = "fallbackResumenPaciente")
    public ApiResponseDTO<PacienteResumenDTO> obtenerResumenPaciente(Long pacienteId) {
        log.info("BFF: obteniendo resumen paciente ID: {}", pacienteId);
        return ApiResponseDTO.ok(
                listaEsperaClient.obtenerPorPaciente(pacienteId),
                "Resumen de paciente obtenido");
    }

    //Fallbacks

    /**
     * Fallback para operaciones de reasignacion individual
     * @param t excepcion capturada
     * @return respuesta de error estandarizada
     */
    public ApiResponseDTO<ReasignacionDTO> fallbackReasignacion(Throwable t) {
        log.error("Circuit Breaker activado para reasignacion: {}", t.getMessage());
        return ApiResponseDTO.error(503, "Servicio de reasignacion no disponible temporalmente");
    }

    /**
     * Fallback para operaciones de lista de reasignaciones.
     * @param t excepcion capturada
     * @return respuesta de error estandarizada
     */
    public ApiResponseDTO<List<ReasignacionDTO>> fallbackListaReasignacion(Throwable t) {
        log.error("Circuit Breaker activado para lista reasignacion: {}", t.getMessage());
        return ApiResponseDTO.error(503, "Servicio de reasignacion no disponible temporalmente");
    }

    /**
     * Fallback para operaciones de lista de espera.
     * @param t excepcion capturada
     * @return respuesta de error estandarizada
     */
    public ApiResponseDTO<List<ListaEsperaDTO>> fallbackListaEspera(Throwable t) {
        log.error("Circuit Breaker activado para lista espera: {}", t.getMessage());
        return ApiResponseDTO.error(503, "Servicio de lista de espera no disponible temporalmente");
    }

    /**
     * Fallback para operaciones de resumen de paciente.
     * @param t excepcion capturada
     * @return respuesta de error estandarizada
     */
    public ApiResponseDTO<PacienteResumenDTO> fallbackResumenPaciente(Throwable t) {
        log.error("Circuit Breaker activado para resumen paciente: {}", t.getMessage());
        return ApiResponseDTO.error(503, "Servicio de lista de espera no disponible temporalmente");
    }
}