package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para realizar búsquedas avanzadas de canciones.
 * RF-004: Permite búsquedas con lógica AND/OR por artista, género y año.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusquedaAvanzadaDTO {

    private String artista;

    private String genero;

    private Integer anio;

    private String operadorLogico; // "AND" o "OR"
}
