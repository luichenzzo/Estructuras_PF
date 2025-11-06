package com.example.demo.excepciones;

/**
 * Excepción lanzada cuando no se encuentra una canción en la base de datos
 */
public class CancionNoEncontradaException extends RecursoNoEncontradoException {

    public CancionNoEncontradaException(Long id) {
        super("Canción no encontrada con ID: " + id);
    }

    public CancionNoEncontradaException(String titulo) {
        super("Canción no encontrada con título: " + titulo);
    }
}

