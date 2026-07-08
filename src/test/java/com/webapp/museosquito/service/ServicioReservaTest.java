package com.webapp.museosquito.service;

import com.webapp.museosquito.model.FranjaReserva;
import com.webapp.museosquito.model.Museo;
import com.webapp.museosquito.model.Reserva;
import com.webapp.museosquito.repository.RepositorioFranjaReserva;
import com.webapp.museosquito.repository.RepositorioReserva;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para ServicioReserva.
 * HU2: Reservar una visita.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("HU2 - Reservar una visita")
class ServicioReservaTest {

    @Mock private RepositorioFranjaReserva repoFranja;
    @Mock private RepositorioReserva       repoReserva;
    @Mock private CorreoService            correoService;

    private ServicioReserva servicio;
    private FranjaReserva   franjaConCupos;
    private FranjaReserva   franjaSinCupos;

    @BeforeEach
    void setUp() {
        // Se inyecta un CorreoService mockeado para no depender de un
        // servidor SMTP real durante las pruebas unitarias (HU-10 / HU-11).
        servicio = new ServicioReserva(repoFranja, repoReserva, correoService);

        Museo museo = new Museo();

        franjaConCupos = new FranjaReserva(museo, "2026-06-02", "09:00", "10:00", 20);
        franjaConCupos.setAforoOcupado(5);   // quedan 15 cupos

        franjaSinCupos = new FranjaReserva(museo, "2026-06-02", "10:00", "11:00", 20);
        franjaSinCupos.setAforoOcupado(20);  // aforo lleno
    }

    // ── CP2.1: Reserva exitosa con disponibilidad ──────────────────────────

    @Test
    @DisplayName("CP2.1 - El sistema registra la reserva cuando hay cupos disponibles")
    void dadoQueCuposDisponibles_cuandoSeReserva_entoncesSeRegistra() {
        when(repoFranja.buscarPorId(1)).thenReturn(franjaConCupos);
        when(repoFranja.actualizar(any())).thenReturn(franjaConCupos);
        Reserva mockReserva = new Reserva(franjaConCupos, "Ana López",
                "ana@test.com", 2, "2026-05-15", "RES-ABCD1234");
        when(repoReserva.guardar(any())).thenReturn(mockReserva);

        Reserva resultado = servicio.crearReserva(1, "Ana López", "ana@test.com", 2);

        assertNotNull(resultado, "La reserva no debe ser null");
        assertEquals("Ana López", resultado.getNombreVisitante());
        assertNotNull(resultado.getCodigoConfirmacion(), "Debe tener código de confirmación");
        verify(repoFranja).actualizar(any());
        verify(repoReserva).guardar(any());
    }

    @Test
    @DisplayName("CP2.1 - El aforo ocupado aumenta al registrar la reserva")
    void dadoUnaReserva_cuandoSeConfirma_entoncesAforoAumenta() {
        when(repoFranja.buscarPorId(1)).thenReturn(franjaConCupos);
        when(repoFranja.actualizar(any())).thenReturn(franjaConCupos);
        when(repoReserva.guardar(any())).thenReturn(new Reserva());

        servicio.crearReserva(1, "Luis Torres", "luis@test.com", 3);

        assertEquals(8, franjaConCupos.getAforoOcupado(), "El aforo debió aumentar en 3");
    }

    // ── CP2.2: Validación de aforo sin cupos ──────────────────────────────

    @Test
    @DisplayName("CP2.2 - El sistema impide reservar cuando el aforo está lleno")
    void dadoAfroLleno_cuandoSeIntentaReservar_entoncesLanzaExcepcion() {
        when(repoFranja.buscarPorId(2)).thenReturn(franjaSinCupos);

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> servicio.crearReserva(2, "Pedro Gómez", "pedro@test.com", 1));

        assertTrue(ex.getMessage().contains("no tiene cupos disponibles"),
                "El mensaje debe indicar falta de cupos");
        verify(repoReserva, never()).guardar(any());
    }

    @Test
    @DisplayName("CP2.2 - No se actualiza el aforo cuando no hay cupos")
    void dadoAforoLleno_cuandoSeIntentaReservar_entoncesNoActualizaAforo() {
        when(repoFranja.buscarPorId(2)).thenReturn(franjaSinCupos);

        assertThrows(IllegalStateException.class,
                () -> servicio.crearReserva(2, "María Paz", "maria@test.com", 1));

        verify(repoFranja, never()).actualizar(any());
    }

    // ── Validaciones de datos ─────────────────────────────────────────────

    @Test
    @DisplayName("Datos inválidos - nombre vacío lanza excepción")
    void dadoNombreVacio_cuandoSeReserva_entoncesLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> servicio.crearReserva(1, "", "ana@test.com", 1));
    }

    @Test
    @DisplayName("Datos inválidos - email sin @ lanza excepción")
    void dadoEmailInvalido_cuandoSeReserva_entoncesLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> servicio.crearReserva(1, "Ana López", "emailsinArroba", 1));
    }

    @Test
    @DisplayName("Datos inválidos - cantidad cero lanza excepción")
    void dadoCantidadCero_cuandoSeReserva_entoncesLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> servicio.crearReserva(1, "Ana López", "ana@test.com", 0));
    }

    @Test
    @DisplayName("Franja inexistente lanza excepción")
    void dadoFranjaInexistente_cuandoSeReserva_entoncesLanzaExcepcion() {
        when(repoFranja.buscarPorId(999)).thenReturn(null);

        assertThrows(IllegalArgumentException.class,
                () -> servicio.crearReserva(999, "Ana López", "ana@test.com", 1));
    }
}