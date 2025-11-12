package com.example.demo.excepciones;

/**
 * Excepción lanzada cuando los datos proporcionados no son válidos.
 * Utilizada para validaciones de campos obligatorios, formatos incorrectos, etc.
 */
public class DatosInvalidosException extends RuntimeException {

    /**
     * Constructor con mensaje de error.
     *
     * @param mensaje Descripción del error de validación
     */
    public DatosInvalidosException(String mensaje) {
        super(mensaje);
    }

    /**
     * Constructor con mensaje de error y causa.
     *
     * @param mensaje Descripción del error de validación
     * @param causa   Causa raíz de la excepción
     */
    public DatosInvalidosException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
