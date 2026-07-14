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
import com.rednorte.bff_gateway.dto.ReasignacionResultadoDTO;
import com.rednorte.bff_gateway.dto.PacienteDTO;
import com.rednorte.bff_gateway.dto.CitaDTO;
import com.rednorte.bff_gateway.dto.LoginRequestDTO;
import com.rednorte.bff_gateway.dto.LoginResponseDTO;
import com.rednorte.bff_gateway.dto.RegistroRequestDTO;
import com.rednorte.bff_gateway.dto.CitaRequestDTO;
import com.rednorte.bff_gateway.dto.RegistroPacienteDTO;

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

    @Operation(summary = "Listar pacientes")
    @ApiResponse(responseCode = "200", description = "Lista de pacientes obtenida")
    @GetMapping("/pacientes")
    public ResponseEntity<ApiResponseDTO<List<PacienteDTO>>> obtenerPacientes() {
        return ResponseEntity.ok(bffService.obtenerPacientes());
    }

    @Operation(summary = "Listar citas")
    @ApiResponse(responseCode = "200", description = "Lista de citas obtenida")
    @GetMapping("/citas")
    public ResponseEntity<ApiResponseDTO<List<CitaDTO>>> obtenerCitas() {
        return ResponseEntity.ok(bffService.obtenerCitas());
    }

    @Operation(summary = "Login de usuario", description = "Autentica un usuario contra ms-auth")
    @ApiResponses ({
            @ApiResponse(responseCode = "200", description = "Login exitoso"),
            @ApiResponse(responseCode = "401", description = "Credenciales invalidas")
    })
    @PostMapping("/auth/login")
    public ResponseEntity<ApiResponseDTO<LoginResponseDTO>> login(
            @RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(bffService.login(request));
    }

    @Operation(summary = "Registro de usuario", description = "Crea un nuevo usuario en ms-auth")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuario registrado"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos o usuario existente")
    })
    @PostMapping("/auth/registro")
    public ResponseEntity<ApiResponseDTO<LoginResponseDTO>> registrar(
            @RequestBody RegistroRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bffService.registrar(request));
    }

    @Operation(summary = "Agendar una nueva cita")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cita agendada"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos")
    })
    @PostMapping("/citas")
    public ResponseEntity<ApiResponseDTO<CitaDTO>> crearCita(
            @RequestBody CitaRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bffService.crearCita(request));
    }

    @Operation(summary = "Listar citas de un paciente")
    @ApiResponse(responseCode = "200", description = "Citas del paciente")
    @GetMapping("/citas/paciente/{pacienteId}")
    public ResponseEntity<ApiResponseDTO<List<CitaDTO>>> obtenerCitasPorPaciente(
            @PathVariable Long pacienteId) {
        return ResponseEntity.ok(bffService.obtenerCitasPorPaciente(pacienteId));
    }

    @Operation(summary = "Listar citas de un medico")
    @ApiResponse(responseCode = "200", description = "Citas del medico")
    @GetMapping("/citas/medico")
    public ResponseEntity<ApiResponseDTO<List<CitaDTO>>> obtenerCitasPorMedico(
            @RequestParam String nombreMedico) {
        return ResponseEntity.ok(bffService.obtenerCitasPorMedico(nombreMedico));
    }

    @Operation(summary = "Procesar reasignacion real",
            description = "Cancela la cita, libera la hora y la reasigna al paciente de mayor prioridad")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reasignacion procesada"),
            @ApiResponse(responseCode = "404", description = "Cita no encontrada")
    })
    @PostMapping("/reasignaciones/procesar/{citaId}")
    public ResponseEntity<ApiResponseDTO<ReasignacionResultadoDTO>> procesarReasignacion(
            @PathVariable Long citaId,
            @RequestParam(required = false) String motivo) {
        return ResponseEntity.ok(bffService.procesarReasignacion(citaId, motivo));
    }

    @Operation(summary = "Registro de paciente", description = "Crea la ficha del paciente y su cuenta de acceso")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cuenta creada"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos o usuario existente")
    })
    @PostMapping("/auth/registro-paciente")
    public ResponseEntity<ApiResponseDTO<LoginResponseDTO>> registrarPaciente(
            @RequestBody RegistroPacienteDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bffService.registrarPaciente(request));
    }
}