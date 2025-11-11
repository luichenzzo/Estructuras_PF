package com.example.demo.estructuras;

import java.util.HashMap;
import java.util.Map;

/**
 * Nodo del Árbol de Prefijos (Trie)
 */
public class NodoTrie {

    private Map<Character, NodoTrie> hijos;
    private boolean esFinDePalabra;
    private String palabraCompleta; // Para almacenar la palabra completa en el nodo final

    /**
     * Constructor que inicializa un nodo vacío
     */
    public NodoTrie() {
        this.hijos = new HashMap<>();
        this.esFinDePalabra = false;
        this.palabraCompleta = null;
    }

    /**
     * Obtiene los hijos del nodo
     * @return Mapa de hijos
     */
    public Map<Character, NodoTrie> getHijos() {
        return hijos;
    }

    /**
     * Verifica si el nodo marca el fin de una palabra
     * @return true si es fin de palabra, false en caso contrario
     */
    public boolean esFinDePalabra() {
        return esFinDePalabra;
    }

    /**
     * Establece si el nodo marca el fin de una palabra
     * @param esFinDePalabra valor booleano
     */
    public void setEsFinDePalabra(boolean esFinDePalabra) {
        this.esFinDePalabra = esFinDePalabra;
    }

    /**
     * Obtiene la palabra completa almacenada
     * @return La palabra completa
     */
    public String getPalabraCompleta() {
        return palabraCompleta;
    }

    /**
     * Establece la palabra completa
     * @param palabraCompleta La palabra completa
     */
    public void setPalabraCompleta(String palabraCompleta) {
        this.palabraCompleta = palabraCompleta;
    }

    /**
     * Verifica si el nodo tiene un hijo con el carácter especificado
     * @param c El carácter a buscar
     * @return true si existe el hijo, false en caso contrario
     */
    public boolean contieneHijo(char c) {
        return hijos.containsKey(c);
    }

    /**
     * Obtiene el hijo correspondiente a un carácter
     * @param c El carácter
     * @return El nodo hijo, o null si no existe
     */
    public NodoTrie getHijo(char c) {
        return hijos.get(c);
    }

    /**
     * Agrega un hijo al nodo
     * @param c El carácter
     * @param nodo El nodo hijo
     */
    public void agregarHijo(char c, NodoTrie nodo) {
        hijos.put(c, nodo);
    }

    /**
     * Verifica si el nodo es una hoja (no tiene hijos)
     * @return true si es hoja, false en caso contrario
     */
    public boolean esHoja() {
        return hijos.isEmpty();
    }
}

