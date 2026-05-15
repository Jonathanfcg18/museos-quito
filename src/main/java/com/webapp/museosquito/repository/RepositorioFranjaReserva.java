package com.webapp.museosquito.repository;

import com.webapp.museosquito.model.FranjaReserva;
import com.webapp.museosquito.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Collections;
import java.util.List;

/**
 * Repositorio para FranjaReserva.
 * HU2: endpoint GET /museos/{id}/horarios
 */
public class RepositorioFranjaReserva {

    private SessionFactory sf() {
        return HibernateUtil.getSessionFactory();
    }

    public FranjaReserva guardar(FranjaReserva f) {
        try (Session s = sf().openSession()) {
            s.beginTransaction();
            s.persist(f);
            s.getTransaction().commit();
            return f;
        }
    }

    public FranjaReserva actualizar(FranjaReserva f) {
        try (Session s = sf().openSession()) {
            s.beginTransaction();
            FranjaReserva merged = s.merge(f);
            s.getTransaction().commit();
            return merged;
        }
    }

    public FranjaReserva buscarPorId(int id) {
        try (Session s = sf().openSession()) {
            FranjaReserva f = s.get(FranjaReserva.class, id);
            if (f != null) {
                // Inicializar la relación lazy museo para que el JSP pueda acceder
                f.getMuseo().getNombre();
            }
            return f;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * GET /museos/{id}/horarios — franjas disponibles a futuro para un museo.
     */
    public List<FranjaReserva> listarPorMuseo(int museoId) {
        try (Session s = sf().openSession()) {
            return s.createQuery(
                    "FROM FranjaReserva f WHERE f.museo.id = :mid " +
                    "ORDER BY f.fecha, f.horaInicio",
                    FranjaReserva.class
            ).setParameter("mid", museoId).list();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public boolean hayDatos(int museoId) {
        try (Session s = sf().openSession()) {
            Long count = s.createQuery(
                    "SELECT count(f) FROM FranjaReserva f WHERE f.museo.id = :mid",
                    Long.class
            ).setParameter("mid", museoId).uniqueResult();
            return count != null && count > 0;
        } catch (Exception e) {
            return false;
        }
    }
}