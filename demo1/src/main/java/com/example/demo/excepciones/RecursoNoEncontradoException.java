package com.example.demo.excepciones;

/**
 * Excepción base para cuando un recurso no se encuentra en la base de datos.
 * Clase padre de excepciones específicas como UsuarioNoEncontradoException, CancionNoEncontradaException, etc.
 */
public class RecursoNoEncontradoException extends RuntimeException {

    /**
     * Constructor con mensaje de error.
     *
     * @param mensaje Descripción del error
     */
    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }

    /**
     * Constructor con mensaje de error y causa.
     *
     * @param mensaje Descripción del error
     * @param causa   Causa raíz de la excepción
     */
    public RecursoNoEncontradoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
