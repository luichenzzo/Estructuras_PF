package com.example.demo.structures;

public interface Lista<T> extends Iterable<T> {

    void agregar(T elemento);

    void agregar(T elemento, int indice);

    boolean eliminar(T elemento);

    T eliminarEn(int indice);

    T obtener(int indice);

    boolean contiene(T elemento);

    int indiceDe(T elemento);

    boolean estaVacia();

    int tamanio();

    void limpiar();
}
