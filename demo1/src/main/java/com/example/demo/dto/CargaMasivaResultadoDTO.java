package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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
