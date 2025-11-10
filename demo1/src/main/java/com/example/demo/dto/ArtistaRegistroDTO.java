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
public class ArtistaRegistroDTO {
    private String nombre;
    private String nacionalidad;
    private GENERO generoPrincipal;
    private GENERO generoSecundario;
    @JsonProperty("URLFotoArtista")
    @JsonAlias({"urlFotoArtista", "urlfotoartista", "URLFotoArtista"})
    private String URLFotoArtista;
}
