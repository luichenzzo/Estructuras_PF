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

/**
 * Controlador REST para la gestión de canciones.
 * Proporciona endpoints para crear, consultar y buscar canciones.
 */
@RestController
@RequestMapping("/canciones")
public class CancionController {

    @Autowired
    private CancionService cancionService;

    /**
     * Registra una nueva canción utilizando los IDs del artista y álbum.
     *
     * @param cancionDTO Datos de la canción incluyendo IDs del artista y álbum
     * @return ResponseEntity con la canción creada
     */
    @PostMapping
    public ResponseEntity<Cancion> guardarCancion(@RequestBody CancionRegistroDTO cancionDTO) {
        Cancion cancionGuardada = cancionService.guardarCancion(cancionDTO);
        return new ResponseEntity<>(cancionGuardada, HttpStatus.CREATED);
    }

    /**
     * Registra una nueva canción utilizando los nombres del artista y álbum.
     * Este endpoint es más intuitivo para el frontend al no requerir IDs.
     *
     * @param cancionDTO Datos de la canción incluyendo nombres del artista y álbum
     * @return ResponseEntity con la canción creada
     */
    @PostMapping("/por-nombres")
    public ResponseEntity<Cancion> guardarCancionPorNombres(@RequestBody CancionRegistroPorNombreDTO cancionDTO) {
        Cancion cancionGuardada = cancionService.guardarCancionPorNombres(cancionDTO);
        return new ResponseEntity<>(cancionGuardada, HttpStatus.CREATED);
    }

    /**
     * Obtiene la lista completa de todas las canciones registradas.
     *
     * @return ResponseEntity con la lista de canciones
     */
    @GetMapping
    public ResponseEntity<List<Cancion>> obtenerCanciones() {
        List<Cancion> canciones = cancionService.obtenerCanciones();
        return new ResponseEntity<>(canciones, HttpStatus.OK);
    }

    /**
     * Realiza una búsqueda avanzada de canciones con lógica AND/OR.
     *
     * @param busqueda Criterios de búsqueda con operador lógico (AND/OR)
     * @return ResponseEntity con la lista de canciones que cumplen los criterios
     */
    @PostMapping("/busqueda-avanzada")
    public ResponseEntity<List<Cancion>> busquedaAvanzada(@RequestBody BusquedaAvanzadaDTO busqueda) {
        List<Cancion> resultados = cancionService.busquedaAvanzada(busqueda);
        return new ResponseEntity<>(resultados, HttpStatus.OK);
    }
}
