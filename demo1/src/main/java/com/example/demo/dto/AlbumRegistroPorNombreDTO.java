package com.example.demo.dto;

import com.example.demo.modelo.GENERO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlbumRegistroPorNombreDTO {
    private String titulo;
    private int anio;
    private String nombreArtista; // Nombre del artista en lugar del ID
    private GENERO genero;
    private String URLPortadaAlbum;
}

