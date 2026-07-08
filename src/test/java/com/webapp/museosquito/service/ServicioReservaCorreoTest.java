package com.webapp.museosquito.service;

import com.webapp.museosquito.model.FranjaReserva;
import com.webapp.museosquito.model.Museo;
import com.webapp.museosquito.model.Reserva;
import com.webapp.museosquito.repository.RepositorioFranjaReserva;
import com.webapp.museosquito.repository.RepositorioReserva;
import jakarta.mail.MessagingException;
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
 * Pruebas unitarias para el envío de correos electrónicos ligado a reservas.
 * HU-10: Recibir correo electrónico de confirmación al realizar una reserva exitosa.
 * HU-11: Recibir correo electrónico al cancelar una reserva.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("HU-10 / HU-11 - Correos de confirmación y cancelación de reserva")
class ServicioReservaCorreoTest {

    @Mock private RepositorioFranjaReserva repoFranja;
    @Mock private RepositorioReserva       repoReserva;
    @Mock private CorreoService            correoService;

    private ServicioReserva servicio;
    private FranjaReserva   franjaConCupos;
    private Reserva         reservaActiva;

    @BeforeEach
    void setUp() {
        servicio = new ServicioReserva(repoFranja, repoReserva, correoService);

        Museo museo = new Museo("Museo Nacional", "Av. 12 de Octubre y Patria", "desc",
                "Historia", "022222222", "https://museo.ec", "img.png",
                5.0, 2.0, 3.0);

        franjaConCupos = new FranjaReserva(museo, "2026-06-02", "09:00", "10:00", 20);
        franjaConCupos.setAforoOcupado(5); // quedan 15 cupos

        reservaActiva = new Reserva(franjaConCupos, "Ana López", "ana@test.com",
                2, "2026-05-15", "RES-ABCD1234");
        reservaActiva.setId(1);
    }

    // ── HU-10 Escenario 1: correo de confirmación enviado ──────────────────

    @Test
    @DisplayName("HU-10 Escenario 1 - Se envía el correo de confirmación tras reserva exitosa")
    void dadoReservaExitosa_cuandoQuedaRegistrada_entoncesEnviaCorreoConfirmacion() throws Exception {
        when(repoFranja.buscarPorId(1)).thenReturn(franjaConCupos);
        when(repoFranja.actualizar(any())).thenReturn(franjaConCupos);
        when(repoReserva.guardar(any())).thenReturn(reservaActiva);

        servicio.crearReserva(1, "Ana López", "ana@test.com", 2);

        verify(correoService, times(1)).enviarConfirmacionReserva(reservaActiva);
    }

    // ── HU-10 Escenario 2: no se envía correo si la reserva falla ──────────

    @Test
    @DisplayName("HU-10 Escenario 2 - No se envía correo si la reserva no se registra por falta de cupos")
    void dadoSinCupos_cuandoFallaReserva_entoncesNoEnviaCorreo() {
        FranjaReserva franjaSinCupos = new FranjaReserva(
                franjaConCupos.getMuseo(), "2026-06-02", "10:00", "11:00", 20);
        franjaSinCupos.setAforoOcupado(20);
        when(repoFranja.buscarPorId(2)).thenReturn(franjaSinCupos);

        assertThrows(IllegalStateException.class,
                () -> servicio.crearReserva(2, "Pedro Gómez", "pedro@test.com", 1));

        verifyNoInteractions(correoService);
        verify(repoReserva, never()).guardar(any());
    }

    // ── HU-10 Escenario 3: fallo de envío no afecta la reserva ─────────────

    @Test
    @DisplayName("HU-10 Escenario 3 - Un fallo del correo no afecta la reserva ya registrada")
    void dadoFalloEnvioCorreo_cuandoReservaYaRegistrada_entoncesReservaPermaneceIntacta() throws Exception {
        when(repoFranja.buscarPorId(1)).thenReturn(franjaConCupos);
        when(repoFranja.actualizar(any())).thenReturn(franjaConCupos);
        when(repoReserva.guardar(any())).thenReturn(reservaActiva);
        doThrow(new MessagingException("SMTP no disponible"))
                .when(correoService).enviarConfirmacionReserva(any());

        Reserva resultado = assertDoesNotThrow(
                () -> servicio.crearReserva(1, "Ana López", "ana@test.com", 2),
                "Un fallo en el envío del correo no debe propagarse ni afectar la reserva");

        assertNotNull(resultado);
        assertEquals("RES-ABCD1234", resultado.getCodigoConfirmacion());
        verify(repoReserva, times(1)).guardar(any());
        // La reserva no se elimina ni se revierte: no se invoca ningún método adicional sobre ella.
        verifyNoMoreInteractions(repoReserva);
    }

    // ── HU-11 Escenario 1: correo de cancelación enviado ───────────────────

    @Test
    @DisplayName("HU-11 Escenario 1 - Se envía el correo de cancelación tras cancelar la reserva")
    void dadoCancelacionExitosa_cuandoSeLiberaCupo_entoncesEnviaCorreoCancelacion() throws Exception {
        when(repoReserva.buscarPorId(1)).thenReturn(reservaActiva);
        when(repoFranja.buscarPorId(franjaConCupos.getId())).thenReturn(franjaConCupos);
        when(repoReserva.actualizar(any())).thenReturn(reservaActiva);

        servicio.cancelarReserva(1, "ana@test.com");

        verify(correoService, times(1)).enviarCancelacionReserva(reservaActiva);
    }

    // ── HU-11 Escenario 2: no se envía correo si la cancelación falla ──────

    @Test
    @DisplayName("HU-11 Escenario 2 - No se envía correo si la cancelación no se procesa")
    void dadoReservaInexistente_cuandoFallaCancelacion_entoncesNoEnviaCorreo() {
        when(repoReserva.buscarPorId(99)).thenReturn(null);

        assertThrows(IllegalArgumentException.class,
                () -> servicio.cancelarReserva(99, "ana@test.com"));

        verifyNoInteractions(correoService);
        verify(repoReserva, never()).actualizar(any());
    }

    @Test
    @DisplayName("HU-11 Escenario 2 - No se envía correo si la reserva ya estaba cancelada")
    void dadoReservaYaCancelada_cuandoSeIntentaCancelar_entoncesNoEnviaCorreo() {
        reservaActiva.setActiva(false);
        when(repoReserva.buscarPorId(1)).thenReturn(reservaActiva);

        assertThrows(IllegalStateException.class,
                () -> servicio.cancelarReserva(1, "ana@test.com"));

        verifyNoInteractions(correoService);
    }

    // ── Un fallo de envío no afecta la cancelación ya completada ───────────

    @Test
    @DisplayName("Un fallo del correo de cancelación no revierte la cancelación ya registrada")
    void dadoFalloEnvioCorreoCancelacion_cuandoCancelacionYaCompletada_entoncesNoAfectaFlujo() throws Exception {
        when(repoReserva.buscarPorId(1)).thenReturn(reservaActiva);
        when(repoFranja.buscarPorId(franjaConCupos.getId())).thenReturn(franjaConCupos);
        when(repoReserva.actualizar(any())).thenReturn(reservaActiva);
        doThrow(new MessagingException("SMTP no disponible"))
                .when(correoService).enviarCancelacionReserva(any());

        assertDoesNotThrow(() -> servicio.cancelarReserva(1, "ana@test.com"));

        assertFalse(reservaActiva.isActiva(), "La reserva debe quedar marcada como cancelada");
        verify(repoReserva, times(1)).actualizar(any());
    }
}
