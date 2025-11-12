package com.example.demo.modelo;

import com.example.demo.estructuras.ListaEnlazada;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

/**
 * Entidad que representa un álbum musical.
 * Contiene información del álbum y una lista enlazada de canciones.
 */
@Document(collection = "albumes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Album {

    @Id
    private String id;

    private String titulo;

    private int anio;

    @DBRef
    private Artista artista;

    private GENERO genero;

    private String URLPortadaAlbum;

    @JsonIgnore
    private ListaEnlazada<Cancion> canciones;
}
