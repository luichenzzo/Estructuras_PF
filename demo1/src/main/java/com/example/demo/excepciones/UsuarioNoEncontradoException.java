package com.example.demo.excepciones;

/**
 * Excepción lanzada cuando no se encuentra un usuario en la base de datos.
 * Extiende de RecursoNoEncontradoException para proporcionar mensajes específicos.
 */
public class UsuarioNoEncontradoException extends RecursoNoEncontradoException {

    /**
     * Constructor para búsqueda por ID.
     *
     * @param id ID del usuario no encontrado
     */
    public UsuarioNoEncontradoException(Long id) {
        super("Usuario no encontrado con ID: " + id);
    }

    /**
     * Constructor para búsqueda por correo electrónico.
     *
     * @param correo Correo del usuario no encontrado
     */
    public UsuarioNoEncontradoException(String correo) {
        super("Usuario no encontrado con correo: " + correo);
    }
}
