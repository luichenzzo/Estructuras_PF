package com.example.demo.controladores;

import com.example.demo.estructuras.ListaEnlazada;
import com.example.demo.servicios.AutocompletadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador para Autocompletado con Trie
 * RF-025: Árbol de Prefijos (Trie)
 * RF-026: Devolver palabras que comiencen con un prefijo
 */
@RestController
@RequestMapping("/api/autocompletar")
public class AutocompletadoController {

    @Autowired
    private AutocompletadoService autocompletadoService;

    /**
     * Construye los índices de autocompletado
     */
    @PostMapping("/construir-indices")
    public ResponseEntity<String> construirIndices() {
        autocompletadoService.construirIndices();
        return ResponseEntity.ok("Índices de autocompletado construidos exitosamente");
    }

    /**
     * RF-003, RF-026: Autocompleta títulos de canciones
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
     * Autocompleta nombres de artistas
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
     * Autocompleta nombres de álbumes
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
     * Autocompletado unificado (canciones, artistas y álbumes)
     */
    @GetMapping("/todo")
    public ResponseEntity<Map<String, List<String>>> autocompletarTodo(@RequestParam String prefijo) {
        AutocompletadoService.AutocompletadoResultado resultado =
            autocompletadoService.autocompletarTodo(prefijo);

        Map<String, List<String>> respuesta = new HashMap<>();

        // Convertir ListaEnlazada a List para la respuesta
        List<String> canciones = new ArrayList<>();
        for (String c : resultado.canciones) {
            canciones.add(c);
        }

        List<String> artistas = new ArrayList<>();
        for (String a : resultado.artistas) {
            artistas.add(a);
        }

        List<String> albumes = new ArrayList<>();
        for (String alb : resultado.albumes) {
            albumes.add(alb);
        }

        respuesta.put("canciones", canciones);
        respuesta.put("artistas", artistas);
        respuesta.put("albumes", albumes);

        return ResponseEntity.ok(respuesta);
    }

    /**
     * Reconstruye los índices
     */
    @PostMapping("/reconstruir")
    public ResponseEntity<String> reconstruirIndices() {
        autocompletadoService.reconstruirIndices();
        return ResponseEntity.ok("Índices reconstruidos exitosamente");
    }
}

