package com.webapp.museosquito.model;

import jakarta.persistence.*;

/**
 * Entidad que representa al administrador de un museo.
 * HU4: Gestionar horarios y aforo.
 */
@Entity
@Table(name = "admins_museo")
public class AdminMuseo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "museo_id", nullable = false)
    private Museo museo;

    public AdminMuseo() {}

    public AdminMuseo(String nombre, String email, String password, Museo museo) {
        this.nombre   = nombre;
        this.email    = email;
        this.password = password;
        this.museo    = museo;
    }

    public int getId()                      { return id; }
    public void setId(int id)               { this.id = id; }
    public String getNombre()               { return nombre; }
    public void setNombre(String n)         { this.nombre = n; }
    public String getEmail()                { return email; }
    public void setEmail(String e)          { this.email = e; }
    public String getPassword()             { return password; }
    public void setPassword(String p)       { this.password = p; }
    public Museo getMuseo()                 { return museo; }
    public void setMuseo(Museo m)           { this.museo = m; }
}