package com.webapp.museosquito.filter;

import com.webapp.museosquito.model.Usuario;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Verifica que el rol del usuario en sesión corresponda a la ruta solicitada.
 * Si no coincide, muestra la vista de acceso denegado.
 *
 * Sprint 2 – HU-02 T-02.6: AutorizacionFilter.
 * Responsable Backend: Jhonny Moreira
 *
 * Reglas:
 *   /admin/*         → solo ADMIN_MUSEO (panel de horarios del Sprint 1)
 *   /admin-museo/*   → solo ADMIN_MUSEO
 *   /admin-sistema/* → solo ADMIN_SISTEMA
 *   /reservas/*      → VISITANTE (y también ADMIN_MUSEO puede ver)
 */
@WebFilter(filterName = "AutorizacionFilter", urlPatterns = {
        "/admin/*",
        "/admin-museo/*",
        "/admin-sistema/*"
})
public class AutorizacionFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest  req = (HttpServletRequest)  request;
        HttpServletResponse res = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);
        Usuario usuario = (session != null)
                ? (Usuario) session.getAttribute("usuarioSesion") : null;

        // Si no hay sesión, el AutenticacionFilter ya habrá redirigido
        if (usuario == null) {
            chain.doFilter(request, response);
            return;
        }

        String path = req.getServletPath();
        boolean autorizado = false;

        if ((path.startsWith("/admin/") || path.startsWith("/admin-museo/"))
                && usuario.isAdminMuseo()) {
            autorizado = true;
        } else if (path.startsWith("/admin-sistema/") && usuario.isAdminSistema()) {
            autorizado = true;
        }

        if (!autorizado) {
            // Mostrar vista de acceso denegado sin revelar detalles de la ruta
            req.getRequestDispatcher("/WEB-INF/views/error-acceso.jsp")
               .forward(req, res);
            return;
        }

        chain.doFilter(request, response);
    }
}