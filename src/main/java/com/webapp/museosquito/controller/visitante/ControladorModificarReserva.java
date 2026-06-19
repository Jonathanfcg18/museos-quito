package com.webapp.museosquito.controller.visitante;

import com.webapp.museosquito.controller.ControladorBase;
import com.webapp.museosquito.model.FranjaReserva;
import com.webapp.museosquito.model.Reserva;
import com.webapp.museosquito.model.Usuario;
import com.webapp.museosquito.service.ServicioReserva;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * Controlador para modificar una reserva.
 * Actualizado para HU-07: toma el email desde la sesión activa.
 */
@WebServlet(name = "ControladorModificarReserva", urlPatterns = "/reservas/modificar")
public class ControladorModificarReserva extends ControladorBase {

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

        String reservaIdParam = req.getParameter("reservaId");
        if (reservaIdParam == null) {
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

        String email = usuario.getEmail();
        Reserva reserva = servicioReserva.obtenerReservaPorId(reservaId);

        if (reserva == null ||
                !reserva.getEmailVisitante().equalsIgnoreCase(email)) {
            redirigir("/reservas/mis-reservas", req, res);
            return;
        }

        List<FranjaReserva> franjas = servicioReserva
                .obtenerFranjasPorMuseo(reserva.getFranja().getMuseo().getId());

        req.setAttribute("reserva", reserva);
        req.setAttribute("franjas", franjas);
        req.setAttribute("email",   email);
        irAVista("visitante/modificarReserva.jsp", req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        setEncoding(req, res);

        HttpSession session = req.getSession(false);
        Usuario usuario = (session != null)
                ? (Usuario) session.getAttribute("usuarioSesion") : null;

        if (usuario == null) {
            redirigir("/login", req, res);
            return;
        }

        String reservaIdParam   = req.getParameter("reservaId");
        String nuevaFranjaParam = req.getParameter("nuevaFranjaId");

        if (reservaIdParam == null || nuevaFranjaParam == null) {
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
            servicioReserva.modificarReserva(reservaId, usuario.getEmail(), nuevaFranjaId);
            String msg = URLEncoder.encode(
                    "Reserva modificada exitosamente. El cupo del horario original fue liberado.",
                    "UTF-8");
            res.sendRedirect(req.getContextPath() +
                    "/reservas/mis-reservas?mensaje=" + msg);
        } catch (Exception e) {
            String err = URLEncoder.encode(e.getMessage(), "UTF-8");
            res.sendRedirect(req.getContextPath() +
                    "/reservas/modificar?reservaId=" + reservaId + "&error=" + err);
        }
    }
}