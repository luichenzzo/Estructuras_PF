package com.example.demo.exceptions;

public class ArtistaNoEncontradoException extends RecursoNoEncontradoException {

    public ArtistaNoEncontradoException(Long id) {
        super("Artista no encontrado con ID: " + id);
    }

    public ArtistaNoEncontradoException(String nombre) {
        super("Artista no encontrado con nombre: " + nombre);
    }
}
