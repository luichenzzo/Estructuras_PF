package com.example.demo.modelo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

@Document(collection = "canciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cancion {
    @Id
    private String id;
    private String titulo;
    @DBRef
    private Artista artista;
    @DBRef
    private Album album;
    private GENERO genero;
    private int anio;
    private double duracion;
    private String URLCancion;
    private String URLPortadaCancion;
}
