package com.example.demo.excepciones;

/**
 * Excepción lanzada cuando no se encuentra un álbum en la base de datos.
 * Extiende de RecursoNoEncontradoException para proporcionar mensajes específicos.
 */
public class AlbumNoEncontradoException extends RecursoNoEncontradoException {

    /**
     * Constructor para búsqueda por ID.
     *
     * @param id ID del álbum no encontrado
     */
    public AlbumNoEncontradoException(Long id) {
        super("Álbum no encontrado con ID: " + id);
    }

    /**
     * Constructor para búsqueda por título.
     *
     * @param titulo Título del álbum no encontrado
     */
    public AlbumNoEncontradoException(String titulo) {
        super("Álbum no encontrado con título: " + titulo);
    }

    /**
     * Constructor para búsqueda por título y artista.
     *
     * @param titulo        Título del álbum no encontrado
     * @param nombreArtista Nombre del artista asociado
     */
    public AlbumNoEncontradoException(String titulo, String nombreArtista) {
        super("Álbum no encontrado con título: " + titulo + " para el artista: " + nombreArtista);
    }
}
