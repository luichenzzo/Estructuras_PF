package com.example.demo.controllers;

import com.example.demo.model.Cancion;
import com.example.demo.services.GeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para funcionalidades generales del sistema.
 * Maneja operaciones como generación de playlists y reportes.
 */
@RestController
@RequestMapping("/general")
public class GeneralController {

    @Autowired
    private GeneralService generalService;

    /**
     * Genera una playlist "Descubrimiento Semanal" basada en los gustos musicales del usuario.
     * @param correoUsuario Correo electrónico del usuario
     * @return ResponseEntity con la lista de canciones recomendadas
     */
    @GetMapping("/descubrimiento-semanal")
    public ResponseEntity<List<Cancion>> generarDescubrimientoSemanal(@RequestParam String correoUsuario) {
        List<Cancion> playlist = generalService.generarDescubrimientoSemanal(correoUsuario);
        return new ResponseEntity<>(playlist, HttpStatus.OK);
    }

    /**
     * Descarga un reporte CSV con las canciones favoritas del usuario.
     *
     * @param correoUsuario Correo electrónico del usuario
     * @return ResponseEntity con el contenido CSV como String
     */
    @GetMapping("/reporte-csv")
    public ResponseEntity<String> descargarReporteCSV(@RequestParam String correoUsuario) {
        String csvContent = generalService.generarReporteCSV(correoUsuario);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("text", "csv", java.nio.charset.StandardCharsets.UTF_8));
        headers.setContentDispositionFormData("attachment", "canciones_favoritas_" + correoUsuario.split("@")[0] + ".csv");
        headers.setCacheControl("no-cache, no-store, must-revalidate");
        headers.setPragma("no-cache");
        headers.setExpires(0);

        return new ResponseEntity<>(csvContent, headers, HttpStatus.OK);
    }
}
