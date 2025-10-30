package modelo;

import estructuras.ListaDoblementeEnlazada;


public class Artista {
    private String id;
    private String nombre;
    private String nacionalidad;
    private GENERO generoPrincipal;
    private GENERO generoSecundario;
    private String URLFotoArtista;
    private ListaDoblementeEnlazada<Album> albumes;
}
