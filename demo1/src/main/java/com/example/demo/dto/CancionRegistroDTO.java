package com.example.demo.dto;

import com.example.demo.modelo.GENERO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String URLCancion;
    // No se pide URLPortadaCancion, se toma del álbum
}

