package modelo;

import estructuras.ListaDoblementeEnlazada;
import estructuras.ListaEnlazada;

public class Playlist {
    private String nombre;
    private ListaEnlazada<Cancion> canciones;
    private Usuario creador;
    private ListaDoblementeEnlazada<Usuario> seguidores;

}
