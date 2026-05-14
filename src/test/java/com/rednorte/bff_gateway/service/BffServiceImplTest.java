package com.rednorte.bff_gateway.service;

import com.rednorte.bff_gateway.client.ListaEsperaClient;
import com.rednorte.bff_gateway.client.ReasignacionClient;
import com.rednorte.bff_gateway.dto.ApiResponseDTO;
import com.rednorte.bff_gateway.dto.ListaEsperaDTO;
import com.rednorte.bff_gateway.dto.PacienteResumenDTO;
import com.rednorte.bff_gateway.dto.ReasignacionDTO;
import com.rednorte.bff_gateway.dto.ReasignacionRequestDTO;
import com.rednorte.bff_gateway.service.impl.BffServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para {@link BffServiceImpl}.
 * Verifica la logica de delegacion y los metodos fallback del Circuit Breaker.
 */
@ExtendWith(MockitoExtension.class)
class BffServiceImplTest {

    @Mock
    private ReasignacionClient reasignacionClient;

    @Mock
    private ListaEsperaClient listaEsperaClient;

    @InjectMocks
    private BffServiceImpl bffService;

    //  Reasignaciones

    @Test
    @DisplayName("crearReasignacion debe retornar respuesta exitosa")
    void crearReasignacion_debeRetornarRespuestaExitosa() {
        ReasignacionRequestDTO request = ReasignacionRequestDTO.builder()
                .citaId(1L).pacienteCanceladorId(1L).build();

        ReasignacionDTO dto = ReasignacionDTO.builder()
                .reasignacionId(1L).estado("COMPLETADA").build();

        when(reasignacionClient.crearReasignacion(any())).thenReturn(dto);

        ApiResponseDTO<ReasignacionDTO> result = bffService.crearReasignacion(request);

        assertTrue(result.isSuccess());
        assertEquals(200, result.getStatus());
        assertNotNull(result.getData());
        verify(reasignacionClient).crearReasignacion(any());
    }

    @Test
    @DisplayName("obtenerReasignacion debe retornar respuesta exitosa")
    void obtenerReasignacion_debeRetornarRespuestaExitosa() {
        ReasignacionDTO dto = ReasignacionDTO.builder()
                .reasignacionId(1L).estado("COMPLETADA").build();

        when(reasignacionClient.obtenerPorId(1L)).thenReturn(dto);

        ApiResponseDTO<ReasignacionDTO> result = bffService.obtenerReasignacion(1L);

        assertTrue(result.isSuccess());
        assertEquals(1L, result.getData().getReasignacionId());
        verify(reasignacionClient).obtenerPorId(1L);
    }

    @Test
    @DisplayName("listarReasignaciones debe retornar lista completa")
    void listarReasignaciones_debeRetornarListaCompleta() {
        List<ReasignacionDTO> lista = Arrays.asList(
                ReasignacionDTO.builder().reasignacionId(1L).estado("COMPLETADA").build(),
                ReasignacionDTO.builder().reasignacionId(2L).estado("FALLIDA").build()
        );

        when(reasignacionClient.listarTodas()).thenReturn(lista);

        ApiResponseDTO<List<ReasignacionDTO>> result = bffService.listarReasignaciones();

        assertTrue(result.isSuccess());
        assertEquals(2, result.getData().size());
        verify(reasignacionClient).listarTodas();
    }

    @Test
    @DisplayName("listarPorEstado debe retornar lista filtrada")
    void listarPorEstado_debeRetornarListaFiltrada() {
        List<ReasignacionDTO> lista = Arrays.asList(
                ReasignacionDTO.builder().reasignacionId(1L).estado("PENDIENTE").build()
        );

        when(reasignacionClient.listarPorEstado("PENDIENTE")).thenReturn(lista);

        ApiResponseDTO<List<ReasignacionDTO>> result = bffService.listarPorEstado("PENDIENTE");

        assertTrue(result.isSuccess());
        assertEquals(1, result.getData().size());
        verify(reasignacionClient).listarPorEstado("PENDIENTE");
    }

