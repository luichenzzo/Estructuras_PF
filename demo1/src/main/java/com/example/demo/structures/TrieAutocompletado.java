package com.example.demo.structures;

public class TrieAutocompletado {

    private NodoTrie raiz;

    public TrieAutocompletado() {
        this.raiz = new NodoTrie();
    }

    public void insertar(String palabra) {
        if (palabra == null || palabra.isEmpty()) {
            return;
        }

        // Convertir a minúsculas para búsqueda case-insensitive
        String palabraNormalizada = palabra.toLowerCase();
        NodoTrie actual = raiz;

        for (char c : palabraNormalizada.toCharArray()) {
            if (!actual.contieneHijo(c)) {
                actual.agregarHijo(c, new NodoTrie());
            }
            actual = actual.getHijo(c);
        }

        actual.setEsFinDePalabra(true);
        actual.setPalabraCompleta(palabra); // Guardar la palabra original
    }

    public boolean buscar(String palabra) {
        if (palabra == null || palabra.isEmpty()) {
            return false;
        }

        NodoTrie nodo = buscarNodo(palabra.toLowerCase());
        return nodo != null && nodo.esFinDePalabra();
    }

    public boolean existeConPrefijo(String prefijo) {
        if (prefijo == null || prefijo.isEmpty()) {
            return false;
        }

        return buscarNodo(prefijo.toLowerCase()) != null;
    }

    private NodoTrie buscarNodo(String texto) {
        NodoTrie actual = raiz;

        for (char c : texto.toCharArray()) {
            if (!actual.contieneHijo(c)) {
                return null;
            }
            actual = actual.getHijo(c);
        }

        return actual;
    }

    public ListaEnlazada<String> autocompletar(String prefijo) {
        ListaEnlazada<String> resultados = new ListaEnlazada<>();

        if (prefijo == null || prefijo.isEmpty()) {
            return resultados;
        }

        String prefijoNormalizado = prefijo.toLowerCase();
        NodoTrie nodo = buscarNodo(prefijoNormalizado);

        if (nodo == null) {
            return resultados; // No existe el prefijo
        }

        // Si el prefijo mismo es una palabra completa, agregarla
        if (nodo.esFinDePalabra()) {
            resultados.agregar(nodo.getPalabraCompleta());
        }

        // Buscar todas las palabras que comienzan con este prefijo
        buscarTodasLasPalabras(nodo, resultados);

        return resultados;
    }

    private void buscarTodasLasPalabras(NodoTrie nodo, ListaEnlazada<String> resultados) {
        if (nodo == null) {
            return;
        }

        // Recorrer todos los hijos
        for (NodoTrie hijo : nodo.getHijos().values()) {
            if (hijo.esFinDePalabra()) {
                resultados.agregar(hijo.getPalabraCompleta());
            }

            // Búsqueda recursiva en los descendientes
            buscarTodasLasPalabras(hijo, resultados);
        }
    }

    public boolean eliminar(String palabra) {
        if (palabra == null || palabra.isEmpty()) {
            return false;
        }

        return eliminarRecursivo(raiz, palabra.toLowerCase(), 0);
    }

    private boolean eliminarRecursivo(NodoTrie nodo, String palabra, int indice) {
        if (nodo == null) {
            return false;
        }

        // Caso base: hemos llegado al final de la palabra
        if (indice == palabra.length()) {
            if (!nodo.esFinDePalabra()) {
                return false; // La palabra no existe
            }

            nodo.setEsFinDePalabra(false);
            nodo.setPalabraCompleta(null);

            // Si el nodo no tiene hijos, puede ser eliminado
            return nodo.esHoja();
        }

        char c = palabra.charAt(indice);
        NodoTrie hijo = nodo.getHijo(c);

        if (hijo == null) {
            return false; // La palabra no existe
        }

        boolean debeEliminarHijo = eliminarRecursivo(hijo, palabra, indice + 1);

        if (debeEliminarHijo) {
            nodo.getHijos().remove(c);
            // Retornar true si este nodo puede ser eliminado también
            return !nodo.esFinDePalabra() && nodo.esHoja();
        }

        return false;
    }

    public boolean estaVacio() {
        return raiz.esHoja();
    }

    public void limpiar() {
        this.raiz = new NodoTrie();
    }

    public int contarPalabras() {
        return contarPalabrasRecursivo(raiz);
    }

    private int contarPalabrasRecursivo(NodoTrie nodo) {
        if (nodo == null) {
            return 0;
        }

        int contador = nodo.esFinDePalabra() ? 1 : 0;

        for (NodoTrie hijo : nodo.getHijos().values()) {
            contador += contarPalabrasRecursivo(hijo);
        }

        return contador;
    }
}
