package com.example.demo.estructuras;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase Cola
 */
@DisplayName("Pruebas unitarias de Cola")
class ColaTest {

    private Cola<Integer> cola;

    @BeforeEach
    void setUp() {
        cola = new Cola<>();
    }

    @Test
    @DisplayName("Test 1: Cola recién creada debe estar vacía")
    void testColaVaciaAlCrear() {
        assertTrue(cola.estaVacia());
        assertEquals(0, cola.tamanio());
    }

    @Test
    @DisplayName("Test 2: Encolar elementos aumenta el tamaño")
    void testEncolarAumentaTamanio() {
        cola.encolar(10);
        assertEquals(1, cola.tamanio());
        assertFalse(cola.estaVacia());

        cola.encolar(20);
        assertEquals(2, cola.tamanio());
    }

    @Test
    @DisplayName("Test 3: Desencolar elementos sigue el orden FIFO")
    void testDesencolarOrdenFIFO() {
        cola.encolar(1);
        cola.encolar(2);
        cola.encolar(3);

        assertEquals(1, cola.desencolar());
        assertEquals(2, cola.desencolar());
        assertEquals(3, cola.desencolar());
    }

    @Test
    @DisplayName("Test 4: Ver frente sin remover elemento")
    void testVerFrenteSinRemover() {
        cola.encolar(100);
        cola.encolar(200);

        assertEquals(100, cola.verFrente());
        assertEquals(2, cola.tamanio()); // El tamaño no debe cambiar
        assertEquals(100, cola.verFrente()); // Debe seguir siendo el mismo
    }

    @Test
    @DisplayName("Test 5: Ver final sin remover elemento")
    void testVerFinalSinRemover() {
        cola.encolar(100);
        cola.encolar(200);
        cola.encolar(300);

        assertEquals(300, cola.verFinal());
        assertEquals(3, cola.tamanio());
    }

    @Test
    @DisplayName("Test 6: Desencolar en cola vacía lanza excepción")
    void testDesencolarColaVaciaLanzaExcepcion() {
        assertThrows(NoSuchElementException.class, () -> {
            cola.desencolar();
        });
    }

    @Test
    @DisplayName("Test 7: Ver frente en cola vacía lanza excepción")
    void testVerFrenteColaVaciaLanzaExcepcion() {
        assertThrows(NoSuchElementException.class, () -> {
            cola.verFrente();
        });
    }

    @Test
    @DisplayName("Test 8: Ver final en cola vacía lanza excepción")
    void testVerFinalColaVaciaLanzaExcepcion() {
        assertThrows(NoSuchElementException.class, () -> {
            cola.verFinal();
        });
    }

    @Test
    @DisplayName("Test 9: Cola se vacía correctamente después de desencolar todos los elementos")
    void testColaSeVaciaCorrectamente() {
        cola.encolar(1);
        cola.encolar(2);
        cola.encolar(3);

        cola.desencolar();
        cola.desencolar();
        cola.desencolar();

        assertTrue(cola.estaVacia());
        assertEquals(0, cola.tamanio());
    }

    @Test
    @DisplayName("Test 10: Cola funciona correctamente con múltiples operaciones mixtas")
    void testOperacionesMixtas() {
        cola.encolar(1);
        cola.encolar(2);
        assertEquals(1, cola.desencolar());

        cola.encolar(3);
        cola.encolar(4);
        assertEquals(2, cola.verFrente());
        assertEquals(4, cola.verFinal());

        assertEquals(2, cola.desencolar());
        assertEquals(3, cola.desencolar());
        assertEquals(1, cola.tamanio());
    }
}

