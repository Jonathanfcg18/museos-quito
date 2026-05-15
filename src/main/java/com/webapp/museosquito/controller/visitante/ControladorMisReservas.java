package com.webapp.museosquito.controller.visitante;

import com.webapp.museosquito.controller.ControladorBase;
import com.webapp.museosquito.model.FranjaReserva;
import com.webapp.museosquito.model.Reserva;
import com.webapp.museosquito.service.ServicioReserva;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Controlador del panel de reservas del visitante.
 * HU3: GET /reservas/usuario — lista reservas activas.
 */
@WebServlet(name = "ControladorMisReservas", urlPatterns = "/reservas/mis-reservas")
public class ControladorMisReservas extends ControladorBase {

    private ServicioReserva servicioReserva;

    @Override
    public void init() {
        servicioReserva = new ServicioReserva();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        setEncoding(req, res);

        String email   = req.getParameter("email");
        String mensaje = req.getParameter("mensaje");
        String error   = req.getParameter("error");

        if (email != null && !email.isBlank()) {
            List<Reserva> reservas = servicioReserva.obtenerReservasPorEmail(email);
            req.setAttribute("reservas", reservas);
            req.setAttribute("email", email);
            req.setAttribute("totalReservas", reservas.size());
        }

        if (mensaje != null) req.setAttribute("mensaje", java.net.URLDecoder.decode(mensaje, "UTF-8"));
        if (error   != null) req.setAttribute("error",   java.net.URLDecoder.decode(error,   "UTF-8"));

        irAVista("visitante/misReservas.jsp", req, res);
    }
}