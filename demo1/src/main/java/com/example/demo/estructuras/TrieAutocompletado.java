package com.example.demo.estructuras;

/**
 * Implementación de un Árbol de Prefijos (Trie) para autocompletado
 * Usado para RF-025 y RF-026: Búsqueda eficiente de texto
 */
public class TrieAutocompletado {

    private NodoTrie raiz;

    /**
     * Constructor que inicializa un Trie vacío
     */
    public TrieAutocompletado() {
        this.raiz = new NodoTrie();
    }

    /**
     * Inserta una palabra en el Trie
     * @param palabra La palabra a insertar
     */
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

    /**
     * Busca si una palabra existe en el Trie
     * @param palabra La palabra a buscar
     * @return true si existe, false en caso contrario
     */
    public boolean buscar(String palabra) {
        if (palabra == null || palabra.isEmpty()) {
            return false;
        }

        NodoTrie nodo = buscarNodo(palabra.toLowerCase());
        return nodo != null && nodo.esFinDePalabra();
    }

    /**
     * Verifica si existe alguna palabra con el prefijo dado
     * @param prefijo El prefijo a buscar
     * @return true si existe al menos una palabra con ese prefijo
     */
    public boolean existeConPrefijo(String prefijo) {
        if (prefijo == null || prefijo.isEmpty()) {
            return false;
        }

        return buscarNodo(prefijo.toLowerCase()) != null;
    }

    /**
     * Busca el nodo correspondiente a una palabra o prefijo
     * @param texto El texto a buscar
     * @return El nodo encontrado, o null si no existe
     */
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

    /**
     * Obtiene todas las palabras que comienzan con un prefijo dado
     * RF-026: Devolver todas las palabras que comiencen con un prefijo dado
     * @param prefijo El prefijo a buscar
     * @return Lista de palabras que comienzan con el prefijo
     */
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

    /**
     * Método auxiliar recursivo para encontrar todas las palabras desde un nodo
     * @param nodo El nodo desde donde buscar
     * @param resultados Lista donde se agregan los resultados
     */
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

    /**
     * Elimina una palabra del Trie
     * @param palabra La palabra a eliminar
     * @return true si se eliminó, false si no existía
     */
    public boolean eliminar(String palabra) {
        if (palabra == null || palabra.isEmpty()) {
            return false;
        }

        return eliminarRecursivo(raiz, palabra.toLowerCase(), 0);
    }

    /**
     * Método auxiliar recursivo para eliminar una palabra
     * @param nodo El nodo actual
     * @param palabra La palabra a eliminar
     * @param indice El índice actual en la palabra
     * @return true si el nodo puede ser eliminado
     */
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

    /**
     * Verifica si el Trie está vacío
     * @return true si está vacío, false en caso contrario
     */
    public boolean estaVacio() {
        return raiz.esHoja();
    }

    /**
     * Limpia todo el Trie
     */
    public void limpiar() {
        this.raiz = new NodoTrie();
    }

    /**
     * Obtiene el número de palabras en el Trie
     * @return Número de palabras
     */
    public int contarPalabras() {
        return contarPalabrasRecursivo(raiz);
    }

    /**
     * Método auxiliar recursivo para contar palabras
     * @param nodo El nodo actual
     * @return Número de palabras desde este nodo
     */
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

