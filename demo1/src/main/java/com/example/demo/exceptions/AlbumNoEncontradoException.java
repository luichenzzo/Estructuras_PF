package com.example.demo.exceptions;

public class AlbumNoEncontradoException extends RecursoNoEncontradoException {

    public AlbumNoEncontradoException(Long id) {
        super("Álbum no encontrado con ID: " + id);
    }

    public AlbumNoEncontradoException(String titulo) {
        super("Álbum no encontrado con título: " + titulo);
    }

    public AlbumNoEncontradoException(String titulo, String nombreArtista) {
        super("Álbum no encontrado con título: " + titulo + " para el artista: " + nombreArtista);
    }
}
