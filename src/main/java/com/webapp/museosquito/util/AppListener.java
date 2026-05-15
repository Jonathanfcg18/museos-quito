package com.webapp.museosquito.util;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

/**
 * Listener que inicializa Hibernate y ejecuta el DataSeeder al arrancar la app.
 */
@WebListener
public class AppListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext ctx = sce.getServletContext();
        String realPath = ctx.getRealPath("/");

        if (realPath == null) {
            throw new RuntimeException("No se pudo obtener la ruta real de la webapp.");
        }

        System.out.println("[AppListener] Inicializando Hibernate...");
        try {
            HibernateUtil.init(realPath);
            System.out.println("[AppListener] Hibernate OK.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Fallo al inicializar Hibernate", e);
        }

        System.out.println("[AppListener] Iniciando seeding de museos de Quito...");
        try {
            new DataSeeder().sembrar();
            System.out.println("[AppListener] Seeding OK.");
        } catch (Exception e) {
            System.err.println("[AppListener] Error en seeding: " + e.getMessage());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        HibernateUtil.shutdown();
    }
}
