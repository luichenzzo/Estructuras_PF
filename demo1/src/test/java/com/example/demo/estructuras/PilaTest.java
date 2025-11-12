package com.example.demo.estructuras;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.EmptyStackException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase Pila
 */
@DisplayName("Pruebas unitarias de Pila")
class PilaTest {

    private Pila<String> pila;

    @BeforeEach
    void setUp() {
        pila = new Pila<>();
    }

    @Test
    @DisplayName("Test 1: Pila recién creada debe estar vacía")
    void testPilaVaciaAlCrear() {
        assertTrue(pila.estaVacia());
        assertEquals(0, pila.tamanio());
    }

    @Test
    @DisplayName("Test 2: Apilar elementos aumenta el tamaño")
    void testApilarAumentaTamanio() {
        pila.apilar("Primero");
        assertEquals(1, pila.tamanio());
        assertFalse(pila.estaVacia());

        pila.apilar("Segundo");
        assertEquals(2, pila.tamanio());
    }

    @Test
    @DisplayName("Test 3: Desapilar elementos sigue el orden LIFO")
    void testDesapilarOrdenLIFO() {
        pila.apilar("A");
        pila.apilar("B");
        pila.apilar("C");

        assertEquals("C", pila.desapilar());
        assertEquals("B", pila.desapilar());
        assertEquals("A", pila.desapilar());
    }

    @Test
    @DisplayName("Test 4: Ver tope sin remover elemento")
    void testVerTopeSinRemover() {
        pila.apilar("Elemento1");
        pila.apilar("Elemento2");

        assertEquals("Elemento2", pila.verTope());
        assertEquals(2, pila.tamanio()); // El tamaño no debe cambiar
        assertEquals("Elemento2", pila.verTope()); // Debe seguir siendo el mismo
    }

    @Test
    @DisplayName("Test 5: Desapilar en pila vacía lanza excepción")
    void testDesapilarPilaVaciaLanzaExcepcion() {
        assertThrows(EmptyStackException.class, () -> pila.desapilar());
    }

    @Test
    @DisplayName("Test 6: Ver tope en pila vacía lanza excepción")
    void testVerTopePilaVaciaLanzaExcepcion() {
        assertThrows(EmptyStackException.class, () -> pila.verTope());
    }

    @Test
    @DisplayName("Test 7: Limpiar pila elimina todos los elementos")
    void testLimpiarPila() {
        pila.apilar("A");
        pila.apilar("B");
        pila.apilar("C");

        pila.limpiar();

        assertTrue(pila.estaVacia());
        assertEquals(0, pila.tamanio());
    }

    @Test
    @DisplayName("Test 8: Contiene encuentra elemento existente")
    void testContieneElementoExistente() {
        pila.apilar("Manzana");
        pila.apilar("Naranja");
        pila.apilar("Plátano");

        assertTrue(pila.contiene("Naranja"));
        assertTrue(pila.contiene("Manzana"));
        assertTrue(pila.contiene("Plátano"));
    }

    @Test
    @DisplayName("Test 9: Contiene no encuentra elemento inexistente")
    void testContieneElementoInexistente() {
        pila.apilar("Rojo");
        pila.apilar("Verde");

        assertFalse(pila.contiene("Azul"));
        assertFalse(pila.contiene("Amarillo"));
    }

    @Test
    @DisplayName("Test 10: Pila funciona correctamente con operaciones mixtas")
    void testOperacionesMixtas() {
        pila.apilar("Uno");
        pila.apilar("Dos");
        assertEquals("Dos", pila.desapilar());

        pila.apilar("Tres");
        pila.apilar("Cuatro");
        assertEquals("Cuatro", pila.verTope());

        assertTrue(pila.contiene("Tres"));
        assertEquals(3, pila.tamanio());

        pila.limpiar();
        assertTrue(pila.estaVacia());
    }
}

