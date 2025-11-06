package com.example.demo.controladores;

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
    public ResponseEntity<?> guardarCancion(@RequestBody CancionRegistroDTO cancionDTO) {
        try {
            Cancion cancionGuardada = cancionService.guardarCancion(cancionDTO);
            return new ResponseEntity<>(cancionGuardada, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno del servidor", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint 2: Recibe NOMBRES del artista y título del álbum (más lógico para el front)
    @PostMapping("/por-nombres")
    public ResponseEntity<?> guardarCancionPorNombres(@RequestBody CancionRegistroPorNombreDTO cancionDTO) {
        try {
            Cancion cancionGuardada = cancionService.guardarCancionPorNombres(cancionDTO);
            return new ResponseEntity<>(cancionGuardada, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno del servidor", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public List<Cancion> obtenerCanciones() {
        return cancionService.obtenerCanciones();
    }
}

