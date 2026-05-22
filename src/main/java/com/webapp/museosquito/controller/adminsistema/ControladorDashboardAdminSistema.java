package com.webapp.museosquito.controller.adminsistema;

import com.webapp.museosquito.controller.ControladorBase;
import com.webapp.museosquito.model.Museo;
import com.webapp.museosquito.model.Usuario;
import com.webapp.museosquito.repository.RepositorioMuseo;
import com.webapp.museosquito.repository.RepositorioUsuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Dashboard del Administrador del Sistema.
 *
 * Sprint 2 – HU-06 Escenario 1: destino de redirección tras login con
 * rol ADMIN_SISTEMA → /admin-sistema/dashboard
 *
 * Muestra métricas globales de la plataforma: total de museos,
 * total de usuarios registrados y accesos rápidos a gestión.
 *
 * Responsable Backend: Jhonny Moreira
 */
@WebServlet(name = "ControladorDashboardAdminSistema",
        urlPatterns = "/admin-sistema/dashboard")
public class ControladorDashboardAdminSistema extends ControladorBase {

    private RepositorioMuseo   repoMuseo;
    private RepositorioUsuario repoUsuario;

    @Override
    public void init() {
        repoMuseo   = new RepositorioMuseo();
        repoUsuario = new RepositorioUsuario();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        setEncoding(req, res);

        // Verificar sesión con rol ADMIN_SISTEMA
        HttpSession session = req.getSession(false);
        Usuario usuario = (session != null)
                ? (Usuario) session.getAttribute("usuarioSesion") : null;

        if (usuario == null || !usuario.isAdminSistema()) {
            redirigir("/login", req, res);
            return;
        }

        // Métricas globales
        List<Museo> museos = repoMuseo.listar();

        req.setAttribute("usuario",      usuario);
        req.setAttribute("totalMuseos",  museos.size());
        req.setAttribute("museos",       museos);

        irAVista("admin-sistema/dashboard.jsp", req, res);
    }
}