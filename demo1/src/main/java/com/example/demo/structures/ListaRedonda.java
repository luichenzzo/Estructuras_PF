package com.example.demo.structures;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ListaRedonda<T> implements Lista<T> {

    private Nodo<T> cola; // Apuntamos a la cola para facilitar agregar al final y acceder a la cabeza
    private int tamanio;

    public ListaRedonda() {
        this.cola = null;
        this.tamanio = 0;
    }

    @Override
    public void agregar(T elemento) {
        Nodo<T> nuevoNodo = new Nodo<>(elemento);

        if (cola == null) {
            nuevoNodo.setSiguiente(nuevoNodo); // Apunta a sí mismo
            cola = nuevoNodo;
        } else {
            nuevoNodo.setSiguiente(cola.getSiguiente()); // El nuevo nodo apunta a la cabeza
            cola.setSiguiente(nuevoNodo); // La cola actual apunta al nuevo nodo
            cola = nuevoNodo; // El nuevo nodo se convierte en la cola
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

        Nodo<T> nuevoNodo = new Nodo<>(elemento);

        if (indice == 0) {
            if (cola == null) {
                nuevoNodo.setSiguiente(nuevoNodo);
                cola = nuevoNodo;
            } else {
                nuevoNodo.setSiguiente(cola.getSiguiente());
                cola.setSiguiente(nuevoNodo);
            }
        } else {
            Nodo<T> actual = cola.getSiguiente(); // Empezamos en la cabeza
            for (int i = 0; i < indice - 1; i++) {
                actual = actual.getSiguiente();
            }
            nuevoNodo.setSiguiente(actual.getSiguiente());
            actual.setSiguiente(nuevoNodo);
        }
        tamanio++;
    }

    @Override
    public boolean eliminar(T elemento) {
        if (cola == null) {
            return false;
        }

        Nodo<T> actual = cola.getSiguiente(); // Cabeza
        Nodo<T> anterior = cola;

        do {
            if (actual.getDato().equals(elemento)) {
                if (actual == cola && tamanio == 1) {
                    // Es el único elemento
                    cola = null;
                } else {
                    anterior.setSiguiente(actual.getSiguiente());
                    if (actual == cola) {
                        cola = anterior;
                    }
                }
                tamanio--;
                return true;
            }
            anterior = actual;
            actual = actual.getSiguiente();
        } while (actual != cola.getSiguiente());

        return false;
    }

    @Override
    public T eliminarEn(int indice) {
        if (indice < 0 || indice >= tamanio) {
            throw new IndexOutOfBoundsException("Índice fuera de rango: " + indice);
        }

        T dato;

        if (tamanio == 1) {
            dato = cola.getDato();
            cola = null;
        } else if (indice == 0) {
            dato = cola.getSiguiente().getDato();
            cola.setSiguiente(cola.getSiguiente().getSiguiente());
        } else {
            Nodo<T> actual = cola.getSiguiente(); // Cabeza
            for (int i = 0; i < indice - 1; i++) {
                actual = actual.getSiguiente();
            }
            dato = actual.getSiguiente().getDato();
            actual.setSiguiente(actual.getSiguiente().getSiguiente());

            if (indice == tamanio - 1) {
                cola = actual;
            }
        }
        tamanio--;
        return dato;
    }

    @Override
    public T obtener(int indice) {
        if (indice < 0 || indice >= tamanio) {
            throw new IndexOutOfBoundsException("Índice fuera de rango: " + indice);
        }

        Nodo<T> actual = cola.getSiguiente(); // Cabeza
        for (int i = 0; i < indice; i++) {
            actual = actual.getSiguiente();
        }
        return actual.getDato();
    }

    @Override
    public boolean contiene(T elemento) {
        if (cola == null) {
            return false;
        }

        Nodo<T> actual = cola.getSiguiente(); // Cabeza
        do {
            if (actual.getDato().equals(elemento)) {
                return true;
            }
            actual = actual.getSiguiente();
        } while (actual != cola.getSiguiente());

        return false;
    }

    @Override
    public int indiceDe(T elemento) {
        if (cola == null) {
            return -1;
        }

        Nodo<T> actual = cola.getSiguiente(); // Cabeza
        int indice = 0;
        do {
            if (actual.getDato().equals(elemento)) {
                return indice;
            }
            actual = actual.getSiguiente();
            indice++;
        } while (actual != cola.getSiguiente());

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
        cola = null;
        tamanio = 0;
    }

    public void rotarAdelante() {
        if (cola != null) {
            cola = cola.getSiguiente();
        }
    }

    public void rotarAtras() {
        if (cola != null) {
            Nodo<T> actual = cola.getSiguiente();
            while (actual.getSiguiente() != cola) {
                actual = actual.getSiguiente();
            }
            cola = actual;
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new ListaRedondaIterator();
    }

    private class ListaRedondaIterator implements Iterator<T> {
        private Nodo<T> actual = (cola != null) ? cola.getSiguiente() : null;
        private int contador = 0;

        @Override
        public boolean hasNext() {
            return contador < tamanio;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            T dato = actual.getDato();
            actual = actual.getSiguiente();
            contador++;
            return dato;
        }
    }

    public Iterator<T> iteradorInfinito() {
        return new Iterator<T>() {
            private Nodo<T> actual = (cola != null) ? cola.getSiguiente() : null;

            @Override
            public boolean hasNext() {
                return cola != null;
            }

            @Override
            public T next() {
                if (cola == null) {
                    throw new NoSuchElementException();
                }
                T dato = actual.getDato();
                actual = actual.getSiguiente();
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
        Nodo<T> actual = cola.getSiguiente(); // Cabeza
        int contador = 0;
        do {
            sb.append(actual.getDato());
            actual = actual.getSiguiente();
            contador++;
            if (contador < tamanio) {
                sb.append(", ");
            }
        } while (contador < tamanio);
        sb.append("]");
        return sb.toString();
    }
}
