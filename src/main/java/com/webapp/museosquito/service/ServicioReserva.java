package com.webapp.museosquito.service;

import com.webapp.museosquito.model.FranjaReserva;
import com.webapp.museosquito.model.Reserva;
import com.webapp.museosquito.repository.RepositorioFranjaReserva;
import com.webapp.museosquito.repository.RepositorioReserva;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Lógica de negocio para reservas.
 * HU2: Reservar una visita.
 */
public class ServicioReserva {

    private final RepositorioFranjaReserva repoFranja;
    private final RepositorioReserva       repoReserva;

    public ServicioReserva() {
        this.repoFranja  = new RepositorioFranjaReserva();
        this.repoReserva = new RepositorioReserva();
    }

    // Constructor para tests con inyección
    public ServicioReserva(RepositorioFranjaReserva repoFranja,
                           RepositorioReserva repoReserva) {
        this.repoFranja  = repoFranja;
        this.repoReserva = repoReserva;
    }

    /**
     * GET /museos/{id}/horarios
     * Retorna todas las franjas de un museo (con y sin cupos, para mostrar agotadas).
     */
    public List<FranjaReserva> obtenerFranjasPorMuseo(int museoId) {
        return repoFranja.listarPorMuseo(museoId);
    }

    /**
     * POST /reservas — registra una reserva validando disponibilidad de cupos.
     *
     * Escenario 1 HU2: reserva exitosa cuando hay cupos.
     * Escenario 2 HU2: lanza excepción cuando no hay cupos (sistema impide avanzar).
     *
     * @throws IllegalStateException si no hay cupos disponibles
     * @throws IllegalArgumentException si los datos son inválidos
     */
    public Reserva crearReserva(int franjaId, String nombre, String email,
                                int cantidad) {
        // Validaciones de datos
        if (nombre == null || nombre.isBlank())
            throw new IllegalArgumentException("El nombre del visitante es obligatorio.");
        if (email == null || email.isBlank() || !email.contains("@"))
            throw new IllegalArgumentException("El email ingresado no es válido.");
        if (cantidad <= 0)
            throw new IllegalArgumentException("La cantidad de personas debe ser mayor a cero.");

        // Verificar existencia de franja
        FranjaReserva franja = repoFranja.buscarPorId(franjaId);
        if (franja == null)
            throw new IllegalArgumentException("El horario seleccionado no existe.");

        // Escenario 2 HU2: validación de aforo — sistema bloquea cuando no hay cupos
        if (!franja.hayCupos())
            throw new IllegalStateException(
                "El horario seleccionado ya no tiene cupos disponibles. " +
                "Por favor elige otra opción.");

        if (cantidad > franja.getCuposDisponibles())
            throw new IllegalStateException(
                "No hay suficientes cupos para " + cantidad + " persona(s). " +
                "Solo quedan " + franja.getCuposDisponibles() + " cupo(s).");

        // Escenario 1 HU2: registrar reserva y actualizar aforo
        franja.setAforoOcupado(franja.getAforoOcupado() + cantidad);
        repoFranja.actualizar(franja);

        String codigo = "RES-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Reserva reserva = new Reserva(
                franja, nombre.trim(), email.trim().toLowerCase(),
                cantidad, LocalDate.now().toString(), codigo
        );

        return repoReserva.guardar(reserva);
    }
    public FranjaReserva obtenerFranjaPorId(int id) {
        return repoFranja.buscarPorId(id);
    }
}