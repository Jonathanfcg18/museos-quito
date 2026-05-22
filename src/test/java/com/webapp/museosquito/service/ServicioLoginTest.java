package com.webapp.museosquito.service;

import com.webapp.museosquito.model.Usuario;
import com.webapp.museosquito.repository.RepositorioUsuario;
import com.webapp.museosquito.util.PasswordUtil;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para HU-02: Iniciar y cerrar sesión.
 * Cubre T-02.8.
 *
 * Responsable Testing: Jhonny Moreira
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("HU-02 – Iniciar y cerrar sesión")
class ServicioLoginTest {

    @Mock
    private RepositorioUsuario repoUsuario;

    private ServicioAutenticacion servicio;

    @BeforeEach
    void setUp() {
        servicio = new ServicioAutenticacion(repoUsuario);
    }

    // ── T-02.8: Login exitoso por cada rol ────────────────────────────────

    @Test
    @DisplayName("T-02.8 – Escenario 1: Login exitoso como VISITANTE retorna EXITOSO")
    void dadoVisitanteActivo_cuandoLoginCorrecto_entoncesRetornaExitoso() {
        Usuario u = crearUsuario("ana@test.ec", "Clave12345", Usuario.Rol.VISITANTE,
                Usuario.Estado.ACTIVO);
        when(repoUsuario.buscarPorEmail("ana@test.ec")).thenReturn(u);

        ServicioAutenticacion.ResultadoLogin res =
                servicio.autenticar("ana@test.ec", "Clave12345");

        assertEquals(ServicioAutenticacion.ResultadoLogin.EXITOSO, res);
        assertNotNull(servicio.getUsuarioAutenticado());
        assertTrue(servicio.getUsuarioAutenticado().isVisitante());
    }

    @Test
    @DisplayName("T-02.8 – Escenario 1: Login exitoso como ADMIN_MUSEO retorna EXITOSO")
    void dadoAdminMuseoActivo_cuandoLoginCorrecto_entoncesRetornaExitoso() {
        Usuario u = crearUsuario("admin@test.ec", "Admin2026!", Usuario.Rol.ADMIN_MUSEO,
                Usuario.Estado.ACTIVO);
        when(repoUsuario.buscarPorEmail("admin@test.ec")).thenReturn(u);

        ServicioAutenticacion.ResultadoLogin res =
                servicio.autenticar("admin@test.ec", "Admin2026!");

        assertEquals(ServicioAutenticacion.ResultadoLogin.EXITOSO, res);
        assertTrue(servicio.getUsuarioAutenticado().isAdminMuseo());
    }

    // ── T-02.8: Mensajes de error — credenciales incorrectas ──────────────

    @Test
    @DisplayName("T-02.8 – Escenario 2: Contraseña incorrecta retorna CREDENCIALES_INCORRECTAS")
    void dadoPasswordIncorrecto_cuandoLogin_entoncesRetornaCredencialesIncorrectas() {
        Usuario u = crearUsuario("ana@test.ec", "Clave12345", Usuario.Rol.VISITANTE,
                Usuario.Estado.ACTIVO);
        when(repoUsuario.buscarPorEmail("ana@test.ec")).thenReturn(u);

        ServicioAutenticacion.ResultadoLogin res =
                servicio.autenticar("ana@test.ec", "WrongPass!");

        assertEquals(ServicioAutenticacion.ResultadoLogin.CREDENCIALES_INCORRECTAS, res);
        assertNull(servicio.getUsuarioAutenticado(),
                "No debe haber usuario autenticado con contraseña incorrecta");
    }

