package com.webapp.museosquito.model;

import jakarta.persistence.*;

/**
 * Entidad que representa a cualquier usuario del sistema.
 * Roles: VISITANTE, ADMIN_MUSEO, ADMIN_SISTEMA.
 *
 * Sprint 2 – HU-01: Registro (rol VISITANTE).
 *            HU-02: Login/Logout (todos los roles).
 *
 * Responsable Backend: Jonathan Cuasapaz (HU-01)
 *                      Jhonny Moreira (HU-02)
 */
@Entity
@Table(name = "usuarios")
public class Usuario {

    public enum Rol {
        VISITANTE, ADMIN_MUSEO, ADMIN_SISTEMA
    }

    public enum Estado {
        ACTIVO, SUSPENDIDO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 100)
    private String nombre;

    /** Identificador único del usuario. Case-insensitive en las búsquedas. */
    @Column(nullable = false, unique = true, length = 150)
    private String email;

    /** Hash BCrypt de la contraseña. Nunca almacenar texto plano. */
    @Column(nullable = false, length = 60)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Estado estado;

    @Column(nullable = false)
    private String creadoEn;

    public Usuario() {}

    // ── Getters y Setters ─────────────────────────────────────────────────

    public int getId()                       { return id; }
    public void setId(int id)                { this.id = id; }

    public String getNombre()                { return nombre; }
    public void setNombre(String nombre)     { this.nombre = nombre; }

    public String getEmail()                 { return email; }
    public void setEmail(String email)       { this.email = email; }

    public String getPasswordHash()          { return passwordHash; }
    public void setPasswordHash(String h)    { this.passwordHash = h; }

    public Rol getRol()                      { return rol; }
    public void setRol(Rol rol)              { this.rol = rol; }

    public Estado getEstado()                { return estado; }
    public void setEstado(Estado estado)     { this.estado = estado; }

    public String getCreadoEn()              { return creadoEn; }
    public void setCreadoEn(String c)        { this.creadoEn = c; }

    // ── Helpers de rol ────────────────────────────────────────────────────

    public boolean isVisitante()    { return Rol.VISITANTE.equals(rol); }
    public boolean isAdminMuseo()   { return Rol.ADMIN_MUSEO.equals(rol); }
    public boolean isAdminSistema() { return Rol.ADMIN_SISTEMA.equals(rol); }
    public boolean isActivo()       { return Estado.ACTIVO.equals(estado); }
}