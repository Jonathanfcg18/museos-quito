package com.webapp.museosquito.service;

import com.webapp.museosquito.model.FranjaReserva;
import com.webapp.museosquito.model.Reserva;
import com.webapp.museosquito.repository.RepositorioFranjaReserva;
import com.webapp.museosquito.repository.RepositorioReserva;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Lógica de negocio para reservas.
 * HU2: Reservar una visita.
 * HU-10: Envío de correo de confirmación tras reserva exitosa.
 * HU-11: Envío de correo de cancelación tras cancelar una reserva.
 */
public class ServicioReserva {

    private static final Logger LOGGER = Logger.getLogger(ServicioReserva.class.getName());

    private final RepositorioFranjaReserva repoFranja;
    private final RepositorioReserva repoReserva;
    private final CorreoService correoService;

    public ServicioReserva() {
        this.repoFranja = new RepositorioFranjaReserva();
        this.repoReserva = new RepositorioReserva();
        this.correoService = new CorreoService();
    }

    // Constructor para tests con inyección (usa CorreoService real por defecto)
    public ServicioReserva(RepositorioFranjaReserva repoFranja,
            RepositorioReserva repoReserva) {
        this(repoFranja, repoReserva, new CorreoService());
    }

    // Constructor para tests con inyección completa (permite mockear CorreoService
    // y así no depender de un servidor SMTP real durante las pruebas — HU-10 / HU-11)
    public ServicioReserva(RepositorioFranjaReserva repoFranja,
            RepositorioReserva repoReserva, CorreoService correoService) {
        this.repoFranja = repoFranja;
        this.repoReserva = repoReserva;
        this.correoService = correoService;
    }

    /**
     * GET /museos/{id}/horarios
     * Retorna todas las franjas de un museo (con y sin cupos, para mostrar
     * agotadas).
     */
    public List<FranjaReserva> obtenerFranjasPorMuseo(int museoId) {
        return repoFranja.listarPorMuseo(museoId);
    }

    /**
     * POST /reservas — registra una reserva validando disponibilidad de cupos.
     *
     * Escenario 1 HU2: reserva exitosa cuando hay cupos.
     * Escenario 2 HU2: lanza excepción cuando no hay cupos (sistema impide
     * avanzar).
     *
     * @throws IllegalStateException    si no hay cupos disponibles
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
                cantidad, LocalDate.now().toString(), codigo);

        Reserva reservaGuardada = repoReserva.guardar(reserva);

        // HU-10 Escenario 1 y 3: el correo se envía solo si la reserva quedó
        // registrada. El envío real ocurre en segundo plano (ver CorreoService),
        // así que este try-catch solo captura errores de configuración/validación
        // inmediatos; un fallo del SMTP en sí se registra en el log desde el
        // propio hilo en segundo plano y nunca afecta esta respuesta.
        try {
            correoService.enviarConfirmacionReserva(reservaGuardada);
            LOGGER.info("Envío de correo de confirmación encolado para "
                    + reservaGuardada.getEmailVisitante() + " (reserva "
                    + reservaGuardada.getCodigoConfirmacion() + ")");
        } catch (Exception e) {
            LOGGER.log(Level.WARNING,
                    "No se pudo encolar el correo de confirmación para la reserva "
                            + reservaGuardada.getCodigoConfirmacion() + ": " + e.getMessage(),
                    e);
        }

        return reservaGuardada;
    }

    public FranjaReserva obtenerFranjaPorId(int id) {
        return repoFranja.buscarPorId(id);
    }

    /**
     * GET /reservas/usuario — lista reservas activas de un visitante por email.
     * HU3: el visitante puede visualizar sus reservas.
     */
    public List<Reserva> obtenerReservasPorEmail(String email) {
        if (email == null || email.isBlank())
            return Collections.emptyList();
        return repoReserva.listarActivasPorEmail(email.trim().toLowerCase());
    }

    /**
     * GET /reservas/mis-reservas — lista TODAS las reservas (activas y canceladas)
     * de un visitante por email. Usado por HU-07 y HU-09.
     */
    public List<Reserva> obtenerTodasReservasPorEmail(String email) {
        if (email == null || email.isBlank())
            return Collections.emptyList();
        return repoReserva.listarTodasPorEmail(email.trim().toLowerCase());
    }

    /**
     * DELETE /reservas/{id} — cancela una reserva y libera el cupo.
     * HU3 Escenario 1: cancelación exitosa.
     *
     * @throws IllegalArgumentException si la reserva no existe o no pertenece al
     *                                  email
     * @throws IllegalStateException    si la reserva ya está cancelada
     */
    public void cancelarReserva(int reservaId, String email) {
        Reserva reserva = repoReserva.buscarPorId(reservaId);

        if (reserva == null)
            throw new IllegalArgumentException("La reserva no existe.");
        if (!reserva.getEmailVisitante().equalsIgnoreCase(email.trim()))
            throw new IllegalArgumentException("No tienes permiso para cancelar esta reserva.");
        if (!reserva.isActiva())
            throw new IllegalStateException("Esta reserva ya fue cancelada.");

        // Liberar cupo al museo
        FranjaReserva franja = repoFranja.buscarPorId(reserva.getFranja().getId());
        int nuevoOcupado = Math.max(0, franja.getAforoOcupado() - reserva.getCantidadPersonas());
        franja.setAforoOcupado(nuevoOcupado);
        repoFranja.actualizar(franja);

        // Marcar como cancelada
        reserva.setActiva(false);
        Reserva reservaCancelada = repoReserva.actualizar(reserva);

        // HU-11 Escenario 1: el correo se envía solo si la cancelación quedó
        // registrada. El envío real ocurre en segundo plano (ver CorreoService).
        try {
            correoService.enviarCancelacionReserva(reservaCancelada);
            LOGGER.info("Envío de correo de cancelación encolado para "
                    + reservaCancelada.getEmailVisitante() + " (reserva "
                    + reservaCancelada.getCodigoConfirmacion() + ")");
        } catch (Exception e) {
            LOGGER.log(Level.WARNING,
                    "No se pudo encolar el correo de cancelación para la reserva "
                            + reservaCancelada.getCodigoConfirmacion() + ": " + e.getMessage(),
                    e);
        }
    }

    /**
     * PUT /reservas/{id} — modifica fecha/horario de una reserva activa.
     * HU3 Escenario 2: libera cupo original y descuenta del nuevo horario.
     *
     * @throws IllegalArgumentException si los datos no son válidos
     * @throws IllegalStateException    si no hay cupos en el nuevo horario
     */
    public Reserva modificarReserva(int reservaId, String email, int nuevaFranjaId) {
        Reserva reserva = repoReserva.buscarPorId(reservaId);

        if (reserva == null)
            throw new IllegalArgumentException("La reserva no existe.");
        if (!reserva.getEmailVisitante().equalsIgnoreCase(email.trim()))
            throw new IllegalArgumentException("No tienes permiso para modificar esta reserva.");
        if (!reserva.isActiva())
            throw new IllegalStateException("No puedes modificar una reserva cancelada.");

        FranjaReserva franjaOriginal = repoFranja.buscarPorId(reserva.getFranja().getId());
        FranjaReserva franjaNueva = repoFranja.buscarPorId(nuevaFranjaId);

        if (franjaNueva == null)
            throw new IllegalArgumentException("El nuevo horario seleccionado no existe.");
        if (franjaOriginal.getId() == franjaNueva.getId())
            throw new IllegalArgumentException("El nuevo horario debe ser diferente al actual.");
        if (!franjaNueva.hayCupos())
            throw new IllegalStateException(
                    "El horario seleccionado ya no tiene cupos disponibles. Elige otra opción.");
        if (reserva.getCantidadPersonas() > franjaNueva.getCuposDisponibles())
            throw new IllegalStateException(
                    "No hay suficientes cupos en el nuevo horario para " +
                            reserva.getCantidadPersonas() + " persona(s).");

        // Liberar cupo original
        int ocupadoOriginal = Math.max(0,
                franjaOriginal.getAforoOcupado() - reserva.getCantidadPersonas());
        franjaOriginal.setAforoOcupado(ocupadoOriginal);
        repoFranja.actualizar(franjaOriginal);

        // Descontar cupo del nuevo horario
        franjaNueva.setAforoOcupado(franjaNueva.getAforoOcupado() + reserva.getCantidadPersonas());
        repoFranja.actualizar(franjaNueva);

        // Actualizar la reserva con la nueva franja
        reserva.setFranja(franjaNueva);
        return repoReserva.actualizar(reserva);
    }

    public Reserva obtenerReservaPorId(int id) {
        return repoReserva.buscarPorId(id);
    }
}