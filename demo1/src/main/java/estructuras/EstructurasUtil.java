package estructuras;

import java.util.Comparator;

/**
 * Clase de utilidad para operaciones comunes en estructuras de datos
 */
public class EstructurasUtil {

    /**
     * Ordena una lista usando el algoritmo de ordenamiento burbuja
     * Los elementos deben implementar Comparable
     */
    public static <T extends Comparable<T>> void ordenar(Lista<T> lista) {
        if (lista.tamanio() <= 1) {
            return;
        }

        boolean intercambio;
        do {
            intercambio = false;
            for (int i = 0; i < lista.tamanio() - 1; i++) {
                if (lista.obtener(i).compareTo(lista.obtener(i + 1)) > 0) {
                    // Intercambiar elementos
                    T temp = lista.obtener(i);
                    lista.eliminarEn(i);
                    lista.agregar(temp, i + 1);
                    intercambio = true;
                }
            }
        } while (intercambio);
    }

    /**
     * Ordena una lista usando un Comparator personalizado
     */
    public static <T> void ordenar(Lista<T> lista, Comparator<T> comparador) {
        if (lista.tamanio() <= 1) {
            return;
        }

        boolean intercambio;
        do {
            intercambio = false;
            for (int i = 0; i < lista.tamanio() - 1; i++) {
                if (comparador.compare(lista.obtener(i), lista.obtener(i + 1)) > 0) {
                    // Intercambiar elementos
                    T temp = lista.obtener(i);
                    lista.eliminarEn(i);
                    lista.agregar(temp, i + 1);
                    intercambio = true;
                }
            }
        } while (intercambio);
    }

    /**
     * Busca el elemento máximo en una lista
     */
    public static <T extends Comparable<T>> T encontrarMaximo(Lista<T> lista) {
        if (lista.estaVacia()) {
            throw new IllegalStateException("La lista está vacía");
        }

        T maximo = lista.obtener(0);
        for (int i = 1; i < lista.tamanio(); i++) {
            T actual = lista.obtener(i);
            if (actual.compareTo(maximo) > 0) {
                maximo = actual;
            }
        }
        return maximo;
    }

    /**
     * Busca el elemento mínimo en una lista
     */
    public static <T extends Comparable<T>> T encontrarMinimo(Lista<T> lista) {
        if (lista.estaVacia()) {
            throw new IllegalStateException("La lista está vacía");
        }

        T minimo = lista.obtener(0);
        for (int i = 1; i < lista.tamanio(); i++) {
            T actual = lista.obtener(i);
            if (actual.compareTo(minimo) < 0) {
                minimo = actual;
            }
        }
        return minimo;
    }

    /**
     * Invierte el orden de los elementos en una pila
     */
    public static <T> void invertirPila(IPila<T> pila) {
        ICola<T> colaAux = new Cola<>();

        while (!pila.estaVacia()) {
            colaAux.encolar(pila.desapilar());
        }

        while (!colaAux.estaVacia()) {
            pila.apilar(colaAux.desencolar());
        }
    }

    /**
     * Invierte el orden de los elementos en una cola
     */
    public static <T> void invertirCola(ICola<T> cola) {
        IPila<T> pilaAux = new Pila<>();

        while (!cola.estaVacia()) {
            pilaAux.apilar(cola.desencolar());
        }

        while (!pilaAux.estaVacia()) {
            cola.encolar(pilaAux.desapilar());
        }
    }

    /**
     * Convierte una lista a una pila
     */
    public static <T> IPila<T> listaAPila(Lista<T> lista) {
        IPila<T> pila = new Pila<>();
        for (T elemento : lista) {
            pila.apilar(elemento);
        }
        return pila;
    }

    /**
     * Convierte una lista a una cola
     */
    public static <T> ICola<T> listaACola(Lista<T> lista) {
        ICola<T> cola = new Cola<>();
        for (T elemento : lista) {
            cola.encolar(elemento);
        }
        return cola;
    }

    /**
     * Convierte una pila a una lista (el tope será el primer elemento)
     */
    public static <T> Lista<T> pilaALista(IPila<T> pila) {
        Lista<T> lista = new ListaEnlazada<>();
        IPila<T> pilaAux = new Pila<>();

        // Pasar a pila auxiliar para no modificar la original
        for (T elemento : pila) {
            pilaAux.apilar(elemento);
        }

        // Pasar de la pila auxiliar a la lista
        while (!pilaAux.estaVacia()) {
            lista.agregar(pilaAux.desapilar());
        }

        return lista;
    }

    /**
     * Convierte una cola a una lista (el frente será el primer elemento)
     */
    public static <T> Lista<T> colaALista(ICola<T> cola) {
        Lista<T> lista = new ListaEnlazada<>();
        for (T elemento : cola) {
            lista.agregar(elemento);
        }
        return lista;
    }
}
