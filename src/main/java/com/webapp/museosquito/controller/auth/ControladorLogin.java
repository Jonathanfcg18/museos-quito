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
 * CAMBIOS v2.0 (auth unificado):
 * - GET /login → muestra auth.jsp (login + registro en tabs)
 * - POST /login → en error usa atributo "errorLogin" (no "error")
 * y también pasa "emailLogin" para repoblar el campo
 * - La vista destino cambió de "auth/login.jsp" a "auth/auth.jsp"
 */
@WebServlet(name = "ControladorLogin", urlPatterns = "/login")
public class ControladorLogin extends ControladorBase {

    private ServicioAutenticacion servicioAuth;

    @Override
    public void init() {
        servicioAuth = new ServicioAutenticacion();
    }

    /**
     * GET /login → muestra el formulario unificado auth.jsp.
     * Acepta ?tab=registro para que el JS abra directamente el panel de registro.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        setEncoding(req, res);

        HttpSession session = req.getSession(false);
        Usuario usuario = (session != null)
                ? (Usuario) session.getAttribute("usuarioSesion")
                : null;

        if (usuario != null) {
            redirigirPorRol(usuario, req, res);
            return;
        }

        irAVista("auth/auth.jsp", req, res);
    }

    /**
     * POST /login → valida credenciales y crea la sesión HTTP.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        setEncoding(req, res);

        String email = req.getParameter("email");
        String password = req.getParameter("password");

        // Validación básica de campos vacíos
        if (email == null || email.isBlank() ||
                password == null || password.isBlank()) {
            // ── CAMBIO: atributo "errorLogin" + "emailLogin" ──
            req.setAttribute("errorLogin",
                    "El correo y la contraseña son obligatorios.");
            req.setAttribute("emailLogin", email);
            irAVista("auth/auth.jsp", req, res);
            return;
        }

        ServicioAutenticacion.ResultadoLogin resultado = servicioAuth.autenticar(email, password);

        switch (resultado) {

            case EXITOSO -> {
                Usuario usuario = servicioAuth.getUsuarioAutenticado();
                HttpSession session = req.getSession(true);
                session.setAttribute("usuarioSesion", usuario);
                session.setMaxInactiveInterval(60 * 30);

                // Redirigir a la URL original si venía de una página protegida
                String urlAnterior = (String) session.getAttribute("urlAnteriorLogin");
                session.removeAttribute("urlAnteriorLogin");

                if (urlAnterior != null && !urlAnterior.isBlank()
                        && !urlAnterior.contains("/login")
                        && !urlAnterior.contains("/registro")) {
                    res.sendRedirect(urlAnterior);
                } else {
                    redirigirPorRol(usuario, req, res);
                }
            }

            case CUENTA_SUSPENDIDA -> {
                // ── CAMBIO: atributo "errorLogin" + "emailLogin" ──
                req.setAttribute("errorLogin",
                        "Tu cuenta ha sido suspendida. Contacta al administrador.");
                req.setAttribute("emailLogin", email);
                irAVista("auth/auth.jsp", req, res);
            }

            default -> {
                // CREDENCIALES_INCORRECTAS
                // ── CAMBIO: atributo "errorLogin" + "emailLogin" ──
                req.setAttribute("errorLogin",
                        "Correo o contraseña incorrectos.");
                req.setAttribute("emailLogin", email);
                irAVista("auth/auth.jsp", req, res);
            }
        }
    }

    /**
     * Redirige al usuario a la sección correspondiente según su rol.
     * Sin cambios respecto al original.
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