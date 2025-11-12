package com.example.demo.excepciones;

/**
 * Excepción lanzada cuando no se encuentra una canción en la base de datos.
 * Extiende de RecursoNoEncontradoException para proporcionar mensajes específicos.
 */
public class CancionNoEncontradaException extends RecursoNoEncontradoException {

    /**
     * Constructor para búsqueda por ID.
     *
     * @param id ID de la canción no encontrada
     */
    public CancionNoEncontradaException(Long id) {
        super("Canción no encontrada con ID: " + id);
    }

    /**
     * Constructor para búsqueda por título.
     *
     * @param titulo Título de la canción no encontrada
     */
    public CancionNoEncontradaException(String titulo) {
        super("Canción no encontrada con título: " + titulo);
    }
}
