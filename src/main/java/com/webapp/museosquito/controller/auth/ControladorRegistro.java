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
 * Controlador de registro de nuevos visitantes.
 *
 * Sprint 2 – HU-01: Registrarse en el portal.
 * Responsable Backend: Jonathan Cuasapaz
 *
 * GET  /registro → muestra el formulario (T-01.1).
 * POST /registro → procesa el registro, crea sesión y redirige (T-01.3/T-01.5/T-01.6).
 */
@WebServlet(name = "ControladorRegistro", urlPatterns = "/registro")
public class ControladorRegistro extends ControladorBase {

    private ServicioAutenticacion servicioAuth;

    @Override
    public void init() {
        servicioAuth = new ServicioAutenticacion();
    }

    /** GET /registro → muestra el formulario de registro. */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        setEncoding(req, res);

        // Si ya tiene sesión activa, redirigir al catálogo (evitar doble registro)
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("usuarioSesion") != null) {
            redirigir("/museos", req, res);
            return;
        }

        irAVista("auth/registro.jsp", req, res);
    }

    /**
     * POST /registro → valida, crea la cuenta y genera sesión automática.
     *
     * T-01.3: valida unicidad del correo (delegado al servicio).
     * T-01.5: crea registro con rol='visitante', estado='activo'.
     * T-01.6: inicia sesión automáticamente y redirige al catálogo.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        setEncoding(req, res);

        String nombre    = req.getParameter("nombre");
        String email     = req.getParameter("email");
        String password  = req.getParameter("password");
        String confirmar = req.getParameter("confirmar");

        // Escenario 4: campos vacíos detectados antes de llamar al servicio
        if (estaVacio(nombre) || estaVacio(email) ||
            estaVacio(password) || estaVacio(confirmar)) {
            req.setAttribute("error", "Todos los campos son obligatorios.");
            req.setAttribute("nombre", nombre);
            req.setAttribute("email", email);
            irAVista("auth/registro.jsp", req, res);
            return;
        }

        try {
            // Delegar toda la lógica de negocio al servicio
            Usuario nuevo = servicioAuth.registrarVisitante(
                    nombre, email, password, confirmar);

            // T-01.6: inicio de sesión automático tras registro exitoso
            HttpSession session = req.getSession(true);
            session.setAttribute("usuarioSesion", nuevo);
            session.setMaxInactiveInterval(60 * 30); // 30 minutos

            // Escenario 1: redirigir al catálogo de museos
            redirigir("/museos", req, res);

        } catch (IllegalStateException e) {
            // Escenario 2: correo ya registrado
            req.setAttribute("error", e.getMessage());
            req.setAttribute("nombre", nombre);
            req.setAttribute("email", email);
            irAVista("auth/registro.jsp", req, res);

        } catch (IllegalArgumentException e) {
            // Escenario 3: contraseñas no coinciden / Escenario 4: campo inválido
            req.setAttribute("error", e.getMessage());
            req.setAttribute("nombre", nombre);
            req.setAttribute("email", email);
            irAVista("auth/registro.jsp", req, res);
        }
    }

    private boolean estaVacio(String valor) {
        return valor == null || valor.isBlank();
    }
}