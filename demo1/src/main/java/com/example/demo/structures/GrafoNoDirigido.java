package com.example.demo.structures;

import java.util.*;

public class GrafoNoDirigido<T> implements IGrafo<T> {

    private Map<T, Set<T>> listaAdyacencia;


    public GrafoNoDirigido() {
        this.listaAdyacencia = new HashMap<>();
    }

    @Override
    public void agregarVertice(T vertice) {
        if (vertice == null) {
            throw new IllegalArgumentException("El vértice no puede ser null");
        }
        listaAdyacencia.putIfAbsent(vertice, new HashSet<>());
    }

    @Override
    public void eliminarVertice(T vertice) {
        if (vertice == null) return;

        // Eliminar todas las aristas que apuntan a este vértice
        for (Set<T> vecinos : listaAdyacencia.values()) {
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
        listaAdyacencia.get(origen).add(destino);
        listaAdyacencia.get(destino).add(origen);
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

        Map<T, Double> vecinos = new HashMap<>();
        for (T vecino : listaAdyacencia.get(vertice)) {
            vecinos.put(vecino, 1.0); // Peso 1.0 por defecto
        }
        return vecinos;
    }


    public Set<T> obtenerVecinosDirectos(T vertice) {
        if (vertice == null || !listaAdyacencia.containsKey(vertice)) {
            return new HashSet<>();
        }
        return new HashSet<>(listaAdyacencia.get(vertice));
    }

    @Override
    public boolean existeArista(T origen, T destino) {
        if (origen == null || destino == null) return false;
        return listaAdyacencia.containsKey(origen) &&
               listaAdyacencia.get(origen).contains(destino);
    }

    @Override
    public Set<T> obtenerVertices() {
        return new HashSet<>(listaAdyacencia.keySet());
    }

    @Override
    public double obtenerPeso(T origen, T destino) {
        return existeArista(origen, destino) ? 1.0 : Double.POSITIVE_INFINITY;
    }

    @Override
    public boolean estaVacio() {
        return listaAdyacencia.isEmpty();
    }

    @Override
    public int obtenerNumeroVertices() {
        return listaAdyacencia.size();
    }


    public ListaEnlazada<T> bfs(T origen, int profundidad) {
        if (origen == null || !listaAdyacencia.containsKey(origen)) {
            return new ListaEnlazada<>();
        }

        ListaEnlazada<T> resultado = new ListaEnlazada<>();
        Map<T, Integer> niveles = new HashMap<>();
        Queue<T> cola = new LinkedList<>();

        cola.offer(origen);
        niveles.put(origen, 0);

        while (!cola.isEmpty()) {
            T actual = cola.poll();
            int nivelActual = niveles.get(actual);

            if (nivelActual < profundidad) {
                Set<T> vecinos = listaAdyacencia.get(actual);
                if (vecinos != null) {
                    for (T vecino : vecinos) {
                        if (!niveles.containsKey(vecino)) {
                            niveles.put(vecino, nivelActual + 1);
                            cola.offer(vecino);

                            // Solo agregamos si está en el nivel deseado
                            if (nivelActual + 1 == profundidad) {
                                resultado.agregar(vecino);
                            }
                        }
                    }
                }
            }
        }

        return resultado;
    }


    public ListaEnlazada<T> bfsCompleto(T origen) {
        if (origen == null || !listaAdyacencia.containsKey(origen)) {
            return new ListaEnlazada<>();
        }

        ListaEnlazada<T> resultado = new ListaEnlazada<>();
        Set<T> visitados = new HashSet<>();
        Queue<T> cola = new LinkedList<>();

        cola.offer(origen);
        visitados.add(origen);

        while (!cola.isEmpty()) {
            T actual = cola.poll();
            resultado.agregar(actual);

            Set<T> vecinos = listaAdyacencia.get(actual);
            if (vecinos != null) {
                for (T vecino : vecinos) {
                    if (!visitados.contains(vecino)) {
                        visitados.add(vecino);
                        cola.offer(vecino);
                    }
                }
            }
        }

        return resultado;
    }


    public ListaEnlazada<T> encontrarSugerencias(T usuario) {
        if (usuario == null || !listaAdyacencia.containsKey(usuario)) {
            return new ListaEnlazada<>();
        }

        ListaEnlazada<T> sugerencias = new ListaEnlazada<>();
        Set<T> amigosDirectos = listaAdyacencia.get(usuario);
        Set<T> yaConsiderados = new HashSet<>(amigosDirectos);
        yaConsiderados.add(usuario); // No sugerir a sí mismo

        // Buscar amigos de amigos
        for (T amigo : amigosDirectos) {
            Set<T> amigosDelAmigo = listaAdyacencia.get(amigo);
            if (amigosDelAmigo != null) {
                for (T candidato : amigosDelAmigo) {
                    if (!yaConsiderados.contains(candidato)) {
                        sugerencias.agregar(candidato);
                        yaConsiderados.add(candidato);
                    }
                }
            }
        }

        return sugerencias;
    }
}


