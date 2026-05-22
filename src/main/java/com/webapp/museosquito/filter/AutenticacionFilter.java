package com.webapp.museosquito.filter;

import com.webapp.museosquito.model.Usuario;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Intercepta las rutas protegidas y redirige al login si no hay sesión activa.
 *
 * Sprint 2 – HU-02 T-02.5: AutenticacionFilter.
 * Responsable Backend: Jhonny Moreira
 *
 * Rutas protegidas: /reservas/*, /admin/*, /admin-museo/*, /admin-sistema/*
 *
 * Rutas públicas (no interceptadas):
 *   /, /museos, /museos/detalle, /museos/horarios,
 *   /login, /registro, /logout, /css/*, recursos estáticos.
 */
@WebFilter(filterName = "AutenticacionFilter", urlPatterns = {
        "/reservas/*",
        "/admin/*",
        "/admin-museo/*",
        "/admin-sistema/*"
})
public class AutenticacionFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest  req = (HttpServletRequest)  request;
        HttpServletResponse res = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);
        Usuario usuario = (session != null)
                ? (Usuario) session.getAttribute("usuarioSesion") : null;

        if (usuario == null) {
            // Sin sesión: redirigir al login
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // Sesión válida: continuar con la cadena de filtros
        chain.doFilter(request, response);
    }
}