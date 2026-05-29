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
 * CAMBIOS v2.0 (auth unificado):
 *   - GET /registro  → redirige a /login?tab=registro (muestra auth.jsp
 *                       con el tab de registro activo vía JS)
 *   - POST /registro → en error usa atributo "errorRegistro" (no "error")
 *                      y pasa "nombreReg" + "emailReg" para repoblar campos
 *   - La vista destino en errores cambió de "auth/registro.jsp"
 *     a "auth/auth.jsp"
 */
@WebServlet(name = "ControladorRegistro", urlPatterns = "/registro")
public class ControladorRegistro extends ControladorBase {

    private ServicioAutenticacion servicioAuth;

    @Override
    public void init() {
        servicioAuth = new ServicioAutenticacion();
    }

    /**
     * GET /registro → redirige a /login con el parámetro tab=registro.
     * El JS en auth.jsp lee el parámetro y activa el tab de registro.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        setEncoding(req, res);

        // Si ya tiene sesión activa, ir al catálogo
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("usuarioSesion") != null) {
            redirigir("/museos", req, res);
            return;
        }

        // ── CAMBIO: en vez de mostrar registro.jsp, redirige al auth unificado ──
        // El parámetro ?tab=registro activa el panel correcto en el JS del auth.jsp
        res.sendRedirect(req.getContextPath() + "/login?tab=registro");
    }

    /**
     * POST /registro → valida, crea la cuenta y genera sesión automática.
     *
     * T-01.3: valida unicidad del correo.
     * T-01.5: crea con rol VISITANTE, estado ACTIVO.
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

            // ── CAMBIO: atributos con sufijo "Reg" + vista auth.jsp ──
            req.setAttribute("errorRegistro",
                    "Todos los campos son obligatorios.");
            req.setAttribute("nombreReg", nombre);
            req.setAttribute("emailReg",  email);
            irAVista("auth/auth.jsp", req, res);
            return;
        }

        try {
            // Delegar toda la lógica al servicio (sin cambios)
            Usuario nuevo = servicioAuth.registrarVisitante(
                    nombre, email, password, confirmar);

            // T-01.6: sesión automática tras registro exitoso
            HttpSession session = req.getSession(true);
            session.setAttribute("usuarioSesion", nuevo);
            session.setMaxInactiveInterval(60 * 30); // 30 minutos

            // Escenario 1: redirigir al catálogo
            redirigir("/museos", req, res);

        } catch (IllegalStateException e) {
            // Escenario 2: correo ya registrado
            // ── CAMBIO: atributos con sufijo "Reg" + vista auth.jsp ──
            req.setAttribute("errorRegistro", e.getMessage());
            req.setAttribute("nombreReg", nombre);
            req.setAttribute("emailReg",  email);
            irAVista("auth/auth.jsp", req, res);

        } catch (IllegalArgumentException e) {
            // Escenario 3: contraseñas no coinciden / campo inválido
            // ── CAMBIO: atributos con sufijo "Reg" + vista auth.jsp ──
            req.setAttribute("errorRegistro", e.getMessage());
            req.setAttribute("nombreReg", nombre);
            req.setAttribute("emailReg",  email);
            irAVista("auth/auth.jsp", req, res);
        }
    }

    private boolean estaVacio(String valor) {
        return valor == null || valor.isBlank();
    }
}