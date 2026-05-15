package com.webapp.museosquito.controller.admin;

import com.webapp.museosquito.controller.ControladorBase;
import com.webapp.museosquito.model.AdminMuseo;
import com.webapp.museosquito.service.ServicioAdmin;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Controlador de login para administradores de museo.
 * HU4: acceso al panel de gestión.
 */
@WebServlet(name = "ControladorLoginAdmin", urlPatterns = "/admin/login")
public class ControladorLoginAdmin extends ControladorBase {

    private ServicioAdmin servicioAdmin;

    @Override
    public void init() {
        servicioAdmin = new ServicioAdmin();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        setEncoding(req, res);

        // Si ya tiene sesión activa, ir directo al panel
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("adminMuseo") != null) {
            redirigir("/admin/horarios", req, res);
            return;
        }
        irAVista("admin/login.jsp", req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        setEncoding(req, res);

        String email    = req.getParameter("email");
        String password = req.getParameter("password");

        AdminMuseo admin = servicioAdmin.autenticar(email, password);

        if (admin == null) {
            req.setAttribute("error", "Correo o contraseña incorrectos.");
            irAVista("admin/login.jsp", req, res);
            return;
        }

        // Crear sesión
        HttpSession session = req.getSession(true);
        session.setAttribute("adminMuseo", admin);
        session.setMaxInactiveInterval(60 * 30); // 30 minutos

        redirigir("/admin/horarios", req, res);
    }
}