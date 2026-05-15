package com.webapp.museosquito.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa un Museo de Quito.
 * HU1: Consultar información de museos.
 */
@Entity
@Table(name = "museos")
public class Museo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String ubicacion;

    @Column(nullable = false, length = 2000)
    private String descripcion;

    @Column
    private String categoria; // Arte, Historia, Ciencia, Cultura, etc.

    @Column
    private String telefono;

    @Column
    private String sitioWeb;

    @Column
    private String imagenUrl;

    @Column
    private double precioAdulto;

    @Column
    private double precioNino;

    @Column
    private double precioEstudiante;

    @Column
    private boolean activo;

    @OneToMany(mappedBy = "museo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<HorarioMuseo> horarios = new ArrayList<>();

    @OneToMany(mappedBy = "museo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Exposicion> exposiciones = new ArrayList<>();

    public Museo() {}

    public Museo(String nombre, String ubicacion, String descripcion, String categoria,
                 String telefono, String sitioWeb, String imagenUrl,
                 double precioAdulto, double precioNino, double precioEstudiante) {
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.telefono = telefono;
        this.sitioWeb = sitioWeb;
        this.imagenUrl = imagenUrl;
        this.precioAdulto = precioAdulto;
        this.precioNino = precioNino;
        this.precioEstudiante = precioEstudiante;
        this.activo = true;
    }

    // Getters y setters
    public int getId()                                    { return id; }
    public void setId(int id)                             { this.id = id; }
    public String getNombre()                             { return nombre; }
    public void setNombre(String nombre)                  { this.nombre = nombre; }
    public String getUbicacion()                          { return ubicacion; }
    public void setUbicacion(String ubicacion)            { this.ubicacion = ubicacion; }
    public String getDescripcion()                        { return descripcion; }
    public void setDescripcion(String descripcion)        { this.descripcion = descripcion; }
    public String getCategoria()                          { return categoria; }
    public void setCategoria(String categoria)            { this.categoria = categoria; }
    public String getTelefono()                           { return telefono; }
    public void setTelefono(String telefono)              { this.telefono = telefono; }
    public String getSitioWeb()                           { return sitioWeb; }
    public void setSitioWeb(String sitioWeb)              { this.sitioWeb = sitioWeb; }
    public String getImagenUrl()                          { return imagenUrl; }
    public void setImagenUrl(String imagenUrl)            { this.imagenUrl = imagenUrl; }
    public double getPrecioAdulto()                       { return precioAdulto; }
    public void setPrecioAdulto(double precioAdulto)      { this.precioAdulto = precioAdulto; }
    public double getPrecioNino()                         { return precioNino; }
    public void setPrecioNino(double precioNino)          { this.precioNino = precioNino; }
    public double getPrecioEstudiante()                   { return precioEstudiante; }
    public void setPrecioEstudiante(double e)             { this.precioEstudiante = e; }
    public boolean isActivo()                             { return activo; }
    public void setActivo(boolean activo)                 { this.activo = activo; }
    public List<HorarioMuseo> getHorarios()               { return horarios; }
    public void setHorarios(List<HorarioMuseo> h)         { this.horarios = h; }
    public List<Exposicion> getExposiciones()             { return exposiciones; }
    public void setExposiciones(List<Exposicion> e)       { this.exposiciones = e; }
}
