package com.example.demo.excepciones;

/**
 * Excepción lanzada cuando se intenta crear un recurso que ya existe en el sistema.
 * Utilizada para evitar duplicados de usuarios, artistas, etc.
 */
public class RecursoDuplicadoException extends RuntimeException {

    /**
     * Constructor con mensaje de error.
     *
     * @param mensaje Descripción del error
     */
    public RecursoDuplicadoException(String mensaje) {
        super(mensaje);
    }

    /**
     * Constructor con mensaje de error y causa.
     *
     * @param mensaje Descripción del error
     * @param causa   Causa raíz de la excepción
     */
    public RecursoDuplicadoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
