package com.example.demo.modelo;

import com.example.demo.estructuras.ListaDoblementeEnlazada;
import com.example.demo.estructuras.ListaEnlazada;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

@Document(collection = "playlists")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Playlist {
    @Id
    private String id;
    private String nombre;
    private ListaEnlazada<Cancion> canciones;
    @DBRef
    private Usuario creador;
    private ListaDoblementeEnlazada<Usuario> seguidores;

}
