package estructuras;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Implementación de una Lista Doblemente Enlazada
 * @param <T> Tipo de elemento que almacenará la lista
 */
public class ListaDoblementeEnlazada<T> implements Lista<T> {

    private NodoDoble<T> cabeza;
    private NodoDoble<T> cola;
    private int tamanio;

    /**
     * Constructor que inicializa una lista vacía
     */
    public ListaDoblementeEnlazada() {
        this.cabeza = null;
        this.cola = null;
        this.tamanio = 0;
    }

    @Override
    public void agregar(T elemento) {
        NodoDoble<T> nuevoNodo = new NodoDoble<>(elemento);

        if (cabeza == null) {
            cabeza = nuevoNodo;
            cola = nuevoNodo;
        } else {
            cola.setSiguiente(nuevoNodo);
            nuevoNodo.setAnterior(cola);
            cola = nuevoNodo;
        }
        tamanio++;
    }

    @Override
    public void agregar(T elemento, int indice) {
        if (indice < 0 || indice > tamanio) {
            throw new IndexOutOfBoundsException("Índice fuera de rango: " + indice);
        }

        if (indice == tamanio) {
            agregar(elemento);
            return;
        }

        NodoDoble<T> nuevoNodo = new NodoDoble<>(elemento);

        if (indice == 0) {
            nuevoNodo.setSiguiente(cabeza);
            cabeza.setAnterior(nuevoNodo);
            cabeza = nuevoNodo;
        } else {
            NodoDoble<T> actual = obtenerNodo(indice);
            nuevoNodo.setSiguiente(actual);
            nuevoNodo.setAnterior(actual.getAnterior());
            actual.getAnterior().setSiguiente(nuevoNodo);
            actual.setAnterior(nuevoNodo);
        }
        tamanio++;
    }

    @Override
    public boolean eliminar(T elemento) {
        NodoDoble<T> actual = cabeza;

        while (actual != null) {
            if (actual.getDato().equals(elemento)) {
                if (actual == cabeza) {
                    cabeza = cabeza.getSiguiente();
                    if (cabeza != null) {
                        cabeza.setAnterior(null);
                    } else {
                        cola = null;
                    }
                } else if (actual == cola) {
                    cola = cola.getAnterior();
                    cola.setSiguiente(null);
                } else {
                    actual.getAnterior().setSiguiente(actual.getSiguiente());
                    actual.getSiguiente().setAnterior(actual.getAnterior());
                }
                tamanio--;
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }

    @Override
    public T eliminarEn(int indice) {
        if (indice < 0 || indice >= tamanio) {
            throw new IndexOutOfBoundsException("Índice fuera de rango: " + indice);
        }

        NodoDoble<T> nodoAEliminar = obtenerNodo(indice);
        T dato = nodoAEliminar.getDato();

        if (nodoAEliminar == cabeza) {
            cabeza = cabeza.getSiguiente();
            if (cabeza != null) {
                cabeza.setAnterior(null);
            } else {
                cola = null;
            }
        } else if (nodoAEliminar == cola) {
            cola = cola.getAnterior();
            cola.setSiguiente(null);
        } else {
            nodoAEliminar.getAnterior().setSiguiente(nodoAEliminar.getSiguiente());
            nodoAEliminar.getSiguiente().setAnterior(nodoAEliminar.getAnterior());
        }

        tamanio--;
        return dato;
    }

    @Override
    public T obtener(int indice) {
        if (indice < 0 || indice >= tamanio) {
            throw new IndexOutOfBoundsException("Índice fuera de rango: " + indice);
        }
        return obtenerNodo(indice).getDato();
    }

    /**
     * Método auxiliar para obtener un nodo en una posición específica
     * Optimizado para buscar desde el inicio o el final según el índice
     */
    private NodoDoble<T> obtenerNodo(int indice) {
        NodoDoble<T> actual;

        if (indice < tamanio / 2) {
            // Buscar desde el inicio
            actual = cabeza;
            for (int i = 0; i < indice; i++) {
                actual = actual.getSiguiente();
            }
        } else {
            // Buscar desde el final
            actual = cola;
            for (int i = tamanio - 1; i > indice; i--) {
                actual = actual.getAnterior();
            }
        }
        return actual;
    }

    @Override
    public boolean contiene(T elemento) {
        NodoDoble<T> actual = cabeza;
        while (actual != null) {
            if (actual.getDato().equals(elemento)) {
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }

    @Override
    public int indiceDe(T elemento) {
        NodoDoble<T> actual = cabeza;
        int indice = 0;
        while (actual != null) {
            if (actual.getDato().equals(elemento)) {
                return indice;
            }
            actual = actual.getSiguiente();
            indice++;
        }
        return -1;
    }

    @Override
    public boolean estaVacia() {
        return tamanio == 0;
    }

    @Override
    public int tamanio() {
        return tamanio;
    }

    @Override
    public void limpiar() {
        cabeza = null;
        cola = null;
        tamanio = 0;
    }

    @Override
    public Iterator<T> iterator() {
        return new ListaDoblementeEnlazadaIterator();
    }

    /**
     * Iterador para recorrer la lista hacia adelante
     */
    private class ListaDoblementeEnlazadaIterator implements Iterator<T> {
        private NodoDoble<T> actual = cabeza;

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

    /**
     * Iterador para recorrer la lista hacia atrás
     */
    public Iterator<T> iteradorReverso() {
        return new Iterator<T>() {
            private NodoDoble<T> actual = cola;

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
                actual = actual.getAnterior();
                return dato;
            }
        };
    }

    @Override
    public String toString() {
        if (estaVacia()) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        NodoDoble<T> actual = cabeza;
        while (actual != null) {
            sb.append(actual.getDato());
            if (actual.getSiguiente() != null) {
                sb.append(", ");
            }
            actual = actual.getSiguiente();
        }
        sb.append("]");
        return sb.toString();
    }
}
