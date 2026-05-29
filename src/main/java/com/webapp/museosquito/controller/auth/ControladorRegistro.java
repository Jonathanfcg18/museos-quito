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
import java.net.URLEncoder;

/**
 * Controlador de registro de nuevos visitantes.
 *
 * Sprint 2 – HU-01: Registrarse en el portal.
 *
 * GET  /registro → redirige a /login?tab=registro (pantalla unificada).
 * POST /registro → procesa el registro, crea sesión y redirige (T-01.3/T-01.5/T-01.6).
 *
 * CAMBIO FRONTEND: el formulario de registro ya no tiene su propia vista JSP;
 * ahora vive dentro de login.jsp como segundo tab. Los errores se pasan por
 * query params para que el tab de registro quede activo.
 */
@WebServlet(name = "ControladorRegistro", urlPatterns = "/registro")
public class ControladorRegistro extends ControladorBase {

    private ServicioAutenticacion servicioAuth;

    @Override
    public void init() {
        servicioAuth = new ServicioAutenticacion();
    }

    /**
     * GET /registro → redirige a la pantalla de autenticación con tab=registro.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        setEncoding(req, res);

        // Si ya tiene sesión activa, redirigir al catálogo
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("usuarioSesion") != null) {
            redirigir("/museos", req, res);
            return;
        }

        redirigir("/login?tab=registro", req, res);
    }

    /**
     * POST /registro → valida, crea la cuenta y genera sesión automática.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        setEncoding(req, res);

        String nombre    = req.getParameter("nombre");
        String email     = req.getParameter("email");
        String password  = req.getParameter("password");
        String confirmar = req.getParameter("confirmar");

        // Campos vacíos detectados antes de llamar al servicio
        if (estaVacio(nombre) || estaVacio(email) ||
            estaVacio(password) || estaVacio(confirmar)) {
            redirigirConError("Todos los campos son obligatorios.", nombre, email, res, req);
            return;
        }

        try {
            Usuario nuevo = servicioAuth.registrarVisitante(
                    nombre, email, password, confirmar);

            // Inicio de sesión automático tras registro exitoso
            HttpSession session = req.getSession(true);
            session.setAttribute("usuarioSesion", nuevo);
            session.setMaxInactiveInterval(60 * 30);

            redirigir("/museos", req, res);

        } catch (IllegalStateException | IllegalArgumentException e) {
            redirigirConError(e.getMessage(), nombre, email, res, req);
        }
    }

    /**
     * Redirige a /login?tab=registro con el mensaje de error y los datos
     * del formulario para que el usuario no tenga que reescribirlos.
     */
    private void redirigirConError(String msg, String nombre, String email,
            HttpServletResponse res, HttpServletRequest req) throws IOException {
        StringBuilder url = new StringBuilder(req.getContextPath());
        url.append("/login?tab=registro");
        url.append("&error=").append(URLEncoder.encode(msg != null ? msg : "", "UTF-8"));
        if (nombre != null) url.append("&nombre=").append(URLEncoder.encode(nombre, "UTF-8"));
        if (email  != null) url.append("&regEmail=").append(URLEncoder.encode(email, "UTF-8"));
        res.sendRedirect(url.toString());
    }

    private boolean estaVacio(String valor) {
        return valor == null || valor.isBlank();
    }
}