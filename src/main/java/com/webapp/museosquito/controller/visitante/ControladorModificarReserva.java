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
import java.net.URLEncoder;
import java.util.List;

/**
 * Controlador para modificar una reserva.
 * HU3 Escenario 2: PUT /reservas/{id}
 */
@WebServlet(name = "ControladorModificarReserva", urlPatterns = "/reservas/modificar")
public class ControladorModificarReserva extends ControladorBase {

    private ServicioReserva servicioReserva;

    @Override
    public void init() {
        servicioReserva = new ServicioReserva();
    }

    // GET: muestra formulario de selección de nuevo horario
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
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

        Reserva reserva = servicioReserva.obtenerReservaPorId(reservaId);
        if (reserva == null || !reserva.getEmailVisitante().equalsIgnoreCase(email.trim())) {
            redirigir("/reservas/mis-reservas?email=" +
                URLEncoder.encode(email, "UTF-8"), req, res);
            return;
        }

        // Franjas disponibles del mismo museo para seleccionar
        List<FranjaReserva> franjas = servicioReserva
            .obtenerFranjasPorMuseo(reserva.getFranja().getMuseo().getId());

        req.setAttribute("reserva", reserva);
        req.setAttribute("franjas", franjas);
        req.setAttribute("email", email);
        irAVista("visitante/modificarReserva.jsp", req, res);
    }

    // POST: confirma el cambio de horario
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        setEncoding(req, res);

        String reservaIdParam  = req.getParameter("reservaId");
        String nuevaFranjaParam = req.getParameter("nuevaFranjaId");
        String email            = req.getParameter("email");

        if (reservaIdParam == null || nuevaFranjaParam == null || email == null) {
            redirigir("/reservas/mis-reservas", req, res);
            return;
        }

        int reservaId, nuevaFranjaId;
        try {
            reservaId     = Integer.parseInt(reservaIdParam.trim());
            nuevaFranjaId = Integer.parseInt(nuevaFranjaParam.trim());
        } catch (NumberFormatException e) {
            redirigir("/reservas/mis-reservas", req, res);
            return;
        }

        try {
            // HU3 Escenario 2: modificar reserva liberando cupo original
            servicioReserva.modificarReserva(reservaId, email, nuevaFranjaId);

            String msg = URLEncoder.encode(
                "Reserva modificada exitosamente. El cupo del horario original fue liberado.",
                "UTF-8");
            res.sendRedirect(req.getContextPath() +
                "/reservas/mis-reservas?email=" +
                URLEncoder.encode(email, "UTF-8") + "&mensaje=" + msg);

        } catch (Exception e) {
            String err = URLEncoder.encode(e.getMessage(), "UTF-8");
            res.sendRedirect(req.getContextPath() +
                "/reservas/modificar?reservaId=" + reservaId +
                "&email=" + URLEncoder.encode(email, "UTF-8") + "&error=" + err);
        }
    }
}