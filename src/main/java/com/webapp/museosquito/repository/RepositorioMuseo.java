package com.webapp.museosquito.repository;

import com.webapp.museosquito.model.Museo;
import com.webapp.museosquito.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Collections;
import java.util.List;

/**
 * Repositorio para operaciones de acceso a datos de Museo.
 * Backend - HU1: GET /museos y GET /museos/{id}
 */
public class RepositorioMuseo {

    private SessionFactory sf() {
        return HibernateUtil.getSessionFactory();
    }

    public Museo guardar(Museo museo) {
        try (Session s = sf().openSession()) {
            s.beginTransaction();
            s.persist(museo);
            s.getTransaction().commit();
            return museo;
        }
    }

    public Museo actualizar(Museo museo) {
        try (Session s = sf().openSession()) {
            s.beginTransaction();
            Museo merged = s.merge(museo);
            s.getTransaction().commit();
            return merged;
        }
    }

    /**
     * GET /museos/{id} - Retorna detalle de un museo específico.
     */
    public Museo buscarPorId(int id) {
        try (Session s = sf().openSession()) {
            Museo museo = s.get(Museo.class, id);
            if (museo != null) {
                // Inicializar colecciones lazy
                museo.getHorarios().size();
                museo.getExposiciones().size();
            }
            return museo;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * GET /museos - Retorna lista de todos los museos activos.
     */
    public List<Museo> listar() {
        try (Session s = sf().openSession()) {
            return s.createQuery(
                    "FROM Museo WHERE activo = true ORDER BY nombre", Museo.class
            ).list();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    /**
     * Búsqueda por categoría para filtrado en la vista.
     */
    public List<Museo> listarPorCategoria(String categoria) {
        try (Session s = sf().openSession()) {
            return s.createQuery(
                    "FROM Museo WHERE activo = true AND categoria = :cat ORDER BY nombre",
                    Museo.class
            ).setParameter("cat", categoria).list();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    /**
     * Búsqueda por nombre (para buscador).
     */
    public List<Museo> buscarPorNombre(String termino) {
        try (Session s = sf().openSession()) {
            return s.createQuery(
                    "FROM Museo WHERE activo = true AND lower(nombre) LIKE :term ORDER BY nombre",
                    Museo.class
            ).setParameter("term", "%" + termino.toLowerCase() + "%").list();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    /**
     * Verifica si ya existen museos en la BD (para DataSeeder).
     */
    public boolean hayDatos() {
        try (Session s = sf().openSession()) {
            Long count = s.createQuery("SELECT count(m) FROM Museo m", Long.class)
                    .uniqueResult();
            return count != null && count > 0;
        } catch (Exception e) {
            return false;
        }
    }
}
