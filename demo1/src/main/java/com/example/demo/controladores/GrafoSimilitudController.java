package com.example.demo.controladores;

import com.example.demo.estructuras.ListaEnlazada;
import com.example.demo.servicios.GrafoSimilitudService;
import com.example.demo.servicios.CancionService;
import com.example.demo.modelo.Cancion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Controlador para el Grafo de Similitud de Canciones
 * RF-021: Grafo Ponderado No Dirigido
 * RF-022: Algoritmo Dijkstra
 */
@RestController
@RequestMapping("/api/grafo-similitud")
public class GrafoSimilitudController {

    @Autowired
    private GrafoSimilitudService grafoSimilitudService;

    @Autowired
    private CancionService cancionService;

    /**
     * Construye el grafo de similitud
     */
    @PostMapping("/construir")
    public ResponseEntity<String> construirGrafo() {
        grafoSimilitudService.construirGrafoSimilitud();
        return ResponseEntity.ok("Grafo de similitud construido exitosamente");
    }

    /**
     * Obtiene canciones similares a una canción dada
     */
    @GetMapping("/similares/{cancionId}")
    public ResponseEntity<List<Cancion>> obtenerCancionesSimilares(@PathVariable String cancionId) {
        ListaEnlazada<String> idsSimiliares = grafoSimilitudService.obtenerCancionesSimilares(cancionId);

        List<Cancion> canciones = new ArrayList<>();
        for (String id : idsSimiliares) {
            Optional<Cancion> cancion = cancionService.obtenerCancionPorId(id);
            cancion.ifPresent(canciones::add);
        }

        return ResponseEntity.ok(canciones);
    }

    /**
     * RF-022: Encuentra la ruta de mayor similitud entre dos canciones usando Dijkstra
     */
    @GetMapping("/ruta-similitud/{origenId}/{destinoId}")
    public ResponseEntity<List<Cancion>> encontrarRutaSimilitud(
            @PathVariable String origenId,
            @PathVariable String destinoId) {

        ListaEnlazada<String> ruta = grafoSimilitudService.encontrarRutaSimilitud(origenId, destinoId);

        List<Cancion> canciones = new ArrayList<>();
        for (String id : ruta) {
            Optional<Cancion> cancion = cancionService.obtenerCancionPorId(id);
            cancion.ifPresent(canciones::add);
        }

        return ResponseEntity.ok(canciones);
    }

    /**
     * RF-006: Genera una "Radio" - cola de reproducción con canciones similares
     */
    @GetMapping("/generar-radio/{cancionId}")
    public ResponseEntity<List<Cancion>> generarRadio(
            @PathVariable String cancionId,
            @RequestParam(defaultValue = "20") int cantidad) {

        ListaEnlazada<String> cola = grafoSimilitudService.generarColaReproduccionSimilar(cancionId, cantidad);

        List<Cancion> canciones = new ArrayList<>();
        for (String id : cola) {
            Optional<Cancion> cancion = cancionService.obtenerCancionPorId(id);
            cancion.ifPresent(canciones::add);
        }

        return ResponseEntity.ok(canciones);
    }

    /**
     * Reconstruye el grafo
     */
    @PostMapping("/reconstruir")
    public ResponseEntity<String> reconstruirGrafo() {
        grafoSimilitudService.reconstruirGrafo();
        return ResponseEntity.ok("Grafo de similitud reconstruido exitosamente");
    }
}

