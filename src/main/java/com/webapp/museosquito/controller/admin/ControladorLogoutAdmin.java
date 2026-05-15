package com.webapp.museosquito.controller.admin;

import com.webapp.museosquito.controller.ControladorBase;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "ControladorLogoutAdmin", urlPatterns = "/admin/logout")
public class ControladorLogoutAdmin extends ControladorBase {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null) session.invalidate();
        redirigir("/admin/login", req, res);
    }
}