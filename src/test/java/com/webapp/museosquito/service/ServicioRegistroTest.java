package com.webapp.museosquito.service;

import com.webapp.museosquito.model.Usuario;
import com.webapp.museosquito.repository.RepositorioUsuario;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para HU-01: Registrarse en el portal.
 * Cubre T-01.7 y T-01.8.
 *
 * Responsable Testing: Jonathan Cuasapaz
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("HU-01 – Registrarse en el portal")
class ServicioRegistroTest {

    @Mock
    private RepositorioUsuario repoUsuario;

    private ServicioAutenticacion servicio;

    @BeforeEach
    void setUp() {
        servicio = new ServicioAutenticacion(repoUsuario);
    }

    // ── T-01.7: Verificar registro exitoso ────────────────────────────────

    @Test
    @DisplayName("T-01.7 – Escenario 1: Registro exitoso crea cuenta con rol VISITANTE y estado ACTIVO")
    void dadoDatosValidos_cuandoSeRegistra_entoncesCreaCuentaVisitante() {
        // Dado: correo no existe aún
        when(repoUsuario.existeEmail("ana@test.ec")).thenReturn(false);
        when(repoUsuario.guardar(any())).thenAnswer(inv -> {
            Usuario u = inv.getArgument(0);
            u.setId(1);
            return u;
        });

        // Cuando: se registra con datos válidos
        Usuario resultado = servicio.registrarVisitante(
                "Ana López", "ana@test.ec", "Clave12345", "Clave12345");

        // Entonces: cuenta creada con rol VISITANTE y estado ACTIVO
        assertNotNull(resultado, "El usuario no debe ser null");
        assertEquals("Ana López", resultado.getNombre());
        assertEquals("ana@test.ec", resultado.getEmail());
        assertEquals(Usuario.Rol.VISITANTE, resultado.getRol(),
                "El rol debe ser VISITANTE");
        assertEquals(Usuario.Estado.ACTIVO, resultado.getEstado(),
                "El estado debe ser ACTIVO");
        assertNotNull(resultado.getPasswordHash(),
                "El hash de contraseña debe estar guardado");
        assertNotEquals("Clave12345", resultado.getPasswordHash(),
                "La contraseña no debe guardarse en texto plano");
        verify(repoUsuario).guardar(any());
    }

    @Test
    @DisplayName("T-01.7 – El email se guarda en minúsculas y sin espacios")
    void dadoEmailConMayusculas_cuandoSeRegistra_entoncesSeGuardaEnMinusculas() {
        when(repoUsuario.existeEmail(anyString())).thenReturn(false);
        when(repoUsuario.guardar(any())).thenAnswer(inv -> inv.getArgument(0));

        Usuario resultado = servicio.registrarVisitante(
                "Luis Torres", "  LUIS@TEST.EC  ", "Clave12345", "Clave12345");

        assertEquals("luis@test.ec", resultado.getEmail());
    }

    // ── T-01.8: Verificar bloqueo por correo duplicado ────────────────────

    @Test
    @DisplayName("T-01.8 – Escenario 2: Correo ya registrado bloquea el registro")
    void dadoCorreoYaRegistrado_cuandoSeRegistra_entoncesLanzaExcepcion() {
        // Dado: el correo ya existe
        when(repoUsuario.existeEmail("ana@test.ec")).thenReturn(true);

        // Cuando / Entonces: se lanza excepción con el mensaje correcto
        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> servicio.registrarVisitante(
                        "Ana López", "ana@test.ec", "Clave12345", "Clave12345"));

        assertTrue(ex.getMessage().contains("ya está registrado"),
                "El mensaje debe indicar correo duplicado");
        verify(repoUsuario, never()).guardar(any());
    }

    // ── T-01.8: Verificar bloqueo por contraseñas distintas ───────────────

    @Test
    @DisplayName("T-01.8 – Escenario 3: Contraseñas distintas bloquean el registro")
    void dadoContrasenasDistintas_cuandoSeRegistra_entoncesLanzaExcepcion() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> servicio.registrarVisitante(
                        "Ana López", "ana@test.ec", "Clave12345", "OtraClaveXX"));

        assertTrue(ex.getMessage().contains("no coinciden"),
                "El mensaje debe indicar que las contraseñas no coinciden");
        // No debe consultar si existe el email (validación previa)
        verify(repoUsuario, never()).existeEmail(any());
        verify(repoUsuario, never()).guardar(any());
    }

    // ── T-01.8: Verificar bloqueo por campos vacíos ───────────────────────

    @Test
    @DisplayName("T-01.8 – Escenario 4: Nombre vacío bloquea el registro")
    void dadoNombreVacio_cuandoSeRegistra_entoncesLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> servicio.registrarVisitante(
                        "   ", "ana@test.ec", "Clave12345", "Clave12345"));
        verify(repoUsuario, never()).guardar(any());
    }

    @Test
    @DisplayName("T-01.8 – Escenario 4: Correo vacío bloquea el registro")
    void dadoEmailVacio_cuandoSeRegistra_entoncesLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> servicio.registrarVisitante(
                        "Ana López", "", "Clave12345", "Clave12345"));
        verify(repoUsuario, never()).guardar(any());
    }

    @Test
    @DisplayName("T-01.8 – Escenario 4: Contraseña vacía bloquea el registro")
    void dadoPasswordVacio_cuandoSeRegistra_entoncesLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> servicio.registrarVisitante(
                        "Ana López", "ana@test.ec", "", ""));
        verify(repoUsuario, never()).guardar(any());
    }

    @Test
    @DisplayName("T-01.8 – Contraseña menor a 8 caracteres bloquea el registro")
    void dadoPasswordCorto_cuandoSeRegistra_entoncesLanzaExcepcion() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> servicio.registrarVisitante(
                        "Ana López", "ana@test.ec", "abc", "abc"));

        assertTrue(ex.getMessage().contains("8 caracteres"));
        verify(repoUsuario, never()).guardar(any());
    }

    @Test
    @DisplayName("T-01.8 – Correo sin formato válido bloquea el registro")
    void dadoEmailSinFormato_cuandoSeRegistra_entoncesLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> servicio.registrarVisitante(
                        "Ana López", "noesuncorreo", "Clave12345", "Clave12345"));
        verify(repoUsuario, never()).guardar(any());
    }
}