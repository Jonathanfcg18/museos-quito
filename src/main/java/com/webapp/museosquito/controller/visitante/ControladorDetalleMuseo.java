package com.webapp.museosquito.controller.visitante;

import com.webapp.museosquito.controller.ControladorBase;
import com.webapp.museosquito.model.Museo;
import com.webapp.museosquito.service.ServicioMuseo;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Controlador para el detalle de un museo específico.
 *
 * HU1 – Escenario 2: GET /museos/detalle?id={id}
 *   → Dirige al usuario a una vista detallada con información completa del museo.
 *
 * Backend: endpoint GET /museos/{id}
 */
@WebServlet(name = "ControladorDetalleMuseo", urlPatterns = "/museos/detalle")
public class ControladorDetalleMuseo extends ControladorBase {

    private ServicioMuseo servicioMuseo;

    @Override
    public void init() {
        servicioMuseo = new ServicioMuseo();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        setEncoding(req, res);

        String idParam = req.getParameter("id");

        if (idParam == null || idParam.isBlank()) {
            redirigir("/museos", req, res);
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idParam.trim());
        } catch (NumberFormatException e) {
            redirigir("/museos", req, res);
            return;
        }

        // HU1 – Escenario 2: retorna detalle de museo específico con info completa
        Museo museo = servicioMuseo.obtenerPorId(id);

        if (museo == null) {
            req.setAttribute("error", "El museo solicitado no fue encontrado.");
            irAVista("visitante/museos.jsp", req, res);
            return;
        }

        req.setAttribute("museo", museo);
        irAVista("visitante/detalleMuseo.jsp", req, res);
    }
}
