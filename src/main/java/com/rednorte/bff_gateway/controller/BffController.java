package com.rednorte.bff_gateway.controller;


import com.rednorte.bff_gateway.dto.*;
import com.rednorte.bff_gateway.service.BffService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador principal de BFF Gateway de RedNorte
 * Expone los endpoints unificados paara el front,
 * delegando la logica al link de BffService
 */
@RestController
@RequestMapping("/api/bff")
@RequiredArgsConstructor
@Tag(name = "BFF gateway", description = "Endpoints unificados para el frontend de RedNorte")

public class BffController {

    private final BffService bffService;

    /**
     * crea nueva reasignacion de cita
     * @param request datos de la solicitud
     * @return reasignacion creada con estado 201
     */
    @Operation(summary = "Crear reasignacion", description = "Inicia el proceso de reasignacion automatica de una cita cancelada")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Reasignacion creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada invalidos"),
            @ApiResponse(responseCode = "503", description = "Servicio no disponible")
    })
    @PostMapping("/reasignaciones")
    public ResponseEntity<ApiResponseDTO<ReasignacionDTO>> crearReasignacion(
            @Valid @RequestBody ReasignacionRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bffService.crearReasignacion(request));
    }

    /**
     * obtiene una reasignacion
     * @param id
     * @return devuelve la reasignacion encontrada
     */
    @Operation(summary = "Obtener reasignacion por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reasignacion encontrada"),
            @ApiResponse(responseCode = "503", description = "Servicio no disponible")
    })
    @GetMapping("/reasignaciones/{id}")
    public ResponseEntity<ApiResponseDTO<ReasignacionDTO>> obtenerReasignacion(
            @PathVariable Long id) {
        return ResponseEntity.ok(bffService.obtenerReasignacion(id));
    }

    /**
     * Lista todas las reasignaciones registradas
     * @return lista completa de reasignacione
     */
    @Operation(summary = "Listar todas las reasignaciones")
    @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    @GetMapping("/reasignaciones")
    public ResponseEntity<ApiResponseDTO<List<ReasignacionDTO>>> listarReasignaciones() {
        return ResponseEntity.ok(bffService.listarReasignaciones());
    }

    /**
     * Lista reasignaciones filtradas por estado
     * @param estado estado a filtrar (PENDIENTE, COMPLETADA, FALLIDA, CANCELADA)
     * @return lista filtrada
     */
    @Operation(summary = "Listar reasignaciones por estado")
    @ApiResponse(responseCode = "200", description = "Lista filtrada obtenida exitosamente")
    @GetMapping("/reasignaciones/estado")
    public ResponseEntity<ApiResponseDTO<List<ReasignacionDTO>>> listarPorEstado(
            @RequestParam String estado) {
        return ResponseEntity.ok(bffService.listarPorEstado(estado));
    }

    /**
     * Obtiene la lista completa de pacientes en espera
     * @return lista de espera completa
     */
    @Operation(summary = "Obtener lista de espera completa")
    @ApiResponse(responseCode = "200", description = "Lista de espera obtenida exitosamente")
    @GetMapping("/lista-espera")
    public ResponseEntity<ApiResponseDTO<List<ListaEsperaDTO>>> obtenerListaEspera() {
        return ResponseEntity.ok(bffService.obtenerListaEspera());
    }

    /**
     * Obtiene la lista de espera filtrada por especialidad
     * @param especialidad nombre de la especialidad medica
     * @return lista filtrada
     */
    @Operation(summary = "Obtener lista de espera por especialidad")
    @ApiResponse(responseCode = "200", description = "Lista filtrada obtenida exitosamente")
    @GetMapping("/lista-espera/especialidad")
    public ResponseEntity<ApiResponseDTO<List<ListaEsperaDTO>>> obtenerListaEsperaPorEspecialidad(
            @RequestParam String especialidad) {
        return ResponseEntity.ok(bffService.obtenerListaEsperaPorEspecialidad(especialidad));
    }

    /**
     * Obtiene el resumen de un paciente en lista de espera
     * @param pacienteId ID del paciente
     * @return resumen del paciente con su posicion
     */
    @Operation(summary = "Obtener resumen de paciente en lista de espera")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Resumen obtenido exitosamente"),
            @ApiResponse(responseCode = "503", description = "Servicio no disponible")
    })
    @GetMapping("/lista-espera/paciente/{pacienteId}")
    public ResponseEntity<ApiResponseDTO<PacienteResumenDTO>> obtenerResumenPaciente(
            @PathVariable Long pacienteId) {
        return ResponseEntity.ok(bffService.obtenerResumenPaciente(pacienteId));
    }
}
