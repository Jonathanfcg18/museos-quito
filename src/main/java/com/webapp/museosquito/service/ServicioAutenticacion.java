package com.webapp.museosquito.service;

import com.webapp.museosquito.model.Usuario;
import com.webapp.museosquito.repository.RepositorioUsuario;
import com.webapp.museosquito.util.PasswordUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Lógica de negocio para registro y autenticación de usuarios.
 *
 * Sprint 2:
 *   HU-01 – registrarVisitante (Jonathan Cuasapaz — Backend)
 *   HU-02 – autenticar, ResultadoLogin   (Jhonny Moreira — Backend)
 */
public class ServicioAutenticacion {

    private final RepositorioUsuario repoUsuario;

    public ServicioAutenticacion() {
        this.repoUsuario = new RepositorioUsuario();
    }

    /** Constructor para inyección en tests. */
    public ServicioAutenticacion(RepositorioUsuario repoUsuario) {
        this.repoUsuario = repoUsuario;
    }

    /**
     * Registra un nuevo visitante cumpliendo todos los criterios de HU-01.
     *
     * Escenario 1 → crea cuenta con rol VISITANTE y estado ACTIVO.
     * Escenario 2 → lanza IllegalStateException si el correo ya existe.
     * Escenario 3 → lanza IllegalArgumentException si las contraseñas no coinciden.
     * Escenario 4 → lanza IllegalArgumentException si algún campo está vacío.
     *
     * T-01.3: valida unicidad del correo.
     * T-01.4: hashea la contraseña con BCrypt (PasswordUtil).
     * T-01.5: guarda con rol='visitante', estado='activo'.
     */
    public Usuario registrarVisitante(String nombre, String email,
                                      String password, String confirmar) {
        // Escenario 4: campos obligatorios vacíos
        if (nombre == null || nombre.isBlank())
            throw new IllegalArgumentException("El nombre completo es obligatorio.");
        if (email == null || email.isBlank())
            throw new IllegalArgumentException("El correo electrónico es obligatorio.");
        if (!email.contains("@") || !email.contains("."))
            throw new IllegalArgumentException("Ingresa un correo electrónico válido.");
        if (password == null || password.isBlank())
            throw new IllegalArgumentException("La contraseña es obligatoria.");
        if (confirmar == null || confirmar.isBlank())
            throw new IllegalArgumentException("Debes confirmar tu contraseña.");

        // Escenario 3: contraseñas no coinciden
        if (!password.equals(confirmar))
            throw new IllegalArgumentException("Las contraseñas no coinciden.");

        // Política mínima de contraseña
        if (password.length() < 8)
            throw new IllegalArgumentException(
                    "La contraseña debe tener al menos 8 caracteres.");

        // Escenario 2: correo ya registrado
        if (repoUsuario.existeEmail(email.trim()))
            throw new IllegalStateException("Este correo ya está registrado.");

        // Escenario 1: crear visitante
        Usuario u = new Usuario();
        u.setNombre(nombre.trim());
        u.setEmail(email.trim().toLowerCase());
        u.setPasswordHash(PasswordUtil.hashear(password));   // T-01.4
        u.setRol(Usuario.Rol.VISITANTE);                     // T-01.5
        u.setEstado(Usuario.Estado.ACTIVO);                  // T-01.5
        u.setCreadoEn(LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        return repoUsuario.guardar(u);
    }

    /**
     * Posibles resultados de un intento de login.
     * Permite al controlador distinguir entre los tres escenarios de HU-02
     * sin exponer información sensible en los mensajes de error.
     */
    public enum ResultadoLogin {
        /** Escenario 1: credenciales válidas, cuenta activa. */
        EXITOSO,
        /** Escenario 2: email no existe o contraseña incorrecta. */
        CREDENCIALES_INCORRECTAS,
        /** Escenario 3: credenciales válidas pero cuenta suspendida. */
        CUENTA_SUSPENDIDA
    }

    /**
     * Usuario recuperado tras un login exitoso.
     * Solo válido cuando autenticar() retorna ResultadoLogin.EXITOSO.
     */
    private Usuario ultimoUsuarioAutenticado;

    /**
     * Valida las credenciales y retorna el resultado del intento.
     *
     * T-02.2: valida credenciales con BCrypt (PasswordUtil.verificar).
     *
     * IMPORTANTE: cuando el correo no existe y cuando la contraseña es
     * incorrecta, el resultado es el mismo (CREDENCIALES_INCORRECTAS)
     * para no revelar qué campo es incorrecto — Escenario 2.
     */
    public ResultadoLogin autenticar(String email, String password) {
        ultimoUsuarioAutenticado = null;

        if (email == null || password == null)
            return ResultadoLogin.CREDENCIALES_INCORRECTAS;

        // Buscar por email
        Usuario u = repoUsuario.buscarPorEmail(email.trim());

        // Correo no encontrado → CREDENCIALES_INCORRECTAS (no revelar si existe)
        if (u == null)
            return ResultadoLogin.CREDENCIALES_INCORRECTAS;

        // Contraseña incorrecta → mismo resultado, no revelar cuál falló
        if (!PasswordUtil.verificar(password, u.getPasswordHash()))
            return ResultadoLogin.CREDENCIALES_INCORRECTAS;

        // Escenario 3: cuenta suspendida
        if (Usuario.Estado.SUSPENDIDO.equals(u.getEstado()))
            return ResultadoLogin.CUENTA_SUSPENDIDA;

        // Escenario 1: éxito
        ultimoUsuarioAutenticado = u;
        return ResultadoLogin.EXITOSO;
    }

    /**
     * Retorna el usuario autenticado en el último intento exitoso.
     * Null si el último intento no fue EXITOSO.
     */
    public Usuario getUsuarioAutenticado() {
        return ultimoUsuarioAutenticado;
    }
}