package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO para el resultado de una carga masiva de canciones desde CSV.
 * Contiene estadísticas y detalles de errores si los hay.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CargaMasivaResultadoDTO {
    private int totalProcesadas;
    private int exitosas;
    private int fallidas;
    private List<String> errores = new ArrayList<>();

    public void agregarError(int linea, String mensaje) {
        errores.add("Línea " + linea + ": " + mensaje);
    }
}

