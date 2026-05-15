package com.webapp.museosquito.repository;

import com.webapp.museosquito.model.Reserva;
import com.webapp.museosquito.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Collections;
import java.util.List;

/**
 * Repositorio para Reserva.
 * HU2: endpoint POST /reservas
 */
public class RepositorioReserva {

    private SessionFactory sf() {
        return HibernateUtil.getSessionFactory();
    }

    public Reserva guardar(Reserva r) {
        try (Session s = sf().openSession()) {
            s.beginTransaction();
            s.persist(r);
            s.getTransaction().commit();
            return r;
        }
    }

    public List<Reserva> listarPorEmail(String email) {
        try (Session s = sf().openSession()) {
            return s.createQuery(
                    "FROM Reserva r WHERE r.emailVisitante = :email " +
                    "ORDER BY r.fechaRegistro DESC",
                    Reserva.class
            ).setParameter("email", email).list();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public Reserva buscarPorCodigo(String codigo) {
        try (Session s = sf().openSession()) {
            return s.createQuery(
                    "FROM Reserva r WHERE r.codigoConfirmacion = :codigo",
                    Reserva.class
            ).setParameter("codigo", codigo).uniqueResult();
        } catch (Exception e) {
            return null;
        }
    }
    
}