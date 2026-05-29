package com.webapp.museosquito.filter;

import com.webapp.museosquito.model.Usuario;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Protege rutas que requieren sesión activa.
 * Sin sesión → redirige al login.
 */
@WebFilter(filterName = "AutenticacionFilter", urlPatterns = {
        "/museos",
        "/museos/*",
        "/reservas/*",
        "/admin-museo/*",
        "/admin-sistema/*"
})
public class AutenticacionFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest  req = (HttpServletRequest)  request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getServletPath();

        // Rutas del sistema nuevo (Sprint 2): requieren usuarioSesion
        HttpSession session = req.getSession(false);
        Usuario usuario = (session != null)
                ? (Usuario) session.getAttribute("usuarioSesion") : null;

        // Admin legacy Sprint 1 (/admin/*) tiene su propio sistema de sesión
        if (path.startsWith("/admin/") || path.equals("/admin")) {
            chain.doFilter(request, response);
            return;
        }

        if (usuario == null) {
            // Guardar la URL a la que intentaba acceder para redirigir después del login
            String queryString = req.getQueryString();
            String urlOriginal = req.getContextPath() + path +
                    (queryString != null ? "?" + queryString : "");
            req.getSession(true).setAttribute("urlAnteriorLogin", urlOriginal);
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        chain.doFilter(request, response);
    }
}