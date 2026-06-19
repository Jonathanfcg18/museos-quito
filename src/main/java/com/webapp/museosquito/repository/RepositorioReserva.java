package com.webapp.museosquito.repository;

import com.webapp.museosquito.model.Reserva;
import com.webapp.museosquito.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Collections;
import java.util.List;

/**
 * Repositorio para Reserva.
 * HU2: POST /reservas
 * HU3: GET /reservas/usuario, DELETE /reservas/{id}, PUT /reservas/{id}
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

    public Reserva actualizar(Reserva r) {
        try (Session s = sf().openSession()) {
            s.beginTransaction();
            Reserva merged = s.merge(r);
            s.getTransaction().commit();
            return merged;
        }
    }

    public Reserva buscarPorId(int id) {
        try (Session s = sf().openSession()) {
            Reserva r = s.get(Reserva.class, id);
            if (r != null) {
                // Inicializar relaciones lazy
                r.getFranja().getMuseo().getNombre();
                r.getFranja().getFecha();
            }
            return r;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * GET /reservas/usuario — lista reservas activas por email.
     */
    public List<Reserva> listarActivasPorEmail(String email) {
        try (Session s = sf().openSession()) {
            List<Reserva> lista = s.createQuery(
                    "FROM Reserva r WHERE r.emailVisitante = :email " +
                    "AND r.activa = true ORDER BY r.fechaRegistro DESC",
                    Reserva.class
            ).setParameter("email", email).list();
            // Inicializar relaciones lazy para cada reserva
            for (Reserva r : lista) {
                r.getFranja().getMuseo().getNombre();
                r.getFranja().getFecha();
            }
            return lista;
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    /**
     * Lista TODAS las reservas (activas y canceladas) de un visitante.
     * Usada por HU-07 y HU-09 para mostrar y filtrar por estado en el cliente.
     */
    public List<Reserva> listarTodasPorEmail(String email) {
        try (Session s = sf().openSession()) {
            List<Reserva> lista = s.createQuery(
                    "FROM Reserva r WHERE r.emailVisitante = :email " +
                            "ORDER BY r.fechaRegistro DESC",
                    Reserva.class
            ).setParameter("email", email).list();
            for (Reserva r : lista) {
                r.getFranja().getMuseo().getNombre();
                r.getFranja().getFecha();
            }
            return lista;
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public Reserva buscarPorCodigo(String codigo) {
        try (Session s = sf().openSession()) {
            Reserva r = s.createQuery(
                    "FROM Reserva r WHERE r.codigoConfirmacion = :codigo",
                    Reserva.class
            ).setParameter("codigo", codigo).uniqueResult();
            if (r != null) {
                r.getFranja().getMuseo().getNombre();
            }
            return r;
        } catch (Exception e) {
            return null;
        }
    }
}