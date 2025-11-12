package com.example.demo.modelo;

import com.example.demo.estructuras.ListaDoblementeEnlazada;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Entidad que representa un artista musical.
 * Contiene información del artista y una lista doblemente enlazada de álbumes.
 */
@Document(collection = "artistas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "albumes")
public class Artista {

    @Id
    private String id;

    private String nombre;

    private String nacionalidad;

    private GENERO generoPrincipal;

    private GENERO generoSecundario;

    private String URLFotoArtista;

    private ListaDoblementeEnlazada<Album> albumes;
}
