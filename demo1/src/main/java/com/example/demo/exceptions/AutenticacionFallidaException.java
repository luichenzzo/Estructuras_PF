package com.example.demo.exceptions;

public class AutenticacionFallidaException extends RuntimeException {

    public AutenticacionFallidaException(String mensaje) {
        super(mensaje);
    }

    public AutenticacionFallidaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
