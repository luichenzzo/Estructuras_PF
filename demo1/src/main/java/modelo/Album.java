package modelo;

import estructuras.ListaEnlazada;

public class Album {
    private String id;
    private String titulo;
    private int anio;
    private Artista artista;
    private GENERO genero;
    private String URLPortadaAlbum;
    private ListaEnlazada<Cancion> canciones;
}
