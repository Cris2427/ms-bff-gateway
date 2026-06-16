package com.rednorte.bff_gateway.controller;

import com.rednorte.bff_gateway.dto.ApiResponseDTO;
import com.rednorte.bff_gateway.dto.ListaEsperaDTO;
import com.rednorte.bff_gateway.dto.PacienteResumenDTO;
import com.rednorte.bff_gateway.dto.ReasignacionDTO;
import com.rednorte.bff_gateway.dto.ReasignacionRequestDTO;
import com.rednorte.bff_gateway.service.BffService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controlador principal del BFF Gateway.
 * Expone los endpoints unificados para el frontend de RedNorte,
 * delegando la logica al servicio BFF con proteccion Circuit Breaker.
 */
@RestController
@RequestMapping("/api/bff")
@RequiredArgsConstructor
@Tag(name = "BFF Gateway", description = "Endpoints unificados para el frontend de RedNorte")
public class BffController {

    private final BffService bffService;
    /**
     * Crea una nueva reasignacion delegando al ms-reasignacion.
     * @param request datos de la solicitud
     * @return respuesta estandarizada con la reasignacion creada
     */
    @Operation(summary = "Crear reasignacion", description = "Inicia una reasignacion automatica de cita medica")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Reasignacion creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada invalidos"),
            @ApiResponse(responseCode = "503", description = "Servicio no disponible - Circuit Breaker activo")
    })
    @PostMapping("/reasignaciones")
    public ResponseEntity<ApiResponseDTO<ReasignacionDTO>> crearReasignacion(
            @Valid @RequestBody ReasignacionRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bffService.crearReasignacion(request));
    }

    /**
     * Obtiene una reasignacion por su ID.
     * @param id identificador de la reasignacion
     * @return respuesta estandarizada con la reasignacion encontrada
     */
    @Operation(summary = "Obtener reasignacion por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reasignacion encontrada"),
            @ApiResponse(responseCode = "503", description = "Servicio no disponible - Circuit Breaker activo")
    })
    @GetMapping("/reasignaciones/{id}")
    public ResponseEntity<ApiResponseDTO<ReasignacionDTO>> obtenerReasignacion(
            @PathVariable Long id) {
        return ResponseEntity.ok(bffService.obtenerReasignacion(id));
    }

    /**
     * Lista todas las reasignaciones registradas.
     * @return respuesta estandarizada con la lista completa
     */
    @Operation(summary = "Listar todas las reasignaciones")
    @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    @GetMapping("/reasignaciones")
    public ResponseEntity<ApiResponseDTO<List<ReasignacionDTO>>> listarReasignaciones() {
        return ResponseEntity.ok(bffService.listarReasignaciones());
    }

    /**
     * Lista reasignaciones filtradas por estado.
     * @param estado estado a filtrar
     * @return respuesta estandarizada con la lista filtrada
     */
    @Operation(summary = "Listar reasignaciones por estado")
    @ApiResponse(responseCode = "200", description = "Lista filtrada obtenida exitosamente")
    @GetMapping("/reasignaciones/estado")
    public ResponseEntity<ApiResponseDTO<List<ReasignacionDTO>>> listarPorEstado(
            @RequestParam String estado) {
        return ResponseEntity.ok(bffService.listarPorEstado(estado));
    }

    /**
     * Obtiene la lista completa de pacientes en espera.
     * @return respuesta estandarizada con la lista de espera
     */
    @Operation(summary = "Obtener lista de espera completa")
    @ApiResponse(responseCode = "200", description = "Lista de espera obtenida exitosamente")
    @GetMapping("/solicitudes")
    public ResponseEntity<ApiResponseDTO<List<ListaEsperaDTO>>> obtenerListaEspera() {
        return ResponseEntity.ok(bffService.obtenerListaEspera());
    }

    /**
     * Obtiene pacientes en espera filtrados por especialidad.
     * @param especialidad nombre de la especialidad
     * @return respuesta estandarizada con la lista filtrada
     */
    @Operation(summary = "Obtener lista de espera por especialidad")
    @ApiResponse(responseCode = "200", description = "Lista filtrada obtenida exitosamente")
    @GetMapping("/solicitudes/especialidad")
    public ResponseEntity<ApiResponseDTO<List<ListaEsperaDTO>>> obtenerListaEsperaPorEspecialidad(
            @RequestParam String especialidad) {
        return ResponseEntity.ok(bffService.obtenerListaEsperaPorEspecialidad(especialidad));
    }

    /**
     * Obtiene el resumen de un paciente en lista de espera.
     * @param pacienteId ID del paciente
     * @return respuesta estandarizada con el resumen del paciente
     */
    @Operation(summary = "Obtener resumen de paciente en lista de espera")
    @ApiResponse(responseCode = "200", description = "Resumen obtenido exitosamente")
    @GetMapping("/solicitudes/paciente/{pacienteId}")
    public ResponseEntity<ApiResponseDTO<PacienteResumenDTO>> obtenerResumenPaciente(
            @PathVariable Long pacienteId) {
        return ResponseEntity.ok(bffService.obtenerResumenPaciente(pacienteId));
    }
}