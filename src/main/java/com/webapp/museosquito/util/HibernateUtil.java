package com.webapp.museosquito.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import java.io.File;
import java.nio.file.Files;

/**
 * Inicializa y provee la SessionFactory de Hibernate.
 * La BD SQLite se guarda en src/main/webapp/WEB-INF para persistir entre
 * reinicios.
 */
public class HibernateUtil {

    private static SessionFactory sessionFactory;

    public static synchronized void init(String webAppRealPath) {
        if (sessionFactory != null)
            return;

        // Railway inyecta DATABASE_URL en formato postgresql://user:pass@host:port/db
        String databaseUrl = System.getenv("DATABASE_URL");

        String jdbcUrl, user, password;

        if (databaseUrl != null && !databaseUrl.isBlank()) {
            // Parsear URL de Railway: postgresql://user:pass@host:port/db
            try {
                java.net.URI uri = new java.net.URI(databaseUrl.replace("postgresql://", "http://"));
                String userInfo = uri.getUserInfo();
                user = userInfo.split(":")[0];
                password = userInfo.split(":")[1];
                jdbcUrl = "jdbc:postgresql://" + uri.getHost() + ":" + uri.getPort() + uri.getPath();
                System.out.println("[HibernateUtil] Usando PostgreSQL de Railway.");
            } catch (Exception e) {
                throw new RuntimeException("No se pudo parsear DATABASE_URL: " + databaseUrl, e);
            }
        } else {
            // Fallback local: SQLite para desarrollo en tu máquina
            File dbFile = resolverRutaBD(webAppRealPath);
            jdbcUrl = "jdbc:sqlite:" + dbFile.getAbsolutePath();
            user = null;
            password = null;
            System.out.println("[HibernateUtil] Usando SQLite local: " + jdbcUrl);
        }

        Configuration cfg = new Configuration().configure();
        cfg.setProperty(Environment.URL, jdbcUrl);
        if (user != null)
            cfg.setProperty(Environment.USER, user);
        if (password != null)
            cfg.setProperty(Environment.PASS, password);

// Ajustar dialecto y driver según la base de datos
        String driverClass;
        if (jdbcUrl.contains("postgresql")) {
            driverClass = "org.postgresql.Driver";
            cfg.setProperty(Environment.DIALECT, "org.hibernate.dialect.PostgreSQLDialect");
        } else {
            driverClass = "org.sqlite.JDBC";
            cfg.setProperty(Environment.DIALECT, "org.hibernate.community.dialect.SQLiteDialect");
        }

        try {
            Class.forName(driverClass); // fuerza la carga y expone el error real si falla
        } catch (Throwable t) {
            throw new RuntimeException("No se pudo cargar el driver JDBC " + driverClass, t);
        }
        cfg.setProperty(Environment.DRIVER, driverClass);

        sessionFactory = cfg.buildSessionFactory();
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
            if (candidato == null)
                break;
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
