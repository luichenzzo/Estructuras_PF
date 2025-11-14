package com.example.demo.controllers;

import com.example.demo.structures.ListaEnlazada;
import com.example.demo.services.AutocompletadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador REST para el sistema de autocompletado basado en Trie.
 * RF-025: Implementa árbol de prefijos (Trie) para autocompletado eficiente.
 * RF-026: Devuelve todas las palabras que comiencen con un prefijo dado.
 */
@RestController
@RequestMapping("/api/autocompletar")
public class AutocompletadoController {

    @Autowired
    private AutocompletadoService autocompletadoService;

    /**
     * Construye los índices de autocompletado para canciones, artistas y álbumes.
     *
     * @return ResponseEntity con mensaje de confirmación
     */
    @PostMapping("/construir-indices")
    public ResponseEntity<String> construirIndices() {
        autocompletadoService.construirIndices();
        return ResponseEntity.ok("Índices de autocompletado construidos exitosamente");
    }

    /**
     * Autocompleta títulos de canciones basado en un prefijo.
     *
     * @param prefijo Prefijo a buscar
     * @return ResponseEntity con lista de títulos que comienzan con el prefijo
     */
    @GetMapping("/canciones")
    public ResponseEntity<List<String>> autocompletarCanciones(@RequestParam String prefijo) {
        ListaEnlazada<String> resultados = autocompletadoService.autocompletarCanciones(prefijo);

        List<String> lista = new ArrayList<>();
        for (String titulo : resultados) {
            lista.add(titulo);
        }

        return ResponseEntity.ok(lista);
    }

    /**
     * Autocompleta nombres de artistas basado en un prefijo.
     *
     * @param prefijo Prefijo a buscar
     * @return ResponseEntity con lista de nombres que comienzan con el prefijo
     */
    @GetMapping("/artistas")
    public ResponseEntity<List<String>> autocompletarArtistas(@RequestParam String prefijo) {
        ListaEnlazada<String> resultados = autocompletadoService.autocompletarArtistas(prefijo);

        List<String> lista = new ArrayList<>();
        for (String nombre : resultados) {
            lista.add(nombre);
        }

        return ResponseEntity.ok(lista);
    }

    /**
     * Autocompleta nombres de álbumes basado en un prefijo.
     *
     * @param prefijo Prefijo a buscar
     * @return ResponseEntity con lista de nombres que comienzan con el prefijo
     */
    @GetMapping("/albumes")
    public ResponseEntity<List<String>> autocompletarAlbumes(@RequestParam String prefijo) {
        ListaEnlazada<String> resultados = autocompletadoService.autocompletarAlbumes(prefijo);

        List<String> lista = new ArrayList<>();
        for (String nombre : resultados) {
            lista.add(nombre);
        }

        return ResponseEntity.ok(lista);
    }

    /**
     * Realiza autocompletado unificado en canciones, artistas y álbumes simultáneamente.
     *
     * @param prefijo Prefijo a buscar
     * @return ResponseEntity con mapa conteniendo resultados por categoría
     */
    @GetMapping("/todo")
    public ResponseEntity<Map<String, List<String>>> autocompletarTodo(@RequestParam String prefijo) {
        AutocompletadoService.AutocompletadoResultado resultado =
            autocompletadoService.autocompletarTodo(prefijo);

        Map<String, List<String>> respuesta = new HashMap<>();

        List<String> canciones = new ArrayList<>();
        for (String c : resultado.canciones) {
            canciones.add(c);
        }

        List<String> artistas = new ArrayList<>();
        for (String a : resultado.artistas) {
            artistas.add(a);
        }

        List<String> albumes = new ArrayList<>();
        for (String al : resultado.albumes) {
            albumes.add(al);
        }

        respuesta.put("canciones", canciones);
        respuesta.put("artistas", artistas);
        respuesta.put("albumes", albumes);

        return ResponseEntity.ok(respuesta);
    }
}
