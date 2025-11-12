package com.example.demo.modelo;

import com.example.demo.estructuras.Cola;
import com.example.demo.estructuras.ListaEnlazada;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Entidad que representa un usuario del sistema de música.
 * Gestiona información personal, favoritos, listas de reproducción y relaciones sociales.
 */
@Document(collection = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Usuario {

    @Id
    private String id;
    private String correo;
    private String contrasena;
    private String nombre;
    private ListaEnlazada<Cancion> listaFavoritos;
    
    @JsonIgnore
    private ListaEnlazada<Playlist> listasDeReproduccion;
    
    @JsonIgnore
    private Cola<Cancion> colaReproduccion;
    
    private ListaEnlazada <Usuario> seguidores;
    private ListaEnlazada <Usuario> seguidos;

}
