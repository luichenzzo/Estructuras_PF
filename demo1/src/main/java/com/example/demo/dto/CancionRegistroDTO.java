package com.example.demo.dto;

import com.example.demo.modelo.GENERO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para el registro de una canción utilizando IDs de artista y álbum.
 * La URL de la portada se toma automáticamente del álbum.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CancionRegistroDTO {

    private String titulo;

    private String artistaId;

    private String albumId;

    private GENERO genero;

    private int anio;

    private double duracion;

    @JsonProperty("URLCancion")
    @JsonAlias({"urlCancion", "urlcancion", "URLCancion"})
    private String URLCancion;
}
