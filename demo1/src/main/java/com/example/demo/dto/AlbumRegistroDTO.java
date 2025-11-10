package com.example.demo.dto;

import com.example.demo.modelo.GENERO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlbumRegistroDTO {
    private String titulo;
    private int anio;
    private String artistaId; // El ID del artista al que pertenece este álbum
    private GENERO genero;
    @JsonProperty("URLPortadaAlbum")
    @JsonAlias({"urlPortadaAlbum", "urlportadaalbum", "URLPortadaAlbum"})
    private String URLPortadaAlbum;
}

