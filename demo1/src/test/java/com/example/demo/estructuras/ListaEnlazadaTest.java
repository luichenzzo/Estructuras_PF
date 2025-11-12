package com.example.demo.estructuras;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase ListaEnlazada
 */
@DisplayName("Pruebas unitarias de ListaEnlazada")
class ListaEnlazadaTest {

    private ListaEnlazada<Integer> lista;

    @BeforeEach
    void setUp() {
        lista = new ListaEnlazada<>();
    }

    @Test
    @DisplayName("Test 1: Lista recién creada debe estar vacía")
    void testListaVaciaAlCrear() {
        assertTrue(lista.estaVacia());
        assertEquals(0, lista.tamanio());
    }

    @Test
    @DisplayName("Test 2: Agregar elementos aumenta el tamaño")
    void testAgregarAumentaTamanio() {
        lista.agregar(10);
        assertEquals(1, lista.tamanio());
        assertFalse(lista.estaVacia());

        lista.agregar(20);
        lista.agregar(30);
        assertEquals(3, lista.tamanio());
    }

    @Test
    @DisplayName("Test 3: Agregar elemento en índice específico")
    void testAgregarEnIndice() {
        lista.agregar(1);
        lista.agregar(2);
        lista.agregar(4);

        lista.agregar(3, 2); // Insertar 3 en la posición 2

        assertEquals(4, lista.tamanio());
        assertEquals(3, lista.obtener(2));
        assertEquals(4, lista.obtener(3));
    }

    @Test
    @DisplayName("Test 4: Obtener elemento por índice")
    void testObtenerPorIndice() {
        lista.agregar(100);
        lista.agregar(200);
        lista.agregar(300);

        assertEquals(100, lista.obtener(0));
        assertEquals(200, lista.obtener(1));
        assertEquals(300, lista.obtener(2));
    }

    @Test
    @DisplayName("Test 5: Eliminar elemento existente")
    void testEliminarElementoExistente() {
        lista.agregar(10);
        lista.agregar(20);
        lista.agregar(30);

        boolean eliminado = lista.eliminar(20);

        assertTrue(eliminado);
        assertEquals(2, lista.tamanio());
        assertFalse(lista.contiene(20));
    }

    @Test
    @DisplayName("Test 6: Eliminar elemento en índice específico")
    void testEliminarEnIndice() {
        lista.agregar(1);
        lista.agregar(2);
        lista.agregar(3);

        Integer eliminado = lista.eliminarEn(1);

        assertEquals(2, eliminado);
        assertEquals(2, lista.tamanio());
        assertEquals(1, lista.obtener(0));
        assertEquals(3, lista.obtener(1));
    }

    @Test
    @DisplayName("Test 7: Contiene encuentra elemento existente")
    void testContieneElementoExistente() {
        lista.agregar(5);
        lista.agregar(10);
        lista.agregar(15);

        assertTrue(lista.contiene(10));
        assertTrue(lista.contiene(5));
        assertTrue(lista.contiene(15));
    }

    @Test
    @DisplayName("Test 8: Índice de elemento devuelve posición correcta")
    void testIndiceDe() {
        lista.agregar(100);
        lista.agregar(200);
        lista.agregar(300);

        assertEquals(0, lista.indiceDe(100));
        assertEquals(1, lista.indiceDe(200));
        assertEquals(2, lista.indiceDe(300));
        assertEquals(-1, lista.indiceDe(999)); // Elemento inexistente
    }

    @Test
    @DisplayName("Test 9: Agregar en índice fuera de rango lanza excepción")
    void testAgregarIndiceInvalidoLanzaExcepcion() {
        lista.agregar(1);
        lista.agregar(2);

        assertThrows(IndexOutOfBoundsException.class, () -> {
            lista.agregar(99, 10);
        });

        assertThrows(IndexOutOfBoundsException.class, () -> {
            lista.agregar(99, -1);
        });
    }

    @Test
    @DisplayName("Test 10: Operaciones mixtas funcionan correctamente")
    void testOperacionesMixtas() {
        // Agregar elementos
        lista.agregar(1);
        lista.agregar(2);
        lista.agregar(3);

        // Insertar en medio
        lista.agregar(99, 1);
        assertEquals(4, lista.tamanio());

        // Verificar contiene
        assertTrue(lista.contiene(99));

        // Eliminar
        lista.eliminar(99);
        assertEquals(3, lista.tamanio());

        // Verificar orden
        assertEquals(1, lista.obtener(0));
        assertEquals(2, lista.obtener(1));
        assertEquals(3, lista.obtener(2));
    }
}

