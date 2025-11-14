package com.example.demo.structures;

public interface ICola<T> extends Iterable<T> {

    void encolar(T elemento);

    T desencolar();

    T verFrente();

    T verFinal();

    boolean estaVacia();

    int tamanio();

    void limpiar();

    boolean contiene(T elemento);
}
