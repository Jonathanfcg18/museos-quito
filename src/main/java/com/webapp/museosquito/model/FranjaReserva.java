package com.webapp.museosquito.model;

import jakarta.persistence.*;

/**
 * Entidad que representa una franja horaria reservable para un museo en una fecha concreta.
 * HU2: Backend - endpoint GET /museos/{id}/horarios
 */
@Entity
@Table(name = "franjas_reserva")
public class FranjaReserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "museo_id", nullable = false)
    private Museo museo;

    @Column(nullable = false)
    private String fecha;         // "2026-06-15"

    @Column(nullable = false)
    private String horaInicio;    // "09:00"

    @Column(nullable = false)
    private String horaFin;       // "10:00"

    @Column(nullable = false)
    private int aforoMaximo;

    @Column(nullable = false)
    private int aforoOcupado;

    public FranjaReserva() {}

    public FranjaReserva(Museo museo, String fecha, String horaInicio,
                         String horaFin, int aforoMaximo) {
        this.museo        = museo;
        this.fecha        = fecha;
        this.horaInicio   = horaInicio;
        this.horaFin      = horaFin;
        this.aforoMaximo  = aforoMaximo;
        this.aforoOcupado = 0;
    }

    public boolean hayCupos() {
        return aforoOcupado < aforoMaximo;
    }

    public int getCuposDisponibles() {
        return aforoMaximo - aforoOcupado;
    }

    public int getId()                              { return id; }
    public void setId(int id)                       { this.id = id; }
    public Museo getMuseo()                         { return museo; }
    public void setMuseo(Museo museo)               { this.museo = museo; }
    public String getFecha()                        { return fecha; }
    public void setFecha(String fecha)              { this.fecha = fecha; }
    public String getHoraInicio()                   { return horaInicio; }
    public void setHoraInicio(String h)             { this.horaInicio = h; }
    public String getHoraFin()                      { return horaFin; }
    public void setHoraFin(String h)                { this.horaFin = h; }
    public int getAforoMaximo()                     { return aforoMaximo; }
    public void setAforoMaximo(int a)               { this.aforoMaximo = a; }
    public int getAforoOcupado()                    { return aforoOcupado; }
    public void setAforoOcupado(int a)              { this.aforoOcupado = a; }
}