package com.example.demo.estructuras;

import java.util.*;

/**
 * Implementación de un Grafo Ponderado No Dirigido
 * Usado para el módulo de similitud de canciones (RF-021)
 * @param <T> Tipo de elemento que almacenarán los vértices
 */
public class GrafoPonderadoNoDirigido<T> implements IGrafo<T> {

    private Map<T, Map<T, Double>> listaAdyacencia;

    /**
     * Constructor que inicializa un grafo vacío
     */
    public GrafoPonderadoNoDirigido() {
        this.listaAdyacencia = new HashMap<>();
    }

    @Override
    public void agregarVertice(T vertice) {
        if (vertice == null) {
            throw new IllegalArgumentException("El vértice no puede ser null");
        }
        listaAdyacencia.putIfAbsent(vertice, new HashMap<>());
    }

    @Override
    public void eliminarVertice(T vertice) {
        if (vertice == null) return;

        // Eliminar todas las aristas que apuntan a este vértice
        for (Map<T, Double> vecinos : listaAdyacencia.values()) {
            vecinos.remove(vertice);
        }

        // Eliminar el vértice
        listaAdyacencia.remove(vertice);
    }

    @Override
    public void agregarArista(T origen, T destino) {
        agregarArista(origen, destino, 1.0);
    }

    @Override
    public void agregarArista(T origen, T destino, double peso) {
        if (origen == null || destino == null) {
            throw new IllegalArgumentException("Los vértices no pueden ser null");
        }

        // Agregar vértices si no existen
        agregarVertice(origen);
        agregarVertice(destino);

        // Como es no dirigido, agregamos en ambas direcciones
        listaAdyacencia.get(origen).put(destino, peso);
        listaAdyacencia.get(destino).put(origen, peso);
    }

    @Override
    public void eliminarArista(T origen, T destino) {
        if (origen == null || destino == null) return;

        // Como es no dirigido, eliminamos en ambas direcciones
        if (listaAdyacencia.containsKey(origen)) {
            listaAdyacencia.get(origen).remove(destino);
        }
        if (listaAdyacencia.containsKey(destino)) {
            listaAdyacencia.get(destino).remove(origen);
        }
    }

    @Override
    public Map<T, Double> obtenerVecinos(T vertice) {
        if (vertice == null || !listaAdyacencia.containsKey(vertice)) {
            return new HashMap<>();
        }
        return new HashMap<>(listaAdyacencia.get(vertice));
    }

    @Override
    public boolean existeArista(T origen, T destino) {
        if (origen == null || destino == null) return false;
        return listaAdyacencia.containsKey(origen) &&
               listaAdyacencia.get(origen).containsKey(destino);
    }

    @Override
    public Set<T> obtenerVertices() {
        return new HashSet<>(listaAdyacencia.keySet());
    }

    @Override
    public double obtenerPeso(T origen, T destino) {
        if (existeArista(origen, destino)) {
            return listaAdyacencia.get(origen).get(destino);
        }
        return Double.POSITIVE_INFINITY;
    }

    @Override
    public boolean estaVacio() {
        return listaAdyacencia.isEmpty();
    }

    @Override
    public int obtenerNumeroVertices() {
        return listaAdyacencia.size();
    }

    /**
     * Implementación del algoritmo de Dijkstra para encontrar la ruta de menor costo
     * Usado para RF-022: encontrar rutas de mayor similitud entre canciones
     * @param origen Vértice de origen
     * @param destino Vértice de destino
     * @return Lista con el camino más corto, o lista vacía si no existe camino
     */
    public ListaEnlazada<T> dijkstra(T origen, T destino) {
        if (origen == null || destino == null || !listaAdyacencia.containsKey(origen)) {
            return new ListaEnlazada<>();
        }

        Map<T, Double> distancias = new HashMap<>();
        Map<T, T> predecesores = new HashMap<>();
        Set<T> visitados = new HashSet<>();
        PriorityQueue<VerticeDistancia> cola = new PriorityQueue<>();

        // Inicializar distancias
        for (T vertice : listaAdyacencia.keySet()) {
            distancias.put(vertice, Double.POSITIVE_INFINITY);
        }
        distancias.put(origen, 0.0);
        cola.offer(new VerticeDistancia(origen, 0.0));

        while (!cola.isEmpty()) {
            VerticeDistancia actual = cola.poll();
            T verticeActual = actual.vertice;

            if (visitados.contains(verticeActual)) continue;
            visitados.add(verticeActual);

            if (verticeActual.equals(destino)) break;

            Map<T, Double> vecinos = listaAdyacencia.get(verticeActual);
            for (Map.Entry<T, Double> vecino : vecinos.entrySet()) {
                T verticeVecino = vecino.getKey();
                double peso = vecino.getValue();
                double nuevaDistancia = distancias.get(verticeActual) + peso;

                if (nuevaDistancia < distancias.get(verticeVecino)) {
                    distancias.put(verticeVecino, nuevaDistancia);
                    predecesores.put(verticeVecino, verticeActual);
                    cola.offer(new VerticeDistancia(verticeVecino, nuevaDistancia));
                }
            }
        }

        // Reconstruir el camino
        return reconstruirCamino(predecesores, origen, destino);
    }

    /**
     * Reconstruye el camino desde origen hasta destino
     */
    private ListaEnlazada<T> reconstruirCamino(Map<T, T> predecesores, T origen, T destino) {
        ListaEnlazada<T> camino = new ListaEnlazada<>();

        if (!predecesores.containsKey(destino) && !origen.equals(destino)) {
            return camino; // No hay camino
        }

        // Reconstruir camino desde destino a origen
        Pila<T> pilaTemp = new Pila<>();
        T actual = destino;

        while (actual != null) {
            pilaTemp.apilar(actual);
            if (actual.equals(origen)) break;
            actual = predecesores.get(actual);
        }

        // Invertir usando la pila
        while (!pilaTemp.estaVacia()) {
            camino.agregar(pilaTemp.desapilar());
        }

        return camino;
    }

    /**
     * Clase interna para almacenar vértices con su distancia en Dijkstra
     */
    private class VerticeDistancia implements Comparable<VerticeDistancia> {
        T vertice;
        double distancia;

        VerticeDistancia(T vertice, double distancia) {
            this.vertice = vertice;
            this.distancia = distancia;
        }

        @Override
        public int compareTo(VerticeDistancia otro) {
            return Double.compare(this.distancia, otro.distancia);
        }
    }
}

