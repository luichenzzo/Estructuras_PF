package com.example.demo.services;

import com.example.demo.structures.TrieAutocompletado;
import com.example.demo.structures.ListaEnlazada;
import com.example.demo.model.Cancion;
import com.example.demo.model.Artista;
import com.example.demo.model.Album;
import com.example.demo.repository.CancionRepository;
import com.example.demo.repository.ArtistaRepository;
import com.example.demo.repository.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio para Autocompletado usando Trie
 * RF-025: Implementar Árbol de Prefijos (Trie)
 * RF-026: Devolver todas las palabras que comiencen con un prefijo dado
 */
@Service
public class AutocompletadoService {

    private TrieAutocompletado trieCanciones;
    private TrieAutocompletado trieArtistas;
    private TrieAutocompletado trieAlbumes;

    @Autowired
    private CancionRepository cancionRepository;

    @Autowired
    private ArtistaRepository artistaRepository;

    @Autowired
    private AlbumRepository albumRepository;

    public AutocompletadoService() {
        this.trieCanciones = new TrieAutocompletado();
        this.trieArtistas = new TrieAutocompletado();
        this.trieAlbumes = new TrieAutocompletado();
    }

    /**
     * Construye los índices Trie para canciones, artistas y álbumes
     */
    public void construirIndices() {
        trieCanciones = new TrieAutocompletado();
        trieArtistas = new TrieAutocompletado();
        trieAlbumes = new TrieAutocompletado();

        // Indexar canciones
        List<Cancion> canciones = cancionRepository.findAll();
        for (Cancion cancion : canciones) {
            if (cancion.getTitulo() != null && !cancion.getTitulo().isEmpty()) {
                trieCanciones.insertar(cancion.getTitulo());
            }
        }

        // Indexar artistas
        List<Artista> artistas = artistaRepository.findAll();
        for (Artista artista : artistas) {
            if (artista.getNombre() != null && !artista.getNombre().isEmpty()) {
                trieArtistas.insertar(artista.getNombre());
            }
        }

        // Indexar álbumes
        List<Album> albumes = albumRepository.findAll();
        for (Album album : albumes) {
            if (album.getTitulo() != null && !album.getTitulo().isEmpty()) {
                trieAlbumes.insertar(album.getTitulo());
            }
        }
    }

    /**
     * RF-026: Autocompleta títulos de canciones basado en un prefijo
     * @param prefijo Prefijo a buscar
     * @return Lista de títulos que comienzan con el prefijo
     */
    public ListaEnlazada<String> autocompletarCanciones(String prefijo) {
        if (prefijo == null || prefijo.isEmpty()) {
            return new ListaEnlazada<>();
        }
        return trieCanciones.autocompletar(prefijo);
    }

    /**
     * Autocompleta nombres de artistas basado en un prefijo
     * @param prefijo Prefijo a buscar
     * @return Lista de nombres que comienzan con el prefijo
     */
    public ListaEnlazada<String> autocompletarArtistas(String prefijo) {
        if (prefijo == null || prefijo.isEmpty()) {
            return new ListaEnlazada<>();
        }
        return trieArtistas.autocompletar(prefijo);
    }

    /**
     * Autocompleta nombres de álbumes basado en un prefijo
     * @param prefijo Prefijo a buscar
     * @return Lista de nombres que comienzan con el prefijo
     */
    public ListaEnlazada<String> autocompletarAlbumes(String prefijo) {
        if (prefijo == null || prefijo.isEmpty()) {
            return new ListaEnlazada<>();
        }
        return trieAlbumes.autocompletar(prefijo);
    }

    /**
     * Búsqueda unificada que autocompleta en canciones, artistas y álbumes
     * @param prefijo Prefijo a buscar
     * @return Resultados combinados
     */
    public AutocompletadoResultado autocompletarTodo(String prefijo) {
        AutocompletadoResultado resultado = new AutocompletadoResultado();
        resultado.canciones = autocompletarCanciones(prefijo);
        resultado.artistas = autocompletarArtistas(prefijo);
        resultado.albumes = autocompletarAlbumes(prefijo);
        return resultado;
    }

    /**
     * Agrega una nueva canción al índice
     * @param titulo Título de la canción
     */
    public void agregarCancion(String titulo) {
        if (titulo != null && !titulo.isEmpty()) {
            trieCanciones.insertar(titulo);
        }
    }

    /**
     * Agrega un nuevo artista al índice
     * @param nombre Nombre del artista
     */
    public void agregarArtista(String nombre) {
        if (nombre != null && !nombre.isEmpty()) {
            trieArtistas.insertar(nombre);
        }
    }

    /**
     * Agrega un nuevo álbum al índice
     * @param nombre Nombre del álbum
     */
    public void agregarAlbum(String nombre) {
        if (nombre != null && !nombre.isEmpty()) {
            trieAlbumes.insertar(nombre);
        }
    }

    /**
     * Elimina una canción del índice
     * @param titulo Título de la canción
     */
    public void eliminarCancion(String titulo) {
        if (titulo != null && !titulo.isEmpty()) {
            trieCanciones.eliminar(titulo);
        }
    }

    /**
     * Reconstruye todos los índices
     */
    public void reconstruirIndices() {
        construirIndices();
    }

    /**
     * Clase interna para resultados de autocompletado
     */
    public static class AutocompletadoResultado {
        public ListaEnlazada<String> canciones;
        public ListaEnlazada<String> artistas;
        public ListaEnlazada<String> albumes;

        public AutocompletadoResultado() {
            this.canciones = new ListaEnlazada<>();
            this.artistas = new ListaEnlazada<>();
            this.albumes = new ListaEnlazada<>();
        }
    }
}
