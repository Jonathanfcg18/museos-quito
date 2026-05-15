package com.webapp.museosquito.controller.visitante;

import com.webapp.museosquito.controller.ControladorBase;
import com.webapp.museosquito.service.ServicioReserva;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;

/**
 * Controlador para cancelar una reserva.
 * HU3 Escenario 1: DELETE /reservas/{id}
 */
@WebServlet(name = "ControladorCancelarReserva", urlPatterns = "/reservas/cancelar")
public class ControladorCancelarReserva extends ControladorBase {

    private ServicioReserva servicioReserva;

    @Override
    public void init() {
        servicioReserva = new ServicioReserva();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        setEncoding(req, res);

        String reservaIdParam = req.getParameter("reservaId");
        String email          = req.getParameter("email");

        if (reservaIdParam == null || email == null || email.isBlank()) {
            redirigir("/reservas/mis-reservas", req, res);
            return;
        }

        int reservaId;
        try {
            reservaId = Integer.parseInt(reservaIdParam.trim());
        } catch (NumberFormatException e) {
            redirigir("/reservas/mis-reservas", req, res);
            return;
        }

        try {
            // HU3 Escenario 1: cancelar y liberar cupo
            servicioReserva.cancelarReserva(reservaId, email);

            String msg = URLEncoder.encode(
                "Reserva cancelada exitosamente. El cupo fue liberado.", "UTF-8");
            res.sendRedirect(req.getContextPath() +
                "/reservas/mis-reservas?email=" +
                URLEncoder.encode(email, "UTF-8") + "&mensaje=" + msg);

        } catch (Exception e) {
            String err = URLEncoder.encode(e.getMessage(), "UTF-8");
            res.sendRedirect(req.getContextPath() +
                "/reservas/mis-reservas?email=" +
                URLEncoder.encode(email, "UTF-8") + "&error=" + err);
        }
    }
}