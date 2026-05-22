package com.webapp.museosquito.controller.auth;

import com.webapp.museosquito.controller.ControladorBase;
import com.webapp.museosquito.model.Usuario;
import com.webapp.museosquito.service.ServicioAutenticacion;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Controlador de inicio de sesión.
 *
 * Sprint 2 – HU-02: Iniciar y cerrar sesión.
 * Responsable Backend: Jhonny Moreira
 *
 * GET /login → muestra el formulario (T-02.1).
 * POST /login → valida credenciales y redirige por rol (T-02.2 / T-02.3 /
 * T-02.4).
 *
 * Redirección por rol — Escenario 1:
 * VISITANTE → /museos
 * ADMIN_MUSEO → /admin/horarios (panel existente del Sprint 1)
 * ADMIN_SISTEMA → /museos (panel futuro, por ahora al catálogo)
 */
@WebServlet(name = "ControladorLogin", urlPatterns = "/login")
public class ControladorLogin extends ControladorBase {

    private ServicioAutenticacion servicioAuth;

    @Override
    public void init() {
        servicioAuth = new ServicioAutenticacion();
    }

    /** GET /login → muestra el formulario de login. */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        setEncoding(req, res);

        // Si ya tiene sesión activa, redirigir según su rol
        HttpSession session = req.getSession(false);
        Usuario usuario = (session != null)
                ? (Usuario) session.getAttribute("usuarioSesion")
                : null;

        if (usuario != null) {
            redirigirPorRol(usuario, req, res);
            return;
        }

        irAVista("auth/login.jsp", req, res);
    }

    /**
     * POST /login → valida credenciales y crea la sesión HTTP.
     *
     * T-02.2: valida con BCrypt mediante ServicioAutenticacion.autenticar().
     * T-02.3: crea sesión HTTP segura con HttpSession.
     * T-02.4: redirige según el rol del usuario autenticado.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        setEncoding(req, res);

        String email = req.getParameter("email");
        String password = req.getParameter("password");

        // Validación básica de campos
        if (email == null || email.isBlank() ||
                password == null || password.isBlank()) {
            req.setAttribute("error", "El correo y la contraseña son obligatorios.");
            irAVista("auth/login.jsp", req, res);
            return;
        }

        ServicioAutenticacion.ResultadoLogin resultado = servicioAuth.autenticar(email, password);

        switch (resultado) {

            case EXITOSO -> {
                // T-02.3: crear sesión HTTP segura
                Usuario usuario = servicioAuth.getUsuarioAutenticado();
                HttpSession session = req.getSession(true);
                session.setAttribute("usuarioSesion", usuario);
                session.setMaxInactiveInterval(60 * 30); // 30 minutos

                // T-02.4: redirigir según rol — Escenario 1
                redirigirPorRol(usuario, req, res);
            }

            case CUENTA_SUSPENDIDA -> {
                // Escenario 3
                req.setAttribute("error",
                        "Tu cuenta ha sido suspendida. Contacta al administrador.");
                req.setAttribute("email", email);
                irAVista("auth/login.jsp", req, res);
            }

            default -> {
                // Escenario 2: CREDENCIALES_INCORRECTAS
                // Mismo mensaje para correo inexistente y contraseña incorrecta
                req.setAttribute("error", "Correo o contraseña incorrectos.");
                req.setAttribute("email", email);
                irAVista("auth/login.jsp", req, res);
            }
        }
    }

    /**
     * T-02.4: Redirige al usuario a la sección correspondiente según su rol.
     */
    private void redirigirPorRol(Usuario u, HttpServletRequest req,
            HttpServletResponse res) throws IOException {
        String destino = switch (u.getRol()) {
            case VISITANTE -> "/museos";
            case ADMIN_MUSEO -> "/admin-museo/dashboard";
            case ADMIN_SISTEMA -> "/admin-sistema/dashboard";
        };
        redirigir(destino, req, res);
    }
}