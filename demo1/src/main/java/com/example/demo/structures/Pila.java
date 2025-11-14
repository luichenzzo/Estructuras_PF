package com.example.demo.structures;

import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Pila<T> implements IPila<T> {

    private Nodo<T> tope;
    private int tamanio;

    public Pila() {
        this.tope = null;
        this.tamanio = 0;
    }

    public void apilar(T elemento) {
        Nodo<T> nuevoNodo = new Nodo<>(elemento);
        nuevoNodo.setSiguiente(tope);
        tope = nuevoNodo;
        tamanio++;
    }

    public T desapilar() {
        if (estaVacia()) {
            throw new EmptyStackException();
        }

        T dato = tope.getDato();
        tope = tope.getSiguiente();
        tamanio--;
        return dato;
    }

    public T verTope() {
        if (estaVacia()) {
            throw new EmptyStackException();
        }
        return tope.getDato();
    }

    public boolean estaVacia() {
        return tamanio == 0;
    }

    public int tamanio() {
        return tamanio;
    }

    public void limpiar() {
        tope = null;
        tamanio = 0;
    }

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
