package com.rednorte.bff_gateway.service;


import com.rednorte.bff_gateway.dto.*;

import java.util.List;

/**
 * Interfaz del servicio principal
 * define las operaciones deispoinbles para el frontend,
 * coordinando las llamadas a los ms internos con la proteccion de Circuit Breaker
 */
public interface BffService {
    /**
     * crea una nueva reasignacnion al ms-reasignacion
     * @param request datos de la solicitud
     * @return entrega una respuesta estandar con la reasignaacion creada
     */
    ApiResponseDTO<ReasignacionDTO> crearReasignacion(ReasignacionRequestDTO request);

    /**
     * obtiene una reasignacion por el id
     * @param id identifica la reasignacion
     * @return respuesta estandar ocn la reasignacion encontrada
     */

    ApiResponseDTO<ReasignacionDTO> obtenerReasignacion(Long id);

    /**
     * lista las reasignaciones que estan registradas
     * @return lista completa
     */

    ApiResponseDTO<List<ReasignacionDTO>> listarReasignaciones();

    /**
     * reasignaciones filtradas
     * @param estado estado que debe filtrar
     * @return muestra la lista ya filtrada
     */

    ApiResponseDTO<List<ReasignacionDTO>> listarPorEstado(String estado);

    /**
     * lista completa de pacientes en eespeera
     * @return respuesta con lista de espera
     */

    ApiResponseDTO<List<ListaEsperaDTO>> obtenerListaEspera();

    /**
     * pacientes en espera por especialidad
     * @param especialidad nombre de la especialidad
     * @return respuesta con la lista filtrada
     */

    ApiResponseDTO<List<ListaEsperaDTO>> obtenerListaEsperaPorEspecialidad(String especialidad);

    /**
     * resumen de un paciente que este en lista de espera segun su ID
     * @param pacienteId
     * @return respuesta con el resumen del paciente
     */

    ApiResponseDTO<PacienteResumenDTO> obtenerResumenPaciente(Long pacienteId);

    // lsita todos los pacientes registrados
    ApiResponseDTO<List<PacienteDTO>> obtenerPacientes();

    ApiResponseDTO<List<CitaDTO>>  obtenerCitas();

    ApiResponseDTO<LoginResponseDTO> login(LoginRequestDTO request);

    ApiResponseDTO<LoginResponseDTO> registrar(RegistroRequestDTO request);
}
