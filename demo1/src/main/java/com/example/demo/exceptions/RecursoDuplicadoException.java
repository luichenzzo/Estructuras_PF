package com.example.demo.exceptions;

public class RecursoDuplicadoException extends RuntimeException {

    public RecursoDuplicadoException(String mensaje) {
        super(mensaje);
    }

    public RecursoDuplicadoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
