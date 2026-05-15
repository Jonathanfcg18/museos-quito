package com.webapp.museosquito.model;

import jakarta.persistence.*;

/**
 * Entidad que representa una exposición en un museo.
 * HU1: Criterio - Visualizar exposiciones.
 */
@Entity
@Table(name = "exposiciones")
public class Exposicion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "museo_id", nullable = false)
    private Museo museo;

    @Column(nullable = false)
    private String titulo;

    @Column(length = 1000)
    private String descripcion;

    @Column
    private String tipo; // "Permanente" o "Temporal"

    @Column
    private String fechaInicio; // "2025-01-01"

    @Column
    private String fechaFin;   // "2025-12-31" o null si es permanente

    public Exposicion() {}

    public Exposicion(Museo museo, String titulo, String descripcion,
                      String tipo, String fechaInicio, String fechaFin) {
        this.museo = museo;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public int getId()                          { return id; }
    public void setId(int id)                   { this.id = id; }
    public Museo getMuseo()                     { return museo; }
    public void setMuseo(Museo museo)           { this.museo = museo; }
    public String getTitulo()                   { return titulo; }
    public void setTitulo(String titulo)        { this.titulo = titulo; }
    public String getDescripcion()              { return descripcion; }
    public void setDescripcion(String d)        { this.descripcion = d; }
    public String getTipo()                     { return tipo; }
    public void setTipo(String tipo)            { this.tipo = tipo; }
    public String getFechaInicio()              { return fechaInicio; }
    public void setFechaInicio(String f)        { this.fechaInicio = f; }
    public String getFechaFin()                 { return fechaFin; }
    public void setFechaFin(String f)           { this.fechaFin = f; }
}
