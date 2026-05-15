package com.webapp.museosquito.controller.admin;

import com.webapp.museosquito.controller.ControladorBase;
import com.webapp.museosquito.model.AdminMuseo;
import com.webapp.museosquito.model.FranjaReserva;
import com.webapp.museosquito.service.ServicioAdmin;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * Controlador del panel de gestión de horarios y aforo.
 * HU4: POST /horarios, PUT /horarios/{id}
 */
@WebServlet(name = "ControladorGestionHorarios", urlPatterns = "/admin/horarios")
public class ControladorGestionHorarios extends ControladorBase {

    private ServicioAdmin servicioAdmin;

    @Override
    public void init() {
        servicioAdmin = new ServicioAdmin();
    }

    private AdminMuseo obtenerAdmin(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("adminMuseo") == null) {
            redirigir("/admin/login", req, res);
            return null;
        }
        return (AdminMuseo) session.getAttribute("adminMuseo");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        setEncoding(req, res);
        AdminMuseo admin = obtenerAdmin(req, res);
        if (admin == null) return;

        String mensaje = req.getParameter("mensaje");
        String error   = req.getParameter("error");

        List<FranjaReserva> franjas = servicioAdmin
                .obtenerFranjasPorMuseo(admin.getMuseo().getId());

        req.setAttribute("admin",   admin);
        req.setAttribute("franjas", franjas);
        if (mensaje != null)
            req.setAttribute("mensaje", java.net.URLDecoder.decode(mensaje, "UTF-8"));
        if (error != null)
            req.setAttribute("error", java.net.URLDecoder.decode(error, "UTF-8"));

        irAVista("admin/gestionHorarios.jsp", req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        setEncoding(req, res);
        AdminMuseo admin = obtenerAdmin(req, res);
        if (admin == null) return;

        String accion       = req.getParameter("accion");
        String fecha        = req.getParameter("fecha");
        String horaInicio   = req.getParameter("horaInicio");
        String horaFin      = req.getParameter("horaFin");
        String aforoParam   = req.getParameter("aforoMaximo");
        String franjaIdParam = req.getParameter("franjaId");

        int aforoMaximo = 0;
        try {
            if (aforoParam != null && !aforoParam.isBlank())
                aforoMaximo = Integer.parseInt(aforoParam.trim());
        } catch (NumberFormatException e) {
            aforoMaximo = 0;
        }

        try {
            if ("crear".equals(accion)) {
                // POST /horarios — HU4 Escenario 1
                servicioAdmin.crearFranja(
                        admin.getMuseo().getId(),
                        fecha, horaInicio, horaFin, aforoMaximo);
                String msg = URLEncoder.encode("Franja horaria creada exitosamente.", "UTF-8");
                res.sendRedirect(req.getContextPath() + "/admin/horarios?mensaje=" + msg);

            } else if ("modificar".equals(accion)) {
                // PUT /horarios/{id} — HU4 Escenario 1 y 2
                int franjaId = Integer.parseInt(franjaIdParam.trim());
                servicioAdmin.modificarFranja(
                        franjaId, fecha, horaInicio, horaFin, aforoMaximo);
                String msg = URLEncoder.encode("Franja horaria modificada exitosamente.", "UTF-8");
                res.sendRedirect(req.getContextPath() + "/admin/horarios?mensaje=" + msg);

            } else if ("eliminar".equals(accion)) {
                int franjaId = Integer.parseInt(franjaIdParam.trim());
                servicioAdmin.eliminarFranja(franjaId);
                String msg = URLEncoder.encode("Franja horaria eliminada.", "UTF-8");
                res.sendRedirect(req.getContextPath() + "/admin/horarios?mensaje=" + msg);

            } else {
                redirigir("/admin/horarios", req, res);
            }

        } catch (IllegalArgumentException | IllegalStateException e) {
            String err = URLEncoder.encode(e.getMessage(), "UTF-8");
            res.sendRedirect(req.getContextPath() + "/admin/horarios?error=" + err);
        }
    }
}