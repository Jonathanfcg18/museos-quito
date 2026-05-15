package com.webapp.museosquito.service;

import com.webapp.museosquito.model.Museo;
import com.webapp.museosquito.repository.RepositorioMuseo;

import java.util.List;

/**
 * Servicio de lógica de negocio para Museos.
 * HU1: Consultar información de museos.
 */
public class ServicioMuseo {

    private final RepositorioMuseo repositorio;

    public ServicioMuseo() {
        this.repositorio = new RepositorioMuseo();
    }

    // Constructor para tests con inyección
    public ServicioMuseo(RepositorioMuseo repositorio) {
        this.repositorio = repositorio;
    }

    /**
     * Retorna la lista de todos los museos activos.
     * Escenario 1 HU1: La información debe estar actualizada.
     */
    public List<Museo> obtenerTodos() {
        return repositorio.listar();
    }

    /**
     * Retorna el detalle completo de un museo por su ID.
     * Escenario 2 HU1: Navegación a detalles.
     *
     * @param id Identificador del museo
     * @return Museo con horarios y exposiciones cargados, o null si no existe
     */
    public Museo obtenerPorId(int id) {
        if (id <= 0) return null;
        return repositorio.buscarPorId(id);
    }

    /**
     * Filtra museos por categoría.
     */
    public List<Museo> filtrarPorCategoria(String categoria) {
        if (categoria == null || categoria.isBlank()) {
            return repositorio.listar();
        }
        return repositorio.listarPorCategoria(categoria);
    }

    /**
     * Búsqueda de museos por nombre.
     */
    public List<Museo> buscarPorNombre(String termino) {
        if (termino == null || termino.isBlank()) {
            return repositorio.listar();
        }
        return repositorio.buscarPorNombre(termino.trim());
    }
}
