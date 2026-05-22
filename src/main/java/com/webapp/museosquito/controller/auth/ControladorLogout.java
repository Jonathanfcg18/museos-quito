package com.webapp.museosquito.controller.auth;

import com.webapp.museosquito.controller.ControladorBase;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Controlador de cierre de sesión.
 *
 * Sprint 2 – HU-02 Escenario 4: Cierre de sesión exitoso.
 * Responsable Backend: Jhonny Moreira
 *
 * T-02.7: GET /logout invalida la sesión activa y redirige al inicio.
 */
@WebServlet(name = "ControladorLogout", urlPatterns = "/logout")
public class ControladorLogout extends ControladorBase {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        // Escenario 4: invalidar la sesión HTTP (el token deja de ser válido)
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // Redirigir a la página principal — el usuario ya no accede a rutas protegidas
        redirigir("/", req, res);
    }
}