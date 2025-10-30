package estructuras;

import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Implementación de una Pila (Stack) - LIFO (Last In, First Out)
 * @param <T> Tipo de elemento que almacenará la pila
 */
public class Pila<T> implements IPila<T> {

    private Nodo<T> tope;
    private int tamanio;

    /**
     * Constructor que inicializa una pila vacía
     */
    public Pila() {
        this.tope = null;
        this.tamanio = 0;
    }

    /**
     * Apila un elemento en el tope de la pila
     * @param elemento El elemento a apilar
     */
    public void apilar(T elemento) {
        Nodo<T> nuevoNodo = new Nodo<>(elemento);
        nuevoNodo.setSiguiente(tope);
        tope = nuevoNodo;
        tamanio++;
    }

    /**
     * Desapila y retorna el elemento en el tope de la pila
     * @return El elemento en el tope
     * @throws EmptyStackException si la pila está vacía
     */
    public T desapilar() {
        if (estaVacia()) {
            throw new EmptyStackException();
        }

        T dato = tope.getDato();
        tope = tope.getSiguiente();
        tamanio--;
        return dato;
    }

    /**
     * Retorna el elemento en el tope sin removerlo
     * @return El elemento en el tope
     * @throws EmptyStackException si la pila está vacía
     */
    public T verTope() {
        if (estaVacia()) {
            throw new EmptyStackException();
        }
        return tope.getDato();
    }

    /**
     * Verifica si la pila está vacía
     * @return true si está vacía, false en caso contrario
     */
    public boolean estaVacia() {
        return tamanio == 0;
    }

    /**
     * Obtiene el tamaño de la pila
     * @return El número de elementos en la pila
     */
    public int tamanio() {
        return tamanio;
    }

    /**
     * Limpia todos los elementos de la pila
     */
    public void limpiar() {
        tope = null;
        tamanio = 0;
    }

    /**
     * Busca un elemento en la pila
     * @param elemento El elemento a buscar
     * @return true si existe, false en caso contrario
     */
    public boolean contiene(T elemento) {
        Nodo<T> actual = tope;
        while (actual != null) {
            if (actual.getDato().equals(elemento)) {
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }

    /**
     * Busca la posición de un elemento desde el tope (0 = tope)
     * @param elemento El elemento a buscar
     * @return La posición desde el tope, o -1 si no existe
     */
    public int buscar(T elemento) {
        Nodo<T> actual = tope;
        int posicion = 0;
        while (actual != null) {
            if (actual.getDato().equals(elemento)) {
                return posicion;
            }
            actual = actual.getSiguiente();
            posicion++;
        }
        return -1;
    }

    @Override
    public Iterator<T> iterator() {
        return new PilaIterator();
    }

    /**
     * Iterador para recorrer la pila desde el tope hasta el fondo
     */
    private class PilaIterator implements Iterator<T> {
        private Nodo<T> actual = tope;

        @Override
        public boolean hasNext() {
            return actual != null;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            T dato = actual.getDato();
            actual = actual.getSiguiente();
            return dato;
        }
    }

    @Override
    public String toString() {
        if (estaVacia()) {
            return "[Tope]";
        }

        StringBuilder sb = new StringBuilder("[Tope] ");
        Nodo<T> actual = tope;
        while (actual != null) {
            sb.append(actual.getDato());
            if (actual.getSiguiente() != null) {
                sb.append(" <- ");
            }
            actual = actual.getSiguiente();
        }
        return sb.toString();
    }
}
