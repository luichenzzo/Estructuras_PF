package com.example.demo.servicios;

import com.example.demo.dto.ArtistaRegistroDTO;
import com.example.demo.modelo.Artista;
import com.example.demo.repositorio.ArtistaRepository;
import com.example.demo.estructuras.ListaDoblementeEnlazada;
import com.example.demo.excepciones.DatosInvalidosException;
import com.example.demo.excepciones.RecursoDuplicadoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de artistas musicales.
 * Proporciona lógica de negocio para crear y consultar artistas.
 */
@Service
public class ArtistaService {

    @Autowired
    private ArtistaRepository artistaRepository;

    /**
     * Guarda un nuevo artista en el sistema.
     *
     * @param artistaDTO Datos del artista a registrar
     * @return Artista guardado
     * @throws DatosInvalidosException Si el nombre del artista está vacío
     * @throws RecursoDuplicadoException Si ya existe un artista con ese nombre
     */
    public Artista guardarArtista(ArtistaRegistroDTO artistaDTO) {
        // Validar datos
        if (artistaDTO.getNombre() == null || artistaDTO.getNombre().trim().isEmpty()) {
            throw new DatosInvalidosException("El nombre del artista es obligatorio");
        }

        // Verificar si el artista ya existe
        Optional<Artista> artistaExistente = artistaRepository.findByNombre(artistaDTO.getNombre());
        if (artistaExistente.isPresent()) {
            throw new RecursoDuplicadoException("Ya existe un artista con el nombre: " + artistaDTO.getNombre());
        }
        System.out.println(artistaDTO.getURLFotoArtista());
        Artista artista = new Artista();
        artista.setNombre(artistaDTO.getNombre());
        artista.setNacionalidad(artistaDTO.getNacionalidad());
        artista.setGeneroPrincipal(artistaDTO.getGeneroPrincipal());
        artista.setGeneroSecundario(artistaDTO.getGeneroSecundario());
        artista.setURLFotoArtista(artistaDTO.getURLFotoArtista());
        artista.setAlbumes(new ListaDoblementeEnlazada<>());
        System.out.println(artista.toString());
        return artistaRepository.save(artista);
    }

    /**
     * Obtiene todos los artistas registrados en el sistema.
     *
     * @return Lista de todos los artistas
     */
    public ArrayList<Artista> obtenerArtistas() {

        return (ArrayList<Artista>) artistaRepository.findAll();
    }

    /**
     * Obtiene únicamente los nombres de todos los artistas.
     *
     * @return ResponseEntity con lista de nombres de artistas
     */
    public ResponseEntity<List<String>> obtenerNombresArtistas() {
        List<Artista> artistas = artistaRepository.findAll();
        List<String> nombres = new ArrayList<>();

        for (Artista artista : artistas) {
            nombres.add(artista.getNombre());
        }

        return ResponseEntity.ok(nombres);
    }
}
