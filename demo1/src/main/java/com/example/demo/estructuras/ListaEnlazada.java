package com.example.demo.estructuras;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Implementación de una Lista Simplemente Enlazada
 * @param <T> Tipo de elemento que almacenará la lista
 */
public class ListaEnlazada<T> implements Lista<T> {

    private Nodo<T> cabeza;
    private int tamanio;

    /**
     * Constructor que inicializa una lista vacía
     */
    public ListaEnlazada() {
        this.cabeza = null;
        this.tamanio = 0;
    }

    @Override
    public void agregar(T elemento) {
        if (cabeza == null) {
            cabeza = new Nodo<>(elemento);
        } else {
            Nodo<T> actual = cabeza;
            while (actual.getSiguiente() != null) {
                actual = actual.getSiguiente();
            }
            actual.setSiguiente(new Nodo<>(elemento));
        }
        tamanio++;
    }

    @Override
    public void agregar(T elemento, int indice) {
        if (indice < 0 || indice > tamanio) {
            throw new IndexOutOfBoundsException("Índice fuera de rango: " + indice);
        }

        if (indice == 0) {
            Nodo<T> nuevoNodo = new Nodo<>(elemento);
            nuevoNodo.setSiguiente(cabeza);
            cabeza = nuevoNodo;
        } else {
            Nodo<T> actual = cabeza;
            for (int i = 0; i < indice - 1; i++) {
                actual = actual.getSiguiente();
            }
            Nodo<T> nuevoNodo = new Nodo<>(elemento);
            nuevoNodo.setSiguiente(actual.getSiguiente());
            actual.setSiguiente(nuevoNodo);
        }
        tamanio++;
    }

    @Override
    public boolean eliminar(T elemento) {
        System.out.println("Voy a eliminar");
        if (cabeza == null) {
            System.out.println("No hay");
            return false;
        }

        if (elementosIguales(cabeza.getDato(), elemento)) {
            cabeza = cabeza.getSiguiente();
            tamanio--;
            System.out.println("Eliminado en cabeza");
            return true;
        }

        Nodo<T> actual = cabeza;

        while (actual.getSiguiente() != null) {
            if (elementosIguales(actual.getSiguiente().getDato(), elemento)) {
                actual.setSiguiente(actual.getSiguiente().getSiguiente());
                tamanio--;
                System.out.println("Eliminado en medio o final");
                return true;
            }
            actual = actual.getSiguiente();
        }
        System.out.println(" no Eliminado");
        return false;
    }

    @Override
    public T eliminarEn(int indice) {
        if (indice < 0 || indice >= tamanio) {
            throw new IndexOutOfBoundsException("Índice fuera de rango: " + indice);
        }

        T dato;
        if (indice == 0) {
            dato = cabeza.getDato();
            cabeza = cabeza.getSiguiente();
        } else {
            Nodo<T> actual = cabeza;
            for (int i = 0; i < indice - 1; i++) {
                actual = actual.getSiguiente();
            }
            dato = actual.getSiguiente().getDato();
            actual.setSiguiente(actual.getSiguiente().getSiguiente());
        }
        tamanio--;
        return dato;
    }

    @Override
    public T obtener(int indice) {
        if (indice < 0 || indice >= tamanio) {
            throw new IndexOutOfBoundsException("Índice fuera de rango: " + indice);
        }

        Nodo<T> actual = cabeza;
        for (int i = 0; i < indice; i++) {
            actual = actual.getSiguiente();
        }
        return actual.getDato();
    }

    @Override
    public boolean contiene(T elemento) {
        Nodo<T> actual = cabeza;
        while (actual != null) {
            System.out.println("Comparando con: " + actual.getDato());
            System.out.println("Elemento buscado: " + elemento);
            if (elementosIguales(actual.getDato(), elemento)) {
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }

    @Override
    public int indiceDe(T elemento) {
        Nodo<T> actual = cabeza;
        int indice = 0;
        while (actual != null) {
            if (elementosIguales(actual.getDato(), elemento)) {
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
        tamanio = 0;
    }

    @Override
    public Iterator<T> iterator() {
        return new ListaEnlazadaIterator();
    }

    /**
     * Iterador para recorrer la lista enlazada
     */
    private class ListaEnlazadaIterator implements Iterator<T> {
        private Nodo<T> actual = cabeza;

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

    // Nuevo método helper que intenta comparar dos elementos usando equals
    // y, si falla, intenta comparar sus IDs mediante reflexión (getId o campo `id`).
    private boolean elementosIguales(T a, T b) {
        if (a == b) return true;
        if (a == null || b == null) return false;
        try {
            if (a.equals(b)) return true;
        } catch (Exception ignored) {}

        Object ida = obtenerId(a);
        Object idb = obtenerId(b);
        if (ida != null && idb != null) {
            return ida.equals(idb);
        }
        return false;
    }

    private Object obtenerId(Object o) {
        if (o == null) return null;
        try {
            Method m = o.getClass().getMethod("getId");
            return m.invoke(o);
        } catch (Exception e) {
            // intentar campo "id"
            try {
                Field f = o.getClass().getDeclaredField("id");
                f.setAccessible(true);
                return f.get(o);
            } catch (Exception ex) {
                return null;
            }
        }
    }

    @Override
    public String toString() {
        if (estaVacia()) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        Nodo<T> actual = cabeza;
        while (actual != null) {
            T dato = actual.getDato();
            if (dato == this) {
                sb.append("(this ListaEnlazada)");
            } else {
                sb.append(String.valueOf(dato));
            }
            if (actual.getSiguiente() != null) {
                sb.append(", ");
            }
            actual = actual.getSiguiente();
        }
        sb.append("]");
        return sb.toString();
    }
}
