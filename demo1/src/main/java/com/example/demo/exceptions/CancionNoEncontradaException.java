package com.example.demo.exceptions;

public class CancionNoEncontradaException extends RecursoNoEncontradoException {

    public CancionNoEncontradaException(Long id) {
        super("Canción no encontrada con ID: " + id);
    }

    public CancionNoEncontradaException(String titulo) {
        super("Canción no encontrada con título: " + titulo);
    }
}
