package com.webapp.museosquito.controller.visitante;

import com.webapp.museosquito.controller.ControladorBase;
import com.webapp.museosquito.model.FranjaReserva;
import com.webapp.museosquito.model.Museo;
import com.webapp.museosquito.service.ServicioMuseo;
import com.webapp.museosquito.service.ServicioReserva;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Controlador para selección de fecha y horario disponible.
 * HU2: GET /museos/{id}/horarios
 * Frontend: Pantalla de selección de fecha y horario.
 */
@WebServlet(name = "ControladorHorariosMuseo", urlPatterns = "/museos/horarios")
public class ControladorHorariosMuseo extends ControladorBase {

    private ServicioMuseo   servicioMuseo;
    private ServicioReserva servicioReserva;

    @Override
    public void init() {
        servicioMuseo   = new ServicioMuseo();
        servicioReserva = new ServicioReserva();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        setEncoding(req, res);

        String idParam = req.getParameter("museoId");
        if (idParam == null || idParam.isBlank()) {
            redirigir("/museos", req, res);
            return;
        }

        int museoId;
        try {
            museoId = Integer.parseInt(idParam.trim());
        } catch (NumberFormatException e) {
            redirigir("/museos", req, res);
            return;
        }

        Museo museo = servicioMuseo.obtenerPorId(museoId);
        if (museo == null) {
            req.setAttribute("error", "El museo seleccionado no existe.");
            redirigir("/museos", req, res);
            return;
        }

        // Franjas con y sin cupos (Frontend las muestra agotadas visualmente)
        List<FranjaReserva> franjas = servicioReserva.obtenerFranjasPorMuseo(museoId);

        req.setAttribute("museo",  museo);
        req.setAttribute("franjas", franjas);
        irAVista("visitante/seleccionHorario.jsp", req, res);
    }
}