package com.example.demo.structures;

import java.util.Map;
import java.util.Set;

public interface IGrafo<T> {

    void agregarVertice(T vertice);

    void eliminarVertice(T vertice);

    void agregarArista(T origen, T destino);

    void agregarArista(T origen, T destino, double peso);

    void eliminarArista(T origen, T destino);

    Map<T, Double> obtenerVecinos(T vertice);

    boolean existeArista(T origen, T destino);

    Set<T> obtenerVertices();

    double obtenerPeso(T origen, T destino);

    boolean estaVacio();

    int obtenerNumeroVertices();
}
