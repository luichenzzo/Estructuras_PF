package com.example.demo.exceptions;

public class UsuarioNoEncontradoException extends RecursoNoEncontradoException {

    public UsuarioNoEncontradoException(Long id) {
        super("Usuario no encontrado con ID: " + id);
    }

    public UsuarioNoEncontradoException(String correo) {
        super("Usuario no encontrado con correo: " + correo);
    }
}
