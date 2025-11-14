package com.example.demo.exceptions;

public class ErrorInternoException extends RuntimeException {

    public ErrorInternoException(String mensaje) {
        super(mensaje);
    }

    public ErrorInternoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
