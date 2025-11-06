package com.example.demo.excepciones;

/**
 * Excepción lanzada cuando los datos proporcionados son inválidos
 */
public class DatosInvalidosException extends RuntimeException {

    public DatosInvalidosException(String mensaje) {
        super(mensaje);
    }

    public DatosInvalidosException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}

