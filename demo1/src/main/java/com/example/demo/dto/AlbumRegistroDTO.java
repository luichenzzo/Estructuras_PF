package com.example.demo.dto;

import com.example.demo.modelo.GENERO;
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
    private String URLPortadaAlbum;
}

