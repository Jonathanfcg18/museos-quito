package com.webapp.museosquito.controller.visitante;

import com.webapp.museosquito.controller.ControladorBase;
import com.webapp.museosquito.model.Reserva;
import com.webapp.museosquito.model.Usuario;
import com.webapp.museosquito.service.ServicioReserva;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * HU-08: Ver el detalle completo de una reserva.
 * GET /reservas/detalle?id={id}
 * Valida que la reserva pertenezca al visitante en sesión.
 */
@WebServlet(name = "ControladorDetalleReserva", urlPatterns = "/reservas/detalle")
public class ControladorDetalleReserva extends ControladorBase {

    private ServicioReserva servicioReserva;

    @Override
    public void init() {
        servicioReserva = new ServicioReserva();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        setEncoding(req, res);

        HttpSession session = req.getSession(false);
        Usuario usuario = (session != null)
                ? (Usuario) session.getAttribute("usuarioSesion") : null;

        if (usuario == null) {
            redirigir("/login", req, res);
            return;
        }

        String idParam = req.getParameter("id");
        if (idParam == null || idParam.isBlank()) {
            redirigir("/reservas/mis-reservas", req, res);
            return;
        }

        int reservaId;
        try {
            reservaId = Integer.parseInt(idParam.trim());
        } catch (NumberFormatException e) {
            redirigir("/reservas/mis-reservas", req, res);
            return;
        }

        Reserva reserva = servicioReserva.obtenerReservaPorId(reservaId);

        // HU-08 Escenario 2: reserva no existe o no pertenece al visitante en sesión
        if (reserva == null ||
                !reserva.getEmailVisitante().equalsIgnoreCase(usuario.getEmail())) {
            req.setAttribute("error", "La reserva solicitada no fue encontrada.");
            irAVista("visitante/misReservas.jsp", req, res);
            return;
        }

        req.setAttribute("reserva", reserva);
        req.setAttribute("usuario", usuario);
        irAVista("visitante/detalleReserva.jsp", req, res);
    }
}