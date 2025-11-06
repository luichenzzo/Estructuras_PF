package com.example.demo.excepciones;

/**
 * Excepción base para cuando un recurso no se encuentra en la base de datos
 */
public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }

    public RecursoNoEncontradoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}

