package com.webapp.museosquito.service;

import com.webapp.museosquito.model.AdminMuseo;
import com.webapp.museosquito.model.FranjaReserva;
import com.webapp.museosquito.model.Museo;
import com.webapp.museosquito.repository.RepositorioAdminMuseo;
import com.webapp.museosquito.repository.RepositorioFranjaReserva;
import com.webapp.museosquito.repository.RepositorioMuseo;

import java.util.List;

/**
 * Servicio de lógica de negocio para el administrador del museo.
 * HU4: Gestionar horarios y aforo.
 */
public class ServicioAdmin {

    private final RepositorioAdminMuseo  repoAdmin;
    private final RepositorioFranjaReserva repoFranja;
    private final RepositorioMuseo       repoMuseo;

    public ServicioAdmin() {
        this.repoAdmin  = new RepositorioAdminMuseo();
        this.repoFranja = new RepositorioFranjaReserva();
        this.repoMuseo  = new RepositorioMuseo();
    }

    // Constructor para tests
    public ServicioAdmin(RepositorioAdminMuseo repoAdmin,
                         RepositorioFranjaReserva repoFranja,
                         RepositorioMuseo repoMuseo) {
        this.repoAdmin  = repoAdmin;
        this.repoFranja = repoFranja;
        this.repoMuseo  = repoMuseo;
    }

    /**
     * Autentica al administrador del museo.
     */
    public AdminMuseo autenticar(String email, String password) {
        if (email == null || password == null) return null;
        AdminMuseo admin = repoAdmin.buscarPorEmail(email.trim().toLowerCase());
        if (admin == null) return null;
        if (!admin.getPassword().equals(password)) return null;
        return admin;
    }

    /**
     * Retorna todas las franjas del museo del administrador.
     */
    public List<FranjaReserva> obtenerFranjasPorMuseo(int museoId) {
        return repoFranja.listarPorMuseo(museoId);
    }

    /**
     * POST /horarios — crea una nueva franja con aforo obligatorio.
     * HU4 Escenario 1: valida que el aforo máximo esté definido.
     *
     * @throws IllegalArgumentException si faltan datos o el aforo es inválido
     */
    public FranjaReserva crearFranja(int museoId, String fecha,
                                     String horaInicio, String horaFin,
                                     int aforoMaximo) {
        // HU4 Escenario 1: validación obligatoria de aforo
        if (fecha == null || fecha.isBlank())
            throw new IllegalArgumentException("La fecha es obligatoria.");
        if (horaInicio == null || horaInicio.isBlank())
            throw new IllegalArgumentException("La hora de inicio es obligatoria.");
        if (horaFin == null || horaFin.isBlank())
            throw new IllegalArgumentException("La hora de fin es obligatoria.");
        if (aforoMaximo <= 0)
            throw new IllegalArgumentException(
                "Debe definir un aforo máximo válido (mayor a cero). " +
                "Este campo es obligatorio.");

        Museo museo = repoMuseo.buscarPorId(museoId);
        if (museo == null)
            throw new IllegalArgumentException("El museo no existe.");

        FranjaReserva franja = new FranjaReserva(
                museo, fecha.trim(), horaInicio.trim(),
                horaFin.trim(), aforoMaximo);
        return repoFranja.guardar(franja);
    }

    /**
     * PUT /horarios/{id} — modifica una franja existente.
     * HU4 Escenario 1: valida aforo máximo obligatorio.
     * HU4 Escenario 2: el nuevo aforo no puede ser menor al aforo ocupado actual.
     *
     * @throws IllegalArgumentException si los datos no son válidos
     * @throws IllegalStateException si el nuevo aforo genera sobrecupo
     */
    public FranjaReserva modificarFranja(int franjaId, String fecha,
                                          String horaInicio, String horaFin,
                                          int aforoMaximo) {
        if (aforoMaximo <= 0)
            throw new IllegalArgumentException(
                "Debe definir un aforo máximo válido (mayor a cero). " +
                "Este campo es obligatorio.");

        FranjaReserva franja = repoFranja.buscarPorId(franjaId);
        if (franja == null)
            throw new IllegalArgumentException("La franja horaria no existe.");

        // HU4 Escenario 2: no permitir aforo menor al ocupado actual
        if (aforoMaximo < franja.getAforoOcupado())
            throw new IllegalStateException(
                "El aforo máximo no puede ser menor al número de reservas " +
                "actuales (" + franja.getAforoOcupado() + ").");

        franja.setFecha(fecha.trim());
        franja.setHoraInicio(horaInicio.trim());
        franja.setHoraFin(horaFin.trim());
        franja.setAforoMaximo(aforoMaximo);
        return repoFranja.actualizar(franja);
    }

    /**
     * Elimina una franja solo si no tiene reservas activas.
     */
    public void eliminarFranja(int franjaId) {
        FranjaReserva franja = repoFranja.buscarPorId(franjaId);
        if (franja == null)
            throw new IllegalArgumentException("La franja no existe.");
        if (franja.getAforoOcupado() > 0)
            throw new IllegalStateException(
                "No se puede eliminar una franja con reservas activas.");
        repoFranja.eliminar(franjaId);
    }
}