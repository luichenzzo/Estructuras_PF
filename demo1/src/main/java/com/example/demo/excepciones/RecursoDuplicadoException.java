package com.example.demo.excepciones;

/**
 * Excepción lanzada cuando se intenta crear un recurso que ya existe
 */
public class RecursoDuplicadoException extends RuntimeException {

    public RecursoDuplicadoException(String mensaje) {
        super(mensaje);
    }

    public RecursoDuplicadoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}

