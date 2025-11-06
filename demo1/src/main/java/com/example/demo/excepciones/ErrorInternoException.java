package com.example.demo.excepciones;

/**
 * Excepción para errores internos del servidor
 */
public class ErrorInternoException extends RuntimeException {

    public ErrorInternoException(String mensaje) {
        super(mensaje);
    }

    public ErrorInternoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}

