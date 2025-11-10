package com.example.demo.modelo;

import com.example.demo.estructuras.Cola;
import com.example.demo.estructuras.ListaEnlazada;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Usuario {

    @Id
    private String id;
    private String usuario;
    private String contrasena;
    private String nombre;
    private ListaEnlazada<Cancion> listaFavoritos;
    private ListaEnlazada<Playlist> listasDeReproduccion;
    private Cola<Cancion> colaReproduccion;

}
