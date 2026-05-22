package com.webapp.museosquito.repository;

import com.webapp.museosquito.model.Usuario;
import com.webapp.museosquito.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * Repositorio de acceso a datos para la entidad Usuario.
 *
 * Sprint 2 – HU-01 T-01.3: Validar unicidad del correo.
 *            HU-01 T-01.5: Guardar nuevo visitante.
 *            HU-02 T-02.2: Buscar usuario por email para autenticación.
 *
 * Responsable Backend: Jonathan Cuasapaz (HU-01)
 *                      Jhonny Moreira (HU-02 usa buscarPorEmail)
 */
public class RepositorioUsuario {

    private SessionFactory sf() {
        return HibernateUtil.getSessionFactory();
    }

    /**
     * Persiste un nuevo usuario en la base de datos.
     */
    public Usuario guardar(Usuario u) {
        try (Session s = sf().openSession()) {
            s.beginTransaction();
            s.persist(u);
            s.getTransaction().commit();
            return u;
        }
    }

    /**
     * Actualiza un usuario existente.
     */
    public Usuario actualizar(Usuario u) {
        try (Session s = sf().openSession()) {
            s.beginTransaction();
            Usuario merged = s.merge(u);
            s.getTransaction().commit();
            return merged;
        }
    }

    /**
     * Busca un usuario por email (case-insensitive).
     * Usado en el login (HU-02) y en la validación de unicidad (HU-01).
     *
     * @return el Usuario si existe, null si no.
     */
    public Usuario buscarPorEmail(String email) {
        try (Session s = sf().openSession()) {
            return s.createQuery(
                    "FROM Usuario u WHERE lower(u.email) = lower(:email)",
                    Usuario.class)
                    .setParameter("email", email.trim())
                    .uniqueResult();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Verifica si un email ya está registrado en la tabla usuarios.
     * Usado por HU-01 Escenario 2: bloquear registro con correo duplicado.
     */
    public boolean existeEmail(String email) {
        try (Session s = sf().openSession()) {
            Long count = s.createQuery(
                    "SELECT count(u) FROM Usuario u WHERE lower(u.email) = lower(:email)",
                    Long.class)
                    .setParameter("email", email.trim())
                    .uniqueResult();
            return count != null && count > 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verifica si ya existe algún admin del sistema (para DataSeeder).
     */
    public boolean hayAdminSistema() {
        try (Session s = sf().openSession()) {
            Long count = s.createQuery(
                    "SELECT count(u) FROM Usuario u WHERE u.rol = 'ADMIN_SISTEMA'",
                    Long.class).uniqueResult();
            return count != null && count > 0;
        } catch (Exception e) {
            return false;
        }
    }
}