package com.example.demo.structures;

public interface IPila<T> extends Iterable<T> {

    void apilar(T elemento);

    T desapilar();

    T verTope();

    boolean estaVacia();

    int tamanio();

    void limpiar();

    boolean contiene(T elemento);

    int buscar(T elemento);
}
