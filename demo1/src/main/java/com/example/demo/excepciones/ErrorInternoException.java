package com.example.demo.excepciones;

/**
 * Excepción para errores internos del servidor.
 * Utilizada cuando ocurre un error inesperado en la lógica de negocio o infraestructura.
 */
public class ErrorInternoException extends RuntimeException {

    /**
     * Constructor con mensaje de error.
     *
     * @param mensaje Descripción del error interno
     */
    public ErrorInternoException(String mensaje) {
        super(mensaje);
    }

    /**
     * Constructor con mensaje de error y causa.
     *
     * @param mensaje Descripción del error interno
     * @param causa   Causa raíz de la excepción
     */
    public ErrorInternoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
