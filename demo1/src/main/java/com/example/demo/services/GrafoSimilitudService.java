package com.example.demo.services;

import com.example.demo.structures.GrafoPonderadoNoDirigido;
import com.example.demo.structures.ListaEnlazada;
import com.example.demo.model.Cancion;
import com.example.demo.repository.CancionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio para el Grafo de Similitud de Canciones
 * RF-021: Implementar Grafo Ponderado No Dirigido
 * RF-022: Algoritmo Dijkstra para rutas de mayor similitud
 */
@Service
public class GrafoSimilitudService {

    private GrafoPonderadoNoDirigido<String> grafoSimilitud;

    @Autowired
    private CancionRepository cancionRepository;

    public GrafoSimilitudService() {
        this.grafoSimilitud = new GrafoPonderadoNoDirigido<>();
    }

    /**
     * Construye el grafo de similitud basado en todas las canciones
     */
    public void construirGrafoSimilitud() {
        grafoSimilitud = new GrafoPonderadoNoDirigido<>();

        List<Cancion> canciones = cancionRepository.findAll();

        // Agregar todos los vértices (canciones)
        for (Cancion cancion : canciones) {
            grafoSimilitud.agregarVertice(cancion.getId());
        }

        // Calcular similitud entre cada par de canciones
        for (int i = 0; i < canciones.size(); i++) {
            for (int j = i + 1; j < canciones.size(); j++) {
                Cancion c1 = canciones.get(i);
                Cancion c2 = canciones.get(j);

                double similitud = calcularSimilitud(c1, c2);

                // Solo agregar arista si hay similitud significativa
                if (similitud > 0.0) {
                    // Menor peso = mayor similitud (para Dijkstra)
                    double peso = 1.0 / similitud;
                    grafoSimilitud.agregarArista(c1.getId(), c2.getId(), peso);
                }
            }
        }
    }

    /**
     * Calcula la similitud entre dos canciones
     * @param c1 Primera canción
     * @param c2 Segunda canción
     * @return Valor de similitud (mayor = más similares)
     */
    private double calcularSimilitud(Cancion c1, Cancion c2) {
        double similitud = 0.0;

        // Mismo género: +3 puntos
        if (c1.getGenero() != null && c1.getGenero().equals(c2.getGenero())) {
            similitud += 3.0;
        }

        // Mismo artista: +5 puntos
        if (c1.getArtista() != null && c2.getArtista() != null &&
            c1.getArtista().getId().equals(c2.getArtista().getId())) {
            similitud += 5.0;
        }

        // Mismo álbum: +4 puntos
        if (c1.getAlbum() != null && c2.getAlbum() != null &&
            c1.getAlbum().getId().equals(c2.getAlbum().getId())) {
            similitud += 4.0;
        }

        // Año similar (dentro de 3 años): +2 puntos
        if (Math.abs(c1.getAnio() - c2.getAnio()) <= 3) {
            similitud += 2.0;
        }

        // Duración similar (dentro de 30 segundos): +1 punto
        if (Math.abs(c1.getDuracion() - c2.getDuracion()) <= 0.5) {
            similitud += 1.0;
        }

        return similitud;
    }

    /**
     * Encuentra canciones similares a una canción dada
     * @param cancionId ID de la canción
     * @return Lista de IDs de canciones similares
     */
    public ListaEnlazada<String> obtenerCancionesSimilares(String cancionId) {
        ListaEnlazada<String> similares = new ListaEnlazada<>();

        if (!grafoSimilitud.obtenerVertices().contains(cancionId)) {
            return similares;
        }

        // Obtener vecinos directos (canciones con mayor similitud)
        var vecinos = grafoSimilitud.obtenerVecinos(cancionId);

        // Ordenar por similitud (menor peso = mayor similitud)
        vecinos.entrySet().stream()
            .sorted((e1, e2) -> Double.compare(e1.getValue(), e2.getValue()))
            .limit(10) // Top 10 más similares
            .forEach(entry -> similares.agregar(entry.getKey()));

        return similares;
    }

    /**
     * RF-022: Encuentra el camino de mayor similitud entre dos canciones usando Dijkstra
     * @param origenId ID de la canción origen
     * @param destinoId ID de la canción destino
     * @return Lista con el camino de canciones
     */
    public ListaEnlazada<String> encontrarRutaSimilitud(String origenId, String destinoId) {
        return grafoSimilitud.dijkstra(origenId, destinoId);
    }

    /**
     * Genera una cola de reproducción basada en similitud
     * @param cancionInicialId ID de la canción inicial
     * @param cantidad Cantidad de canciones a generar
     * @return Lista de IDs de canciones
     */
    public ListaEnlazada<String> generarColaReproduccionSimilar(String cancionInicialId, int cantidad) {
        ListaEnlazada<String> cola = new ListaEnlazada<>();
        cola.agregar(cancionInicialId);

        String cancionActual = cancionInicialId;

        for (int i = 1; i < cantidad; i++) {
            var vecinos = grafoSimilitud.obtenerVecinos(cancionActual);

            if (vecinos.isEmpty()) break;

            // Encontrar el vecino con mayor similitud que no esté en la cola
            String mejorVecino = null;
            double mejorSimilitud = Double.POSITIVE_INFINITY;

            for (var entry : vecinos.entrySet()) {
                String vecinoId = entry.getKey();
                double peso = entry.getValue();

                // Verificar si ya está en la cola
                boolean yaAgregado = false;
                for (String id : cola) {
                    if (id.equals(vecinoId)) {
                        yaAgregado = true;
                        break;
                    }
                }

                if (!yaAgregado && peso < mejorSimilitud) {
                    mejorSimilitud = peso;
                    mejorVecino = vecinoId;
                }
            }

            if (mejorVecino != null) {
                cola.agregar(mejorVecino);
                cancionActual = mejorVecino;
            } else {
                break;
            }
        }

        return cola;
    }

    /**
     * Reconstruye el grafo (útil cuando se agregan/eliminan canciones)
     */
    public void reconstruirGrafo() {
        construirGrafoSimilitud();
    }
}

