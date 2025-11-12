package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusquedaAvanzadaDTO {
    private String artista;
    private String genero;
    private Integer anio;
    private String operadorLogico; // "AND" o "OR"
}
