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
 * Controlador REST para el grafo de similitud de canciones.
 * RF-021: Implementa grafo ponderado no dirigido para similitud entre canciones.
 * RF-022: Utiliza algoritmo de Dijkstra para encontrar rutas de máxima similitud.
 */
@RestController
@RequestMapping("/api/grafo-similitud")
public class GrafoSimilitudController {

    @Autowired
    private GrafoSimilitudService grafoSimilitudService;

    @Autowired
    private CancionService cancionService;

    /**
     * Construye el grafo de similitud analizando todas las canciones del sistema.
     *
     * @return ResponseEntity con mensaje de confirmación
     */
    @PostMapping("/construir")
    public ResponseEntity<String> construirGrafo() {
        grafoSimilitudService.construirGrafoSimilitud();
        return ResponseEntity.ok("Grafo de similitud construido exitosamente");
    }

    /**
     * Obtiene canciones similares a una canción específica.
     *
     * @param cancionId ID de la canción de referencia
     * @return ResponseEntity con lista de canciones similares
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
     * Encuentra la ruta de mayor similitud entre dos canciones usando el algoritmo de Dijkstra.
     * RF-022: Implementa Dijkstra para encontrar el camino óptimo de similitud.
     *
     * @param origenId  ID de la canción origen
     * @param destinoId ID de la canción destino
     * @return ResponseEntity con la secuencia de canciones en la ruta
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
     * Genera una "Radio" - cola de reproducción con canciones similares.
     * RF-006: Crea una cola de reproducción basada en similitud musical.
     *
     * @param cancionId ID de la canción inicial
     * @param cantidad  Número de canciones a incluir en la radio (por defecto 20)
     * @return ResponseEntity con la cola de reproducción generada
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
     * Reconstruye completamente el grafo de similitud.
     * Útil cuando se agregan o eliminan canciones del sistema.
     *
     * @return ResponseEntity con mensaje de confirmación
     */
    @PostMapping("/reconstruir")
    public ResponseEntity<String> reconstruirGrafo() {
        grafoSimilitudService.reconstruirGrafo();
        return ResponseEntity.ok("Grafo de similitud reconstruido exitosamente");
    }
}
