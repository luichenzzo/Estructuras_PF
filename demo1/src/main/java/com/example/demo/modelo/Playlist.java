package com.example.demo.modelo;

import com.example.demo.estructuras.ListaDoblementeEnlazada;
import com.example.demo.estructuras.ListaEnlazada;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

/**
 * Entidad que representa una lista de reproducción (playlist).
 * Contiene canciones organizadas por un usuario creador y puede tener seguidores.
 */
@Document(collection = "playlists")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Playlist {

    @Id
    private String id;

    private String nombre;

    // descripción opcional
    private String descripcion;

    // Evitar que Jackson intente serializar la implementación personalizada de ListaEnlazada
    @JsonIgnore
    private ListaEnlazada<Cancion> canciones;

    @DBRef
    private Usuario creador;

    @JsonIgnore
    private ListaDoblementeEnlazada<Usuario> seguidores;

}
