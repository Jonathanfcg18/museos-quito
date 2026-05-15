package com.webapp.museosquito.model;

import jakarta.persistence.*;

/**
 * Entidad que representa un horario de atención de un museo.
 * HU1: Criterio - Cada museo incluye horarios.
 */
@Entity
@Table(name = "horarios_museo")
public class HorarioMuseo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "museo_id", nullable = false)
    private Museo museo;

    @Column(nullable = false)
    private String diaSemana; // "Lunes", "Martes-Viernes", "Sábado", "Domingo"

    @Column(nullable = false)
    private String horaApertura; // "09:00"

    @Column(nullable = false)
    private String horaCierre; // "17:00"

    @Column
    private boolean cerrado; // true si el museo está cerrado ese día

    public HorarioMuseo() {}

    public HorarioMuseo(Museo museo, String diaSemana, String horaApertura,
                        String horaCierre, boolean cerrado) {
        this.museo = museo;
        this.diaSemana = diaSemana;
        this.horaApertura = horaApertura;
        this.horaCierre = horaCierre;
        this.cerrado = cerrado;
    }

    public int getId()                          { return id; }
    public void setId(int id)                   { this.id = id; }
    public Museo getMuseo()                     { return museo; }
    public void setMuseo(Museo museo)           { this.museo = museo; }
    public String getDiaSemana()                { return diaSemana; }
    public void setDiaSemana(String d)          { this.diaSemana = d; }
    public String getHoraApertura()             { return horaApertura; }
    public void setHoraApertura(String h)       { this.horaApertura = h; }
    public String getHoraCierre()               { return horaCierre; }
    public void setHoraCierre(String h)         { this.horaCierre = h; }
    public boolean isCerrado()                  { return cerrado; }
    public void setCerrado(boolean cerrado)     { this.cerrado = cerrado; }

    public String getResumen() {
        if (cerrado) return diaSemana + ": Cerrado";
        return diaSemana + ": " + horaApertura + " - " + horaCierre;
    }
}
