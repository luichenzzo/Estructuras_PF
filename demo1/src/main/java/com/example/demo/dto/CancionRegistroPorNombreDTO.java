package com.example.demo.dto;

import com.example.demo.model.GENERO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CancionRegistroPorNombreDTO {
    private String titulo;
    private String nombreArtista;
    private String tituloAlbum;
    private GENERO genero;
    private int anio;
    private double duracion;
    @JsonProperty("URLCancion")
    @JsonAlias({"urlCancion", "urlcancion", "URLCancion"})
    private String URLCancion;
    // No se pide URLPortadaCancion, se toma del álbum
}

