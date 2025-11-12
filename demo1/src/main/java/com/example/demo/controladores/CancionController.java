package com.example.demo.controladores;

import com.example.demo.dto.BusquedaAvanzadaDTO;
import com.example.demo.dto.CancionRegistroDTO;
import com.example.demo.dto.CancionRegistroPorNombreDTO;
import com.example.demo.modelo.Cancion;
import com.example.demo.servicios.CancionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/canciones")
public class CancionController {

    @Autowired
    private CancionService cancionService;

    // Endpoint 1: Recibe IDs del artista y álbum
    @PostMapping
    public ResponseEntity<Cancion> guardarCancion(@RequestBody CancionRegistroDTO cancionDTO) {
        Cancion cancionGuardada = cancionService.guardarCancion(cancionDTO);
        return new ResponseEntity<>(cancionGuardada, HttpStatus.CREATED);
    }

    // Endpoint 2: Recibe NOMBRES del artista y título del álbum (más lógico para el front)
    @PostMapping("/por-nombres")
    public ResponseEntity<Cancion> guardarCancionPorNombres(@RequestBody CancionRegistroPorNombreDTO cancionDTO) {
        Cancion cancionGuardada = cancionService.guardarCancionPorNombres(cancionDTO);
        return new ResponseEntity<>(cancionGuardada, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Cancion>> obtenerCanciones() {
        List<Cancion> canciones = cancionService.obtenerCanciones();
        return new ResponseEntity<>(canciones, HttpStatus.OK);
    }

    /**
     * RF-004: Búsqueda avanzada de canciones con lógica AND/OR
     */
    @PostMapping("/busqueda-avanzada")
    public ResponseEntity<List<Cancion>> busquedaAvanzada(@RequestBody BusquedaAvanzadaDTO busqueda) {
        List<Cancion> resultados = cancionService.busquedaAvanzada(busqueda);
        return new ResponseEntity<>(resultados, HttpStatus.OK);
    }
}
