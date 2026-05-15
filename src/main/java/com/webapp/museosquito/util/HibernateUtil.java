package com.webapp.museosquito.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import java.io.File;
import java.nio.file.Files;

/**
 * Inicializa y provee la SessionFactory de Hibernate.
 * La BD SQLite se guarda en src/main/webapp/WEB-INF para persistir entre reinicios.
 */
public class HibernateUtil {

    private static SessionFactory sessionFactory;

    public static synchronized void init(String webAppRealPath) {
        if (sessionFactory != null) return;

        File dbFile = resolverRutaBD(webAppRealPath);
        String url = "jdbc:sqlite:" + dbFile.getAbsolutePath();
        System.out.println("[HibernateUtil] BD en: " + url);

        sessionFactory = new Configuration()
                .configure()
                .setProperty(Environment.URL, url)
                .buildSessionFactory();
    }

    private static File resolverRutaBD(String webAppRealPath) {
        // Estrategia 1: buscar src/main/webapp/WEB-INF hacia arriba
        File candidato = new File(webAppRealPath).getAbsoluteFile();
        for (int i = 0; i < 8; i++) {
            File posibleWebInf = new File(candidato, "src/main/webapp/WEB-INF");
            if (posibleWebInf.exists() && posibleWebInf.isDirectory()) {
                System.out.println("[HibernateUtil] Proyecto encontrado en: " + candidato);
                return new File(posibleWebInf, "museos.db");
            }
            candidato = candidato.getParentFile();
            if (candidato == null) break;
        }

        // Estrategia 2: user.dir
        File workDir = new File(System.getProperty("user.dir"));
        File srcWebInf = new File(workDir, "src/main/webapp/WEB-INF");
        if (srcWebInf.exists() && srcWebInf.isDirectory()) {
            System.out.println("[HibernateUtil] Usando WEB-INF del proyecto fuente via user.dir.");
            return new File(srcWebInf, "museos.db");
        }

        // Estrategia 3: fallback al WEB-INF del despliegue
        File deployWebInf = new File(webAppRealPath, "WEB-INF");
        try {
            Files.createDirectories(deployWebInf.toPath());
        } catch (Exception e) {
            System.err.println("[HibernateUtil] No se pudo crear WEB-INF: " + e.getMessage());
        }
        System.out.println("[HibernateUtil] Usando WEB-INF del despliegue (fallback).");
        return new File(deployWebInf, "museos.db");
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            throw new IllegalStateException(
                    "SessionFactory no inicializado. ¿Se registró AppListener?");
        }
        return sessionFactory;
    }

    public static synchronized void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
            sessionFactory = null;
        }
    }
}
