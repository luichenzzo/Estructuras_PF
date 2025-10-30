package modelo;

import estructuras.Cola;
import estructuras.ListaEnlazada;

public class Usuario {

    private String usuario;
    private String contrasena;
    private String nombre;
    private ListaEnlazada<Cancion> listaFavoritos;
    private ListaEnlazada<Playlist> listasDeReproduccion;
    private Cola <Cancion> colaReproduccion;



}
