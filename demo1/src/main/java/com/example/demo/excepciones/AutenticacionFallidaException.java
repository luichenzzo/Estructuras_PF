package com.example.demo.excepciones;

/**
 * Excepción lanzada cuando falla la autenticación de un usuario.
 * Se usa cuando las credenciales (correo/contraseña) son incorrectas.
 */
public class AutenticacionFallidaException extends RuntimeException {

    /**
     * Constructor con mensaje de error.
     *
     * @param mensaje Descripción del error de autenticación
     */
    public AutenticacionFallidaException(String mensaje) {
        super(mensaje);
    }

    /**
     * Constructor con mensaje de error y causa.
     *
     * @param mensaje Descripción del error de autenticación
     * @param causa   Causa raíz del error
     */
    public AutenticacionFallidaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}