    // Lista Espera

    @Test
    @DisplayName("obtenerListaEspera debe retornar lista completa")
    void obtenerListaEspera_debeRetornarListaCompleta() {
        List<ListaEsperaDTO> lista = Arrays.asList(
                ListaEsperaDTO.builder().id(1L).especialidad("Cardiologia").build(),
                ListaEsperaDTO.builder().id(2L).especialidad("Neurologia").build()
        );

        when(listaEsperaClient.obtenerListaCompleta()).thenReturn(lista);

        ApiResponseDTO<List<ListaEsperaDTO>> result = bffService.obtenerListaEspera();

        assertTrue(result.isSuccess());
        assertEquals(2, result.getData().size());
        verify(listaEsperaClient).obtenerListaCompleta();
    }

    @Test
    @DisplayName("obtenerListaEsperaPorEspecialidad debe retornar lista filtrada")
    void obtenerListaEsperaPorEspecialidad_debeRetornarListaFiltrada() {
        List<ListaEsperaDTO> lista = Arrays.asList(
                ListaEsperaDTO.builder().id(1L).especialidad("Cardiologia").build()
        );

        when(listaEsperaClient.obtenerPorEspecialidad("Cardiologia")).thenReturn(lista);

        ApiResponseDTO<List<ListaEsperaDTO>> result =
                bffService.obtenerListaEsperaPorEspecialidad("Cardiologia");

        assertTrue(result.isSuccess());
        assertEquals(1, result.getData().size());
        verify(listaEsperaClient).obtenerPorEspecialidad("Cardiologia");
    }

    @Test
    @DisplayName("obtenerResumenPaciente debe retornar resumen del paciente")
    void obtenerResumenPaciente_debeRetornarResumen() {
        PacienteResumenDTO resumen = PacienteResumenDTO.builder()
                .id(1L).nombreCompleto("Juan Perez").build();

        when(listaEsperaClient.obtenerPorPaciente(1L)).thenReturn(resumen);

        ApiResponseDTO<PacienteResumenDTO> result = bffService.obtenerResumenPaciente(1L);

        assertTrue(result.isSuccess());
        assertEquals(1L, result.getData().getId());
        verify(listaEsperaClient).obtenerPorPaciente(1L);
    }

    //Fallbacks

    @Test
    @DisplayName("fallbackReasignacion debe retornar respuesta de error 503")
    void fallbackReasignacion_debeRetornarError() {
        ApiResponseDTO<ReasignacionDTO> result =
                bffService.fallbackReasignacion(new RuntimeException("Servicio caido"));

        assertFalse(result.isSuccess());
        assertEquals(503, result.getStatus());
        assertNotNull(result.getMessage());
    }

    @Test
    @DisplayName("fallbackListaReasignacion debe retornar respuesta de error 503")
    void fallbackListaReasignacion_debeRetornarError() {
        ApiResponseDTO<List<ReasignacionDTO>> result =
                bffService.fallbackListaReasignacion(new RuntimeException("Servicio caido"));

        assertFalse(result.isSuccess());
        assertEquals(503, result.getStatus());
    }

    @Test
    @DisplayName("fallbackListaEspera debe retornar respuesta de error 503")
    void fallbackListaEspera_debeRetornarError() {
        ApiResponseDTO<List<ListaEsperaDTO>> result =
                bffService.fallbackListaEspera(new RuntimeException("Servicio caido"));

        assertFalse(result.isSuccess());
        assertEquals(503, result.getStatus());
    }

    @Test
    @DisplayName("fallbackResumenPaciente debe retornar respuesta de error 503")
    void fallbackResumenPaciente_debeRetornarError() {
        ApiResponseDTO<PacienteResumenDTO> result =
                bffService.fallbackResumenPaciente(new RuntimeException("Servicio caido"));

        assertFalse(result.isSuccess());
        assertEquals(503, result.getStatus());
    }
}