package com.webapp.museosquito.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Servlet base con utilidades comunes para todos los controladores.
 */
public abstract class ControladorBase extends HttpServlet {

    /**
     * Establece encoding UTF-8 en request y response.
     */
    protected void setEncoding(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        req.setCharacterEncoding("UTF-8");
        res.setCharacterEncoding("UTF-8");
        res.setContentType("text/html;charset=UTF-8");
    }

    /**
     * Redirige a una vista JSP bajo WEB-INF/views/.
     */
    protected void irAVista(String vista, HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/" + vista).forward(req, res);
    }

    /**
     * Redirige (redirect) a una URL relativa al contexto.
     */
    protected void redirigir(String url, HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        res.sendRedirect(req.getContextPath() + url);
    }
}
