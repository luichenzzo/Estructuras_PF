package com.example.demo.controladores;


import com.example.demo.modelo.Cancion;
import com.example.demo.servicios.GeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/general")
public class GeneralController {

    @Autowired
    private GeneralService generalService;

    /**
     * RF-005: Genera playlist "Descubrimiento Semanal" basada en gustos del usuario
     */
    @GetMapping("/descubrimiento-semanal")
    public ResponseEntity<List<Cancion>> generarDescubrimientoSemanal(@RequestParam String correoUsuario) {
        List<Cancion> playlist = generalService.generarDescubrimientoSemanal(correoUsuario);
        return new ResponseEntity<>(playlist, HttpStatus.OK);
    }

    /**
     * RF-009: Descarga reporte CSV de canciones favoritas
     */
    @GetMapping("/reporte-csv")
    public ResponseEntity<String> descargarReporteCSV(@RequestParam String correoUsuario) {
        String csvContent = generalService.generarReporteCSV(correoUsuario);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDispositionFormData("attachment", "favoritos.csv");

        return new ResponseEntity<>(csvContent, headers, HttpStatus.OK);
    }
}
