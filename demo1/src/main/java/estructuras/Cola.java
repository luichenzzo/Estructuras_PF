package estructuras;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Implementación de una Cola (Queue) - FIFO (First In, First Out)
 * @param <T> Tipo de elemento que almacenará la cola
 */
public class Cola<T> implements ICola<T> {

    private Nodo<T> frente;
    private Nodo<T> finalCola;
    private int tamanio;

    /**
     * Constructor que inicializa una cola vacía
     */
    public Cola() {
        this.frente = null;
        this.finalCola = null;
        this.tamanio = 0;
    }

    /**
     * Encola un elemento al final de la cola
     * @param elemento El elemento a encolar
     */
    public void encolar(T elemento) {
        Nodo<T> nuevoNodo = new Nodo<>(elemento);

        if (estaVacia()) {
            frente = nuevoNodo;
            finalCola = nuevoNodo;
        } else {
            finalCola.setSiguiente(nuevoNodo);
            finalCola = nuevoNodo;
        }
        tamanio++;
    }

    /**
     * Desencola y retorna el elemento al frente de la cola
     * @return El elemento al frente
     * @throws NoSuchElementException si la cola está vacía
     */
    public T desencolar() {
        if (estaVacia()) {
            throw new NoSuchElementException("La cola está vacía");
        }

        T dato = frente.getDato();
        frente = frente.getSiguiente();

        if (frente == null) {
            finalCola = null;
        }

        tamanio--;
        return dato;
    }

    /**
     * Retorna el elemento al frente sin removerlo
     * @return El elemento al frente
     * @throws NoSuchElementException si la cola está vacía
     */
    public T verFrente() {
        if (estaVacia()) {
            throw new NoSuchElementException("La cola está vacía");
        }
        return frente.getDato();
    }

    /**
     * Retorna el elemento al final sin removerlo
     * @return El elemento al final
     * @throws NoSuchElementException si la cola está vacía
     */
    public T verFinal() {
        if (estaVacia()) {
            throw new NoSuchElementException("La cola está vacía");
        }
        return finalCola.getDato();
    }

    /**
     * Verifica si la cola está vacía
     * @return true si está vacía, false en caso contrario
     */
    public boolean estaVacia() {
        return tamanio == 0;
    }

    /**
     * Obtiene el tamaño de la cola
     * @return El número de elementos en la cola
     */
    public int tamanio() {
        return tamanio;
    }

    /**
     * Limpia todos los elementos de la cola
     */
    public void limpiar() {
        frente = null;
        finalCola = null;
        tamanio = 0;
    }

    /**
     * Busca un elemento en la cola
     * @param elemento El elemento a buscar
     * @return true si existe, false en caso contrario
     */
    public boolean contiene(T elemento) {
        Nodo<T> actual = frente;
        while (actual != null) {
            if (actual.getDato().equals(elemento)) {
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        return new ColaIterator();
    }

    /**
     * Iterador para recorrer la cola desde el frente hasta el final
     */
    private class ColaIterator implements Iterator<T> {
        private Nodo<T> actual = frente;

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
            return "[Frente] -> [Final]";
        }

        StringBuilder sb = new StringBuilder("[Frente] ");
        Nodo<T> actual = frente;
        while (actual != null) {
            sb.append(actual.getDato());
            if (actual.getSiguiente() != null) {
                sb.append(" -> ");
            }
            actual = actual.getSiguiente();
        }
        sb.append(" [Final]");
        return sb.toString();
    }
}
