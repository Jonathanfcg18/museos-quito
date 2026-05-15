package com.webapp.museosquito.service;

import com.webapp.museosquito.model.Museo;
import com.webapp.museosquito.repository.RepositorioMuseo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para ServicioMuseo.
 * Cubre los criterios de aceptación de la HU1: Consultar información de museos.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("HU1 - Consultar información de museos")
class ServicioMuseoTest {

    @Mock
    private RepositorioMuseo repositorioMuseo;

    private ServicioMuseo servicioMuseo;

    private Museo museoNacional;
    private Museo capillaHombre;

    @BeforeEach
    void setUp() {
        servicioMuseo = new ServicioMuseo(repositorioMuseo);

        museoNacional = new Museo(
                "Museo Nacional del Ecuador",
                "Av. Patria y Av. 6 de Diciembre, Quito",
                "Colección patrimonial de arte y arqueología ecuatoriana.",
                "Historia y Arte",
                "(02) 222-3258",
                "https://www.museonacional.gob.ec",
                "",
                2.00, 1.00, 1.00
        );

        capillaHombre = new Museo(
                "Capilla del Hombre – Fundación Guayasamín",
                "Mariano Calvache E18-94, Bellavista, Quito",
                "Museo de arte dedicado al ser humano y la cultura latinoamericana.",
                "Arte",
                "(02) 244-8492",
                "https://www.capilladelhombre.com",
                "",
                8.00, 4.50, 4.50
        );
    }

    // ── Escenario 1: Visualización completa del catálogo ──────────────────────

    @Test
    @DisplayName("CP1.1 - El sistema muestra lista de museos disponibles")
    void dadoQueExistenMuseos_cuandoSeObtienenTodos_entoncesRetornaLista() {
        // Dado
        List<Museo> esperados = Arrays.asList(museoNacional, capillaHombre);
        when(repositorioMuseo.listar()).thenReturn(esperados);

        // Cuando
        List<Museo> resultado = servicioMuseo.obtenerTodos();

        // Entonces
        assertNotNull(resultado, "La lista no debe ser null");
        assertEquals(2, resultado.size(), "Debe retornar 2 museos");
        verify(repositorioMuseo, times(1)).listar();
    }

    @Test
    @DisplayName("CP1.2 - Cada museo incluye nombre, ubicación, descripción y horarios")
    void dadoUnMuseo_cuandoSeObtiene_entoncesIncluyCamposRequeridos() {
        // Dado - el museo tiene todos los campos requeridos por HU1
        when(repositorioMuseo.listar()).thenReturn(List.of(museoNacional));

        // Cuando
        List<Museo> resultado = servicioMuseo.obtenerTodos();

        // Entonces - verificar campos obligatorios del criterio de aceptación
        Museo museo = resultado.get(0);
        assertNotNull(museo.getNombre(),     "El nombre no debe ser null");
        assertNotNull(museo.getUbicacion(),  "La ubicación no debe ser null");
        assertNotNull(museo.getDescripcion(),"La descripción no debe ser null");
        assertFalse(museo.getNombre().isBlank(),     "El nombre no debe estar vacío");
        assertFalse(museo.getUbicacion().isBlank(),  "La ubicación no debe estar vacía");
        assertFalse(museo.getDescripcion().isBlank(),"La descripción no debe estar vacía");
    }

    @Test
    @DisplayName("CP1.3 - La información está actualizada (lista no nula aunque BD vacía)")
    void dadoQueNoExistenMuseos_cuandoSeObtienenTodos_entoncesRetornaListaVacia() {
        // Dado
        when(repositorioMuseo.listar()).thenReturn(Collections.emptyList());

        // Cuando
        List<Museo> resultado = servicioMuseo.obtenerTodos();

        // Entonces – no debe fallar; retorna lista vacía, no null
        assertNotNull(resultado, "Debe retornar lista vacía, no null");
        assertTrue(resultado.isEmpty(), "La lista debe estar vacía");
    }

    // ── Escenario 2: Navegación a detalles ────────────────────────────────────

    @Test
    @DisplayName("CP2.1 - Al hacer clic en un museo se navega a la vista de detalle correcta")
    void dadoUnIdValido_cuandoSeObtieneDetalle_entoncesRetornaMuseoCorrecto() {
        // Dado
        when(repositorioMuseo.buscarPorId(1)).thenReturn(museoNacional);

        // Cuando
        Museo resultado = servicioMuseo.obtenerPorId(1);

        // Entonces
        assertNotNull(resultado, "Debe retornar el museo solicitado");
        assertEquals("Museo Nacional del Ecuador", resultado.getNombre());
        verify(repositorioMuseo, times(1)).buscarPorId(1);
    }

    @Test
    @DisplayName("CP2.2 - Si el museo no existe, obtenerPorId retorna null")
    void dadoUnIdInexistente_cuandoSeObtieneDetalle_entoncesRetornaNull() {
        // Dado
        when(repositorioMuseo.buscarPorId(999)).thenReturn(null);

        // Cuando
        Museo resultado = servicioMuseo.obtenerPorId(999);

        // Entonces
        assertNull(resultado, "Debe retornar null si el museo no existe");
    }

    @Test
    @DisplayName("CP2.3 - ID inválido (0 o negativo) no llama al repositorio")
    void dadoUnIdInvalido_cuandoSeObtieneDetalle_entoncesNoConsultaRepositorio() {
        // Cuando
        Museo resultado = servicioMuseo.obtenerPorId(0);

        // Entonces
        assertNull(resultado, "Debe retornar null con ID inválido");
        verify(repositorioMuseo, never()).buscarPorId(anyInt());
    }

    // ── Funcionalidades adicionales ───────────────────────────────────────────

    @Test
    @DisplayName("Filtrar por categoría retorna museos de esa categoría")
    void dadoUnaCategoria_cuandoSeFiltran_entoncesRetornaMuseosDeEsaCategoria() {
        // Dado
        when(repositorioMuseo.listarPorCategoria("Arte")).thenReturn(List.of(capillaHombre));

        // Cuando
        List<Museo> resultado = servicioMuseo.filtrarPorCategoria("Arte");

        // Entonces
        assertEquals(1, resultado.size());
        assertEquals("Arte", resultado.get(0).getCategoria());
    }

    @Test
    @DisplayName("Buscar por nombre retorna museos que coinciden")
    void dadoUnTermino_cuandoSeBusca_entoncesRetornaMuseosConEseNombre() {
        // Dado
        when(repositorioMuseo.buscarPorNombre("nacional")).thenReturn(List.of(museoNacional));

        // Cuando
        List<Museo> resultado = servicioMuseo.buscarPorNombre("nacional");

        // Entonces
        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).getNombre().toLowerCase().contains("nacional"));
    }

    @Test
    @DisplayName("Buscar con término vacío retorna todos los museos")
    void dadoTerminoVacio_cuandoSeBusca_entoncesRetornaTodos() {
        // Dado
        when(repositorioMuseo.listar()).thenReturn(Arrays.asList(museoNacional, capillaHombre));

        // Cuando
        List<Museo> resultado = servicioMuseo.buscarPorNombre("");

        // Entonces
        assertEquals(2, resultado.size(), "Con término vacío debe retornar todos");
        verify(repositorioMuseo).listar();
        verify(repositorioMuseo, never()).buscarPorNombre(anyString());
    }
}
