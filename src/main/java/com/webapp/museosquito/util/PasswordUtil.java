package com.webapp.museosquito.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utilidad para hashing y verificación de contraseñas con BCrypt.
 *
 * Sprint 2 – HU-01 T-01.4: Encriptación de contraseña antes de persistir.
 *            HU-02 T-02.2: Validación de credenciales en el login.
 *
 * Responsable: Jonathan Cuasapaz (HU-01)
 */
public class PasswordUtil {

    private PasswordUtil() {}

    /**
     * Genera el hash BCrypt de una contraseña en texto plano.
     * Costo 12 (balance entre seguridad y rendimiento).
     */
    public static String hashear(String passwordPlano) {
        return BCrypt.hashpw(passwordPlano, BCrypt.gensalt(12));
    }

    /**
     * Verifica si una contraseña en texto plano coincide con su hash BCrypt.
     *
     * @param passwordPlano  contraseña ingresada por el usuario
     * @param hash           hash almacenado en la base de datos
     * @return true si coinciden
     */
    public static boolean verificar(String passwordPlano, String hash) {
        if (passwordPlano == null || hash == null || hash.isBlank()) return false;
        try {
            return BCrypt.checkpw(passwordPlano, hash);
        } catch (Exception e) {
            return false;
        }
    }
}