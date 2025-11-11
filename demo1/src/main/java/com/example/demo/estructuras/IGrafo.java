package com.example.demo.estructuras;

import java.util.Map;
import java.util.Set;

/**
 * Interfaz para Grafos
 * @param <T> Tipo de elemento que almacenarán los vértices
 */
public interface IGrafo<T> {

    /**
     * Agrega un vértice al grafo
     * @param vertice El vértice a agregar
     */
    void agregarVertice(T vertice);

    /**
     * Elimina un vértice del grafo
     * @param vertice El vértice a eliminar
     */
    void eliminarVertice(T vertice);

    /**
     * Agrega una arista entre dos vértices
     * @param origen Vértice origen
     * @param destino Vértice destino
     */
    void agregarArista(T origen, T destino);

    /**
     * Agrega una arista con peso entre dos vértices
     * @param origen Vértice origen
     * @param destino Vértice destino
     * @param peso Peso de la arista
     */
    void agregarArista(T origen, T destino, double peso);

    /**
     * Elimina una arista entre dos vértices
     * @param origen Vértice origen
     * @param destino Vértice destino
     */
    void eliminarArista(T origen, T destino);

    /**
     * Obtiene los vecinos de un vértice
     * @param vertice El vértice
     * @return Mapa de vecinos con sus pesos
     */
    Map<T, Double> obtenerVecinos(T vertice);

    /**
     * Verifica si existe una arista entre dos vértices
     * @param origen Vértice origen
     * @param destino Vértice destino
     * @return true si existe la arista, false en caso contrario
     */
    boolean existeArista(T origen, T destino);

    /**
     * Obtiene todos los vértices del grafo
     * @return Set con todos los vértices
     */
    Set<T> obtenerVertices();

    /**
     * Obtiene el peso de una arista
     * @param origen Vértice origen
     * @param destino Vértice destino
     * @return El peso de la arista
     */
    double obtenerPeso(T origen, T destino);

    /**
     * Verifica si el grafo está vacío
     * @return true si está vacío, false en caso contrario
     */
    boolean estaVacio();

    /**
     * Obtiene el número de vértices del grafo
     * @return Número de vértices
     */
    int obtenerNumeroVertices();
}

