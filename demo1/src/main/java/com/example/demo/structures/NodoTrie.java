package com.example.demo.structures;

import java.util.HashMap;
import java.util.Map;

public class NodoTrie {

    private Map<Character, NodoTrie> hijos;
    private boolean esFinDePalabra;
    private String palabraCompleta; // Para almacenar la palabra completa en el nodo final

    public NodoTrie() {
        this.hijos = new HashMap<>();
        this.esFinDePalabra = false;
        this.palabraCompleta = null;
    }

    public Map<Character, NodoTrie> getHijos() {
        return hijos;
    }

    public boolean esFinDePalabra() {
        return esFinDePalabra;
    }

    public void setEsFinDePalabra(boolean esFinDePalabra) {
        this.esFinDePalabra = esFinDePalabra;
    }

    public String getPalabraCompleta() {
        return palabraCompleta;
    }

    public void setPalabraCompleta(String palabraCompleta) {
        this.palabraCompleta = palabraCompleta;
    }

    public boolean contieneHijo(char c) {
        return hijos.containsKey(c);
    }

    public NodoTrie getHijo(char c) {
        return hijos.get(c);
    }

    public void agregarHijo(char c, NodoTrie nodo) {
        hijos.put(c, nodo);
    }

    public boolean esHoja() {
        return hijos.isEmpty();
    }
}
