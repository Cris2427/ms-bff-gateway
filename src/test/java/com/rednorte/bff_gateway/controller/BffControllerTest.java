package com.rednorte.bff_gateway.controller;

import com.rednorte.bff_gateway.dto.ApiResponseDTO;
import com.rednorte.bff_gateway.dto.ListaEsperaDTO;
import com.rednorte.bff_gateway.dto.PacienteResumenDTO;
import com.rednorte.bff_gateway.dto.ReasignacionDTO;
import com.rednorte.bff_gateway.dto.ReasignacionRequestDTO;
import com.rednorte.bff_gateway.service.BffService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para {@link BffController}.
 * Verifica el comportamiento de cada endpoint usando Mockito puro.
 */
@ExtendWith(MockitoExtension.class)
class BffControllerTest {

    @Mock
    private BffService bffService;

    @InjectMocks
    private BffController bffController;

    // Reasignaciones

    @Test
    @DisplayName("POST /reasignaciones debe retornar 201 con reasignacion creada")
    void crearReasignacion_debeRetornar201() {
        ReasignacionRequestDTO request = ReasignacionRequestDTO.builder()
                .citaId(1L).pacienteCanceladorId(1L).build();

        ApiResponseDTO<ReasignacionDTO> respuesta = ApiResponseDTO.ok(
                ReasignacionDTO.builder().reasignacionId(1L).estado("COMPLETADA").build(),
                "Reasignacion creada exitosamente");

        when(bffService.crearReasignacion(any())).thenReturn(respuesta);

        ResponseEntity<ApiResponseDTO<ReasignacionDTO>> result =
                bffController.crearReasignacion(request);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertTrue(result.getBody().isSuccess());
        verify(bffService).crearReasignacion(any());
    }

    @Test
    @DisplayName("GET /reasignaciones/{id} debe retornar 200 con reasignacion encontrada")
    void obtenerReasignacion_debeRetornar200() {
        ApiResponseDTO<ReasignacionDTO> respuesta = ApiResponseDTO.ok(
                ReasignacionDTO.builder().reasignacionId(1L).estado("COMPLETADA").build(),
                "Reasignacion encontrada");

        when(bffService.obtenerReasignacion(1L)).thenReturn(respuesta);

        ResponseEntity<ApiResponseDTO<ReasignacionDTO>> result =
                bffController.obtenerReasignacion(1L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(result.getBody().isSuccess());
        assertEquals(1L, result.getBody().getData().getReasignacionId());
        verify(bffService).obtenerReasignacion(1L);
    }

    @Test
    @DisplayName("GET /reasignaciones debe retornar 200 con lista completa")
    void listarReasignaciones_debeRetornar200() {
        List<ReasignacionDTO> lista = Arrays.asList(
                ReasignacionDTO.builder().reasignacionId(1L).estado("COMPLETADA").build(),
                ReasignacionDTO.builder().reasignacionId(2L).estado("FALLIDA").build()
        );

        ApiResponseDTO<List<ReasignacionDTO>> respuesta =
                ApiResponseDTO.ok(lista, "Lista obtenida");

        when(bffService.listarReasignaciones()).thenReturn(respuesta);

        ResponseEntity<ApiResponseDTO<List<ReasignacionDTO>>> result =
                bffController.listarReasignaciones();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(2, result.getBody().getData().size());
        verify(bffService).listarReasignaciones();
    }

    @Test
    @DisplayName("GET /reasignaciones/estado debe retornar 200 con lista filtrada")
    void listarPorEstado_debeRetornar200() {
        List<ReasignacionDTO> lista = Arrays.asList(
                ReasignacionDTO.builder().reasignacionId(1L).estado("PENDIENTE").build()
        );

        ApiResponseDTO<List<ReasignacionDTO>> respuesta =
                ApiResponseDTO.ok(lista, "Lista filtrada");

        when(bffService.listarPorEstado("PENDIENTE")).thenReturn(respuesta);

        ResponseEntity<ApiResponseDTO<List<ReasignacionDTO>>> result =
                bffController.listarPorEstado("PENDIENTE");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().getData().size());
        verify(bffService).listarPorEstado("PENDIENTE");
    }

    // Lista Espera

    @Test
    @DisplayName("GET /solicitudes debe retornar 200 con lista completa")
    void obtenerListaEspera_debeRetornar200() {
        List<ListaEsperaDTO> lista = Arrays.asList(
                ListaEsperaDTO.builder().id(1L).especialidad("Cardiologia").build(),
                ListaEsperaDTO.builder().id(2L).especialidad("Neurologia").build()
        );

        ApiResponseDTO<List<ListaEsperaDTO>> respuesta =
                ApiResponseDTO.ok(lista, "Lista de espera obtenida");

        when(bffService.obtenerListaEspera()).thenReturn(respuesta);

        ResponseEntity<ApiResponseDTO<List<ListaEsperaDTO>>> result =
                bffController.obtenerListaEspera();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(2, result.getBody().getData().size());
        verify(bffService).obtenerListaEspera();
    }

    @Test
    @DisplayName("GET /solicitudes/especialidad debe retornar 200 con lista filtrada")
    void obtenerListaEsperaPorEspecialidad_debeRetornar200() {
        List<ListaEsperaDTO> lista = Arrays.asList(
                ListaEsperaDTO.builder().id(1L).especialidad("Cardiologia").build()
        );

        ApiResponseDTO<List<ListaEsperaDTO>> respuesta =
                ApiResponseDTO.ok(lista, "Lista filtrada por especialidad");

        when(bffService.obtenerListaEsperaPorEspecialidad("Cardiologia")).thenReturn(respuesta);

        ResponseEntity<ApiResponseDTO<List<ListaEsperaDTO>>> result =
                bffController.obtenerListaEsperaPorEspecialidad("Cardiologia");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().getData().size());
        verify(bffService).obtenerListaEsperaPorEspecialidad("Cardiologia");
    }

    @Test
    @DisplayName("GET /solicitudes/paciente/{id} debe retornar 200 con resumen del paciente")
    void obtenerResumenPaciente_debeRetornar200() {
        PacienteResumenDTO resumen = PacienteResumenDTO.builder()
                .id(1L).nombreCompleto("Juan Perez").build();

        ApiResponseDTO<PacienteResumenDTO> respuesta =
                ApiResponseDTO.ok(resumen, "Resumen obtenido");

        when(bffService.obtenerResumenPaciente(1L)).thenReturn(respuesta);

        ResponseEntity<ApiResponseDTO<PacienteResumenDTO>> result =
                bffController.obtenerResumenPaciente(1L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1L, result.getBody().getData().getId());
        verify(bffService).obtenerResumenPaciente(1L);
    }
}