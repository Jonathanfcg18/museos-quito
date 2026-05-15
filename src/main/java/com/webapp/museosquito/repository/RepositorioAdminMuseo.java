package com.webapp.museosquito.repository;

import com.webapp.museosquito.model.AdminMuseo;
import com.webapp.museosquito.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class RepositorioAdminMuseo {

    private SessionFactory sf() {
        return HibernateUtil.getSessionFactory();
    }

    public AdminMuseo guardar(AdminMuseo a) {
        try (Session s = sf().openSession()) {
            s.beginTransaction();
            s.persist(a);
            s.getTransaction().commit();
            return a;
        }
    }

    public AdminMuseo buscarPorEmail(String email) {
        try (Session s = sf().openSession()) {
            AdminMuseo a = s.createQuery(
                    "FROM AdminMuseo a WHERE a.email = :email",
                    AdminMuseo.class
            ).setParameter("email", email).uniqueResult();
            if (a != null) a.getMuseo().getNombre();
            return a;
        } catch (Exception e) {
            return null;
        }
    }

    public boolean hayDatos() {
        try (Session s = sf().openSession()) {
            Long count = s.createQuery(
                    "SELECT count(a) FROM AdminMuseo a", Long.class
            ).uniqueResult();
            return count != null && count > 0;
        } catch (Exception e) {
            return false;
        }
    }
}