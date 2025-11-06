package com.example.demo.excepciones;

/**
 * Excepción lanzada cuando no se encuentra un artista en la base de datos
 */
public class ArtistaNoEncontradoException extends RecursoNoEncontradoException {

    public ArtistaNoEncontradoException(Long id) {
        super("Artista no encontrado con ID: " + id);
    }

    public ArtistaNoEncontradoException(String nombre) {
        super("Artista no encontrado con nombre: " + nombre);
    }
}

