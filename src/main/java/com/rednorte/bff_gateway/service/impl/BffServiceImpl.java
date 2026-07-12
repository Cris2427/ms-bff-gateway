package com.rednorte.bff_gateway.service.impl;

import com.rednorte.bff_gateway.client.ListaEsperaClient;
import com.rednorte.bff_gateway.client.ReasignacionClient;
import com.rednorte.bff_gateway.dto.ApiResponseDTO;
import com.rednorte.bff_gateway.dto.ListaEsperaDTO;
import com.rednorte.bff_gateway.dto.PacienteResumenDTO;
import com.rednorte.bff_gateway.dto.ReasignacionDTO;
import com.rednorte.bff_gateway.dto.ReasignacionRequestDTO;
import com.rednorte.bff_gateway.service.BffService;
import com.rednorte.bff_gateway.client.AuthClient;
import com.rednorte.bff_gateway.dto.LoginRequestDTO;
import com.rednorte.bff_gateway.dto.LoginResponseDTO;
import com.rednorte.bff_gateway.dto.RegistroRequestDTO;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.rednorte.bff_gateway.dto.PacienteDTO;
import com.rednorte.bff_gateway.client.PacienteClient;
import com.rednorte.bff_gateway.dto.CitaDTO;
import com.rednorte.bff_gateway.client.CitaClient;

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
    private final PacienteClient pacienteClient;
    private final CitaClient citaClient;
    private final AuthClient authClient;
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

    /** {@inheritDoc} */
    @Override
    @CircuitBreaker(name = "pacientesCB", fallbackMethod = "fallbackPacientes")
    public ApiResponseDTO<List<PacienteDTO>> obtenerPacientes() {
        log.info("BFF: obteniendo todos los pacientes");
        return ApiResponseDTO.ok(pacienteClient.obtenerTodos(), "Lista de pacientes obtenida");
    }

    /** {@inheritDoc} */
    @Override
    @CircuitBreaker(name = "citasCB", fallbackMethod = "fallbackCitas")
    public ApiResponseDTO<List<CitaDTO>> obtenerCitas() {
        log.info("BFF: obteniendo todas las citas");
        return ApiResponseDTO.ok(citaClient.obtenerTodas(), "Lista de citas obtenida");
    }

    // Auth

    /** {@inheritDoc} */
    @Override
    public ApiResponseDTO<LoginResponseDTO> login(LoginRequestDTO request) {
        log.info("BFF: login de usuario: {}", request.getUsername());
        try {
            LoginResponseDTO data = authClient.login(request);
            return ApiResponseDTO.ok(data, "Login exitoso");
        } catch (FeignException.Unauthorized e) {
            log.warn("BFF: credenciales invalidas para: {}", request.getUsername());
            return ApiResponseDTO.error(401, "Usuario o contrasena incorrectos");
        }
    }

    /** {@inheritDoc} */
    @Override
    public ApiResponseDTO<LoginResponseDTO> registrar(RegistroRequestDTO request) {
        log.info("BFF: registro de usuario: {}", request.getUsername());
        try {
            LoginResponseDTO data = authClient.registrar(request);
            return ApiResponseDTO.ok(data, "Usuario registrado exitosamente");
        } catch (FeignException.BadRequest e) {
            log.warn("BFF: registro invalido para: {}", request.getUsername());
            return ApiResponseDTO.error(400, "El usuario ya existe o los datos son invalidos");
        }
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

    public ApiResponseDTO<List<PacienteDTO>> fallbackPacientes(Throwable t) {
        log.error("Circuit Breaker activado para pacientes: {}", t.getMessage());
        return ApiResponseDTO.error(503, "Servicio de pacientes no disponible temporalmente");
    }

    public ApiResponseDTO<List<CitaDTO>> fallbackCitas(Throwable t) {
        log.error("Circuit Breaker activado para citas: {}", t.getMessage());
        return ApiResponseDTO.error(503, "Servicio de citas no disponible temporalmente");
    }
}