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
 * Solo aplica a las rutas nuevas del Sprint 2.
 * Las rutas /admin/* del Sprint 1 tienen su propio control de acceso
 * (ControladorLoginAdmin + obtenerAdmin()).
 *
 * Sprint 2 – HU-06 T-02.6
 * Responsable: Jhonny Moreira
 */
@WebFilter(filterName = "AutorizacionFilter", urlPatterns = {
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

        // Sin sesión: el AutenticacionFilter ya habrá redirigido
        if (usuario == null) {
            chain.doFilter(request, response);
            return;
        }

        String path = req.getServletPath();
        boolean autorizado = false;

        if (path.startsWith("/admin-museo/") && usuario.isAdminMuseo()) {
            autorizado = true;
        } else if (path.startsWith("/admin-sistema/") && usuario.isAdminSistema()) {
            autorizado = true;
        }

        if (!autorizado) {
            req.getRequestDispatcher("/WEB-INF/views/error-acceso.jsp")
               .forward(req, res);
            return;
        }

        chain.doFilter(request, response);
    }
}