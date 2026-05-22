package com.webapp.museosquito.filter;

import com.webapp.museosquito.model.Usuario;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Intercepta las rutas protegidas del Sprint 2 y redirige al login
 * si no hay sesión activa (usuarioSesion).
 *
 * IMPORTANTE: Solo protege /admin-museo/* y /admin-sistema/*.
 * Las rutas /admin/* del Sprint 1 mantienen su propio sistema de sesión
 * (adminMuseo) y no deben ser interceptadas por este filtro.
 * Las rutas /reservas/* siguen siendo públicas (como en el Sprint 1).
 *
 * Sprint 2 – HU-06 T-02.5
 * Responsable: Jhonny Moreira
 */
@WebFilter(filterName = "AutenticacionFilter", urlPatterns = {
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
            // Sin sesión Sprint 2: redirigir al login unificado
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        chain.doFilter(request, response);
    }
}