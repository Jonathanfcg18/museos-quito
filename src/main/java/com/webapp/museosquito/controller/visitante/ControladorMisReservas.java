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
import java.util.List;

/**
 * HU-07: Muestra reservas automáticamente desde la sesión activa.
 * HU-09: Soporta filtrado por estado vía parámetro ?estado=
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

        // HU-07 Escenario 3: sin sesión → redirige al login (cubierto por AutenticacionFilter)
        HttpSession session = req.getSession(false);
        Usuario usuario = (session != null)
                ? (Usuario) session.getAttribute("usuarioSesion") : null;

        if (usuario == null) {
            redirigir("/login", req, res);
            return;
        }

        // HU-07 Escenario 1 y 2: obtener email desde sesión, sin pedir correo
        String email = usuario.getEmail();

        // HU-09: parámetro de filtro opcional
        String estado = req.getParameter("estado"); // null, "activas", "canceladas"

        List<Reserva> reservas = servicioReserva.obtenerTodasReservasPorEmail(email);

        // Mensajes de feedback (vienen de cancelar/modificar)
        String mensaje = req.getParameter("mensaje");
        String error   = req.getParameter("error");

        req.setAttribute("reservas",      reservas);
        req.setAttribute("email",         email);
        req.setAttribute("usuario",       usuario);
        req.setAttribute("estadoFiltro",  estado != null ? estado : "todas");
        req.setAttribute("totalReservas", reservas.size());

        if (mensaje != null)
            req.setAttribute("mensaje", java.net.URLDecoder.decode(mensaje, "UTF-8"));
        if (error != null)
            req.setAttribute("error", java.net.URLDecoder.decode(error, "UTF-8"));

        irAVista("visitante/misReservas.jsp", req, res);
    }
}