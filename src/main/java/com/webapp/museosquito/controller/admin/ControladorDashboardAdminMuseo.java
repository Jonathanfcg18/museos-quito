package com.webapp.museosquito.controller.admin;

import com.webapp.museosquito.controller.ControladorBase;
import com.webapp.museosquito.model.FranjaReserva;
import com.webapp.museosquito.model.Usuario;
import com.webapp.museosquito.repository.RepositorioAdminMuseo;
import com.webapp.museosquito.repository.RepositorioMuseo;
import com.webapp.museosquito.service.ServicioAdmin;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Dashboard del Administrador del Museo.
 *
 * Sprint 2 – HU-06 Escenario 1: destino de redirección tras login con
 * rol ADMIN_MUSEO → /admin-museo/dashboard
 *
 * Lee la sesión del nuevo sistema (usuarioSesion → Usuario).
 * El museo del admin se resuelve buscando en la tabla admins_museo
 * por el mismo email, para mantener compatibilidad con los datos
 * del Sprint 1 sin migrar la base de datos.
 *
 * Responsable Backend: Jhonny Moreira
 */
@WebServlet(name = "ControladorDashboardAdminMuseo",
        urlPatterns = "/admin-museo/dashboard")
public class ControladorDashboardAdminMuseo extends ControladorBase {

    private ServicioAdmin        servicioAdmin;
    private RepositorioAdminMuseo repoAdminMuseo;
    private RepositorioMuseo     repoMuseo;

    @Override
    public void init() {
        servicioAdmin  = new ServicioAdmin();
        repoAdminMuseo = new RepositorioAdminMuseo();
        repoMuseo      = new RepositorioMuseo();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        setEncoding(req, res);

        // Leer sesión del nuevo sistema (Sprint 2)
        HttpSession session = req.getSession(false);
        Usuario usuario = (session != null)
                ? (Usuario) session.getAttribute("usuarioSesion") : null;

        if (usuario == null || !usuario.isAdminMuseo()) {
            redirigir("/login", req, res);
            return;
        }

        // Resolver el museo asociado buscando en la tabla legacy admins_museo
        // por el mismo email. Esto evita migrar la BD del Sprint 1.
        com.webapp.museosquito.model.AdminMuseo adminLegacy =
                repoAdminMuseo.buscarPorEmail(usuario.getEmail());

        List<FranjaReserva> franjas = Collections.emptyList();
        String nombreMuseo = "—";
        int museoId = 0;

        if (adminLegacy != null && adminLegacy.getMuseo() != null) {
            museoId    = adminLegacy.getMuseo().getId();
            nombreMuseo = adminLegacy.getMuseo().getNombre();
            franjas    = servicioAdmin.obtenerFranjasPorMuseo(museoId);
        }

        // Métricas básicas para el dashboard
        int totalFranjas   = franjas.size();
        int totalReservas  = franjas.stream()
                .mapToInt(FranjaReserva::getAforoOcupado).sum();
        long franjasAgotadas = franjas.stream()
                .filter(f -> !f.hayCupos()).count();

        req.setAttribute("usuario",       usuario);
        req.setAttribute("nombreMuseo",   nombreMuseo);
        req.setAttribute("museoId",       museoId);
        req.setAttribute("franjas",       franjas);
        req.setAttribute("totalFranjas",  totalFranjas);
        req.setAttribute("totalReservas", totalReservas);
        req.setAttribute("franjasAgotadas", franjasAgotadas);

        irAVista("admin-museo/dashboard.jsp", req, res);
    }
}