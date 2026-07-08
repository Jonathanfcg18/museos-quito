package com.webapp.museosquito.controller.visitante;

import com.webapp.museosquito.controller.ControladorBase;
import com.webapp.museosquito.model.FranjaReserva;
import com.webapp.museosquito.model.Reserva;
import com.webapp.museosquito.service.ServicioReserva;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.webapp.museosquito.model.FranjaReserva;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Controlador para registrar una reserva.
 * HU2: POST /reservas
 * Escenario 1: reserva exitosa → muestra confirmación.
 * Escenario 2: sin cupos → regresa con mensaje de error.
 */
@WebServlet(name = "ControladorReserva", urlPatterns = "/reservas")
public class ControladorReserva extends ControladorBase {

    private ServicioReserva servicioReserva;

    @Override
    public void init() {
        servicioReserva = new ServicioReserva();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        setEncoding(req, res);

        // HAL-08: obtener email siempre desde la sesión, ignorar el campo del formulario
        HttpSession session = req.getSession(false);
        com.webapp.museosquito.model.Usuario usuario = (session != null)
                ? (com.webapp.museosquito.model.Usuario) session.getAttribute("usuarioSesion")
                : null;

        String franjaIdParam = req.getParameter("franjaId");
        String nombre        = req.getParameter("nombre");
        String cantidadParam = req.getParameter("cantidad");
        String museoIdParam  = req.getParameter("museoId");

        // HAL-08: email siempre de la sesión
        String email = (usuario != null) ? usuario.getEmail() : req.getParameter("email");

        if (franjaIdParam == null || nombre == null ||
                email == null || cantidadParam == null) {
            req.setAttribute("error", "Todos los campos son obligatorios.");
            redirigir("/museos", req, res);
            return;
        }

        int franjaId, cantidad, museoId;
        try {
            franjaId = Integer.parseInt(franjaIdParam.trim());
            cantidad = Integer.parseInt(cantidadParam.trim());
            museoId  = Integer.parseInt(museoIdParam.trim());
        } catch (NumberFormatException e) {
            redirigir("/museos", req, res);
            return;
        }

        try {
            Reserva reserva = servicioReserva.crearReserva(
                    franjaId, nombre, email, cantidad);

            FranjaReserva franja = servicioReserva.obtenerFranjaPorId(franjaId);
            reserva.setFranja(franja);

            req.setAttribute("reserva", reserva);
            irAVista("visitante/confirmacionReserva.jsp", req, res);

        } catch (IllegalStateException e) {
            res.sendRedirect(req.getContextPath() +
                    "/museos/horarios?museoId=" + museoId +
                    "&error=" + java.net.URLEncoder.encode(e.getMessage(), "UTF-8"));

        } catch (IllegalArgumentException e) {
            res.sendRedirect(req.getContextPath() +
                    "/museos/horarios?museoId=" + museoId +
                    "&error=" + java.net.URLEncoder.encode(e.getMessage(), "UTF-8"));
        }
    }
}