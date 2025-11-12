package com.example.demo.excepciones;

/**
 * Excepción lanzada cuando no se encuentra un artista en la base de datos.
 * Extiende de RecursoNoEncontradoException para proporcionar mensajes específicos.
 */
public class ArtistaNoEncontradoException extends RecursoNoEncontradoException {

    /**
     * Constructor para búsqueda por ID.
     *
     * @param id ID del artista no encontrado
     */
    public ArtistaNoEncontradoException(Long id) {
        super("Artista no encontrado con ID: " + id);
    }

    /**
     * Constructor para búsqueda por nombre.
     *
     * @param nombre Nombre del artista no encontrado
     */
    public ArtistaNoEncontradoException(String nombre) {
        super("Artista no encontrado con nombre: " + nombre);
    }
}
