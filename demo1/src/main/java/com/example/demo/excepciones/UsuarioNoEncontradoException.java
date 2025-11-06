package com.example.demo.excepciones;

/**
 * Excepción lanzada cuando no se encuentra un usuario en la base de datos
 */
public class UsuarioNoEncontradoException extends RecursoNoEncontradoException {

    public UsuarioNoEncontradoException(Long id) {
        super("Usuario no encontrado con ID: " + id);
    }

    public UsuarioNoEncontradoException(String correo) {
        super("Usuario no encontrado con correo: " + correo);
    }
}

