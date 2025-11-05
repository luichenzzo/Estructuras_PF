package com.example.demo.dto;

import com.example.demo.modelo.GENERO;
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
    private String URLFotoArtista;
}