    @Test
    @DisplayName("T-02.8 – Escenario 2: Correo inexistente retorna CREDENCIALES_INCORRECTAS (no revela si existe)")
    void dadoCorreoNoRegistrado_cuandoLogin_entoncesRetornaMismoErrorQuePasswordIncorrecto() {
        when(repoUsuario.buscarPorEmail("noexiste@test.ec")).thenReturn(null);

        ServicioAutenticacion.ResultadoLogin res =
                servicio.autenticar("noexiste@test.ec", "cualquier");

        // Escenario 2: mismo mensaje que contraseña incorrecta — no se revela si existe
        assertEquals(ServicioAutenticacion.ResultadoLogin.CREDENCIALES_INCORRECTAS, res);
        assertNull(servicio.getUsuarioAutenticado());
    }

    // ── T-02.8: Cuenta suspendida ─────────────────────────────────────────

    @Test
    @DisplayName("T-02.8 – Escenario 3: Cuenta suspendida con credenciales correctas retorna CUENTA_SUSPENDIDA")
    void dadoCuentaSuspendida_cuandoLoginCorrecto_entoncesRetornaSuspendida() {
        Usuario u = crearUsuario("ana@test.ec", "Clave12345", Usuario.Rol.VISITANTE,
                Usuario.Estado.SUSPENDIDO);
        when(repoUsuario.buscarPorEmail("ana@test.ec")).thenReturn(u);

        ServicioAutenticacion.ResultadoLogin res =
                servicio.autenticar("ana@test.ec", "Clave12345");

        assertEquals(ServicioAutenticacion.ResultadoLogin.CUENTA_SUSPENDIDA, res,
                "Una cuenta suspendida debe retornar CUENTA_SUSPENDIDA");
        assertNull(servicio.getUsuarioAutenticado(),
                "No debe haber usuario autenticado con cuenta suspendida");
    }

    // ── T-02.8: Cierre de sesión ───────────────────────────────────────────

    @Test
    @DisplayName("T-02.8 – Escenario 4: Campos null retornan CREDENCIALES_INCORRECTAS sin consultar BD")
    void dadoCamposNulos_cuandoLogin_entoncesRetornaIncorrectoSinConsultarBD() {
        ServicioAutenticacion.ResultadoLogin res =
                servicio.autenticar(null, null);

        assertEquals(ServicioAutenticacion.ResultadoLogin.CREDENCIALES_INCORRECTAS, res);
        verify(repoUsuario, never()).buscarPorEmail(any());
    }

    @Test
    @DisplayName("T-02.8 – Después de login exitoso, getUsuarioAutenticado retorna el usuario correcto")
    void dadoLoginExitoso_cuandoSeObtieneusuario_entoncesEsElMismo() {
        Usuario u = crearUsuario("luis@test.ec", "Pass12345!", Usuario.Rol.VISITANTE,
                Usuario.Estado.ACTIVO);
        u.setNombre("Luis Torres");
        when(repoUsuario.buscarPorEmail("luis@test.ec")).thenReturn(u);

        servicio.autenticar("luis@test.ec", "Pass12345!");

        assertEquals("Luis Torres", servicio.getUsuarioAutenticado().getNombre());
        assertEquals("luis@test.ec", servicio.getUsuarioAutenticado().getEmail());
    }

    @Test
    @DisplayName("T-02.8 – Después de login fallido, getUsuarioAutenticado retorna null")
    void dadoLoginFallido_cuandoSeObtieneUsuario_entoncesEsNull() {
        when(repoUsuario.buscarPorEmail("ana@test.ec")).thenReturn(null);

        servicio.autenticar("ana@test.ec", "cualquier");

        assertNull(servicio.getUsuarioAutenticado());
    }

    // ── Helper ────────────────────────────────────────────────────────────

    private Usuario crearUsuario(String email, String password,
                                  Usuario.Rol rol, Usuario.Estado estado) {
        Usuario u = new Usuario();
        u.setId(1);
        u.setNombre("Usuario Test");
        u.setEmail(email);
        u.setPasswordHash(PasswordUtil.hashear(password));
        u.setRol(rol);
        u.setEstado(estado);
        u.setCreadoEn("2026-05-21 10:00:00");
        return u;
    }
}