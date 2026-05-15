package com.webapp.museosquito.controller.visitante;

import com.webapp.museosquito.controller.ControladorBase;
import com.webapp.museosquito.model.Museo;
import com.webapp.museosquito.service.ServicioMuseo;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Controlador principal de museos.
 *
 * HU1 – Escenario 1: GET /museos
 *   → Muestra lista de museos con nombre, ubicación, descripción y horarios.
 *
 * También maneja búsqueda y filtrado por categoría.
 *
 * Backend: endpoint GET /museos
 */
@WebServlet(name = "ControladorMuseos", urlPatterns = "/museos")
public class ControladorMuseos extends ControladorBase {

    private ServicioMuseo servicioMuseo;

    @Override
    public void init() {
        servicioMuseo = new ServicioMuseo();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        setEncoding(req, res);

        String categoria = req.getParameter("categoria");
        String busqueda  = req.getParameter("busqueda");

        List<Museo> museos;

        if (busqueda != null && !busqueda.isBlank()) {
            museos = servicioMuseo.buscarPorNombre(busqueda.trim());
            req.setAttribute("busqueda", busqueda.trim());
        } else if (categoria != null && !categoria.isBlank()) {
            museos = servicioMuseo.filtrarPorCategoria(categoria);
            req.setAttribute("categoriaSeleccionada", categoria);
        } else {
            // HU1 – Escenario 1: carga todos los museos disponibles
            museos = servicioMuseo.obtenerTodos();
        }

        // HU1 criterio: La información debe estar actualizada
        req.setAttribute("museos", museos);
        req.setAttribute("totalMuseos", museos.size());

        irAVista("visitante/museos.jsp", req, res);
    }
}
