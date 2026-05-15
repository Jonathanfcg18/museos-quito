package com.webapp.museosquito.service;

import com.webapp.museosquito.model.AdminMuseo;
import com.webapp.museosquito.model.FranjaReserva;
import com.webapp.museosquito.model.Museo;
import com.webapp.museosquito.repository.RepositorioAdminMuseo;
import com.webapp.museosquito.repository.RepositorioFranjaReserva;
import com.webapp.museosquito.repository.RepositorioMuseo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para ServicioAdmin.
 * HU4: Gestionar horarios y aforo.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("HU4 - Gestionar horarios y aforo")
class ServicioAdminTest {

    @Mock private RepositorioAdminMuseo    repoAdmin;
    @Mock private RepositorioFranjaReserva repoFranja;
    @Mock private RepositorioMuseo         repoMuseo;

    private ServicioAdmin servicio;
    private Museo         museo;

    @BeforeEach
    void setUp() {
        servicio = new ServicioAdmin(repoAdmin, repoFranja, repoMuseo);
        museo    = new Museo();
    }

    // ── CP4.1: Configuración obligatoria de aforo ──────────────────────────

    @Test
    @DisplayName("CP4.1 - El sistema bloquea guardar franja sin aforo máximo")
    void dadoAforoCero_cuandoSeCreanFranja_entoncesLanzaExcepcion() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> servicio.crearFranja(1, "2026-06-10", "09:00", "10:00", 0));

        assertTrue(ex.getMessage().contains("aforo máximo válido"),
                "El mensaje debe indicar que el aforo es obligatorio");
        verify(repoFranja, never()).guardar(any());
    }

    @Test
    @DisplayName("CP4.1 - El sistema bloquea guardar franja con aforo negativo")
    void dadoAforoNegativo_cuandoSeCreanFranja_entoncesLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> servicio.crearFranja(1, "2026-06-10", "09:00", "10:00", -5));
        verify(repoFranja, never()).guardar(any());
    }

    @Test
    @DisplayName("CP4.1 - Franja se crea exitosamente con aforo válido")
    void dadoAforoValido_cuandoSeCreanFranja_entoncesSeCrea() {
        when(repoMuseo.buscarPorId(1)).thenReturn(museo);
        FranjaReserva franjaEsperada = new FranjaReserva(museo, "2026-06-10",
                "09:00", "10:00", 20);
        when(repoFranja.guardar(any())).thenReturn(franjaEsperada);

        FranjaReserva resultado = servicio.crearFranja(1, "2026-06-10",
                "09:00", "10:00", 20);

        assertNotNull(resultado);
        assertEquals(20, resultado.getAforoMaximo());
        verify(repoFranja).guardar(any());
    }

    @Test
    @DisplayName("CP4.1 - Modificar franja sin aforo también es bloqueado")
    void dadoModificarSinAforo_cuandoSeLlama_entoncesLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> servicio.modificarFranja(1, "2026-06-10",
                        "09:00", "10:00", 0));
        verify(repoFranja, never()).actualizar(any());
    }

    // ── CP4.2: Bloqueo automático por sobrecupo ────────────────────────────

    @Test
    @DisplayName("CP4.2 - Franja sin cupos aparece como agotada (hayCupos false)")
    void dadoAforoLleno_cuandoSeVerifica_entoncesHayCuposEsFalse() {
        FranjaReserva franja = new FranjaReserva(museo, "2026-06-10",
                "09:00", "10:00", 20);
        franja.setAforoOcupado(20);

        assertFalse(franja.hayCupos(),
                "Con aforo lleno, hayCupos debe retornar false");
        assertEquals(0, franja.getCuposDisponibles());
    }

    @Test
    @DisplayName("CP4.2 - No se puede reducir aforo por debajo de reservas actuales")
    void dadoReducirAforoMenorQueOcupado_cuandoSeModifica_entoncesLanzaExcepcion() {
        FranjaReserva franja = new FranjaReserva(museo, "2026-06-10",
                "09:00", "10:00", 20);
        franja.setAforoOcupado(15);
        when(repoFranja.buscarPorId(1)).thenReturn(franja);

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> servicio.modificarFranja(1, "2026-06-10",
                        "09:00", "10:00", 10));

        assertTrue(ex.getMessage().contains("15"),
                "El mensaje debe indicar las reservas actuales");
        verify(repoFranja, never()).actualizar(any());
    }

    @Test
    @DisplayName("CP4.2 - Modificar aforo con valor válido funciona correctamente")
    void dadoNuevoAforoValido_cuandoSeModifica_entoncesSeActualiza() {
        FranjaReserva franja = new FranjaReserva(museo, "2026-06-10",
                "09:00", "10:00", 20);
        franja.setAforoOcupado(5);
        when(repoFranja.buscarPorId(1)).thenReturn(franja);
        when(repoFranja.actualizar(any())).thenReturn(franja);

        FranjaReserva resultado = servicio.modificarFranja(1, "2026-06-10",
                "09:00", "10:00", 30);

        assertNotNull(resultado);
        verify(repoFranja).actualizar(any());
    }

    // ── Autenticación ──────────────────────────────────────────────────────

    @Test
    @DisplayName("Autenticación exitosa con credenciales correctas")
    void dadoCredencialesCorrectas_cuandoSeAutentica_entoncesRetornaAdmin() {
        AdminMuseo admin = new AdminMuseo("Admin", "admin@test.com", "pass123", museo);
        when(repoAdmin.buscarPorEmail("admin@test.com")).thenReturn(admin);

        AdminMuseo resultado = servicio.autenticar("admin@test.com", "pass123");

        assertNotNull(resultado);
        assertEquals("Admin", resultado.getNombre());
    }

    @Test
    @DisplayName("Autenticación falla con contraseña incorrecta")
    void dadoPasswordIncorrecto_cuandoSeAutentica_entoncesRetornaNull() {
        AdminMuseo admin = new AdminMuseo("Admin", "admin@test.com", "pass123", museo);
        when(repoAdmin.buscarPorEmail("admin@test.com")).thenReturn(admin);

        AdminMuseo resultado = servicio.autenticar("admin@test.com", "wrongpass");

        assertNull(resultado);
    }
}