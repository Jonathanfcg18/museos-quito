package com.webapp.museosquito.model;

import jakarta.persistence.*;

/**
 * Entidad que representa una reserva registrada por un visitante.
 * HU2: Backend - endpoint POST /reservas
 */
@Entity
@Table(name = "reservas")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "franja_id", nullable = false)
    private FranjaReserva franja;

    @Column(nullable = false)
    private String nombreVisitante;

    @Column(nullable = false)
    private String emailVisitante;

    @Column(nullable = false)
    private int cantidadPersonas;

    @Column(nullable = false)
    private String fechaRegistro; // "2026-05-15"

    @Column(nullable = false)
    private String codigoConfirmacion;

    @Column(nullable = false)
    private boolean activa;

    public Reserva() {
    }

    public Reserva(FranjaReserva franja, String nombreVisitante,
            String emailVisitante, int cantidadPersonas,
            String fechaRegistro, String codigoConfirmacion) {
        this.franja = franja;
        this.nombreVisitante = nombreVisitante;
        this.emailVisitante = emailVisitante;
        this.cantidadPersonas = cantidadPersonas;
        this.fechaRegistro = fechaRegistro;
        this.codigoConfirmacion = codigoConfirmacion;
        this.activa = true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public FranjaReserva getFranja() {
        return franja;
    }

    public void setFranja(FranjaReserva f) {
        this.franja = f;
    }

    public String getNombreVisitante() {
        return nombreVisitante;
    }

    public void setNombreVisitante(String n) {
        this.nombreVisitante = n;
    }

    public String getEmailVisitante() {
        return emailVisitante;
    }

    public void setEmailVisitante(String e) {
        this.emailVisitante = e;
    }

    public int getCantidadPersonas() {
        return cantidadPersonas;
    }

    public void setCantidadPersonas(int c) {
        this.cantidadPersonas = c;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String f) {
        this.fechaRegistro = f;
    }

    public String getCodigoConfirmacion() {
        return codigoConfirmacion;
    }

    public void setCodigoConfirmacion(String c) {
        this.codigoConfirmacion = c;
    }

    public boolean isActiva()              { return activa; }
    public void setActiva(boolean activa)  { this.activa = activa; }
}