package com.example.demo.servicios;

import com.example.demo.dto.CancionRegistroDTO;
import com.example.demo.dto.CancionRegistroPorNombreDTO;
import com.example.demo.dto.BusquedaAvanzadaDTO;
import com.example.demo.modelo.Album;
import com.example.demo.modelo.Artista;
import com.example.demo.modelo.Cancion;
import com.example.demo.repositorio.AlbumRepository;
import com.example.demo.repositorio.ArtistaRepository;
import com.example.demo.repositorio.CancionRepository;
import com.example.demo.excepciones.ArtistaNoEncontradoException;
import com.example.demo.excepciones.AlbumNoEncontradoException;
import com.example.demo.excepciones.DatosInvalidosException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Servicio para la gestión de canciones.
 * Proporciona lógica de negocio para crear, consultar y buscar canciones.
 */
@Service
public class CancionService {

    @Autowired
    private CancionRepository cancionRepository;

    @Autowired
    private ArtistaRepository artistaRepository;

    @Autowired
    private AlbumRepository albumRepository;

    /**
     * Guarda una nueva canción utilizando los IDs del artista y álbum.
     *
     * @param cancionDTO Datos de la canción incluyendo IDs
     * @return Canción guardada
     * @throws DatosInvalidosException Si el título está vacío
     * @throws ArtistaNoEncontradoException Si el artista no existe
     * @throws AlbumNoEncontradoException Si el álbum no existe
     */
    public Cancion guardarCancion(CancionRegistroDTO cancionDTO) {
        // Validar datos
        if (cancionDTO.getTitulo() == null || cancionDTO.getTitulo().trim().isEmpty()) {
            throw new DatosInvalidosException("El título de la canción es obligatorio");
        }

        // Buscar el artista por ID
        Optional<Artista> artistaOpt = artistaRepository.findById(cancionDTO.getArtistaId());
        if (artistaOpt.isEmpty()) {
            throw new ArtistaNoEncontradoException(cancionDTO.getArtistaId());
        }

        // Buscar el álbum por ID
        Optional<Album> albumOpt = albumRepository.findById(cancionDTO.getAlbumId());
        if (albumOpt.isEmpty()) {
            throw new AlbumNoEncontradoException(cancionDTO.getAlbumId());
        }

        Artista artista = artistaOpt.get();
        Album album = albumOpt.get();

        // Crear la canción
        Cancion cancion = new Cancion();
        cancion.setTitulo(cancionDTO.getTitulo());
        cancion.setArtista(artista);
        cancion.setAlbum(album);
        cancion.setGenero(cancionDTO.getGenero());
        cancion.setAnio(cancionDTO.getAnio());
        cancion.setDuracion(cancionDTO.getDuracion());
        cancion.setURLCancion(cancionDTO.getURLCancion());
        // Tomar la portada del álbum
        cancion.setURLPortadaCancion(album.getURLPortadaAlbum());

        // Guardar la canción en la BD
        Cancion cancionGuardada = cancionRepository.save(cancion);

        // Agregar la canción a la lista de canciones del álbum
        album.getCanciones().agregar(cancionGuardada);

        albumRepository.save(album);

        return cancionGuardada;
    }

    /**
     * Guarda una nueva canción utilizando los nombres del artista y álbum.
     *
     * @param cancionDTO Datos de la canción incluyendo nombres
     * @return Canción guardada
     * @throws DatosInvalidosException Si faltan datos obligatorios
     * @throws ArtistaNoEncontradoException Si el artista no existe
     * @throws AlbumNoEncontradoException Si el álbum no existe
     */
    public Cancion guardarCancionPorNombres(CancionRegistroPorNombreDTO cancionDTO) {
        // Validar datos
        if (cancionDTO.getTitulo() == null || cancionDTO.getTitulo().trim().isEmpty()) {
            throw new DatosInvalidosException("El título de la canción es obligatorio");
        }
        if (cancionDTO.getNombreArtista() == null || cancionDTO.getNombreArtista().trim().isEmpty()) {
            throw new DatosInvalidosException("El nombre del artista es obligatorio");
        }
        if (cancionDTO.getTituloAlbum() == null || cancionDTO.getTituloAlbum().trim().isEmpty()) {
            throw new DatosInvalidosException("El título del álbum es obligatorio");
        }

        // Buscar el artista por nombre
        Optional<Artista> artistaOpt = artistaRepository.findByNombre(cancionDTO.getNombreArtista());
        if (artistaOpt.isEmpty()) {
            throw new ArtistaNoEncontradoException(cancionDTO.getNombreArtista());
        }

        Artista artista = artistaOpt.get();

        // Buscar el álbum por título y artista
        List<Album> albumesDelArtista = albumRepository.findByArtista(artista);
        Optional<Album> albumOpt = albumesDelArtista.stream()
                .filter(album -> album.getTitulo().equalsIgnoreCase(cancionDTO.getTituloAlbum()))
                .findFirst();

        if (albumOpt.isEmpty()) {
            throw new AlbumNoEncontradoException(cancionDTO.getTituloAlbum(), cancionDTO.getNombreArtista());
        }

        Album album = albumOpt.get();

        // Crear la canción
        Cancion cancion = new Cancion();
        cancion.setTitulo(cancionDTO.getTitulo());
        cancion.setArtista(artista);
        cancion.setAlbum(album);
        cancion.setGenero(cancionDTO.getGenero());
        cancion.setAnio(cancionDTO.getAnio());
        cancion.setDuracion(cancionDTO.getDuracion());
        cancion.setURLCancion(cancionDTO.getURLCancion());
        // Tomar la portada del álbum
        cancion.setURLPortadaCancion(album.getURLPortadaAlbum());

        // Guardar la canción en la BD
        Cancion cancionGuardada = cancionRepository.save(cancion);

        // Agregar la canción a la lista de canciones del álbum
        album.getCanciones().agregar(cancionGuardada);
        albumRepository.save(album);

        return cancionGuardada;
    }

    /**
     * Obtiene todas las canciones registradas en el sistema.
     *
     * @return Lista de todas las canciones
     */
    public List<Cancion> obtenerCanciones() {
        return cancionRepository.findAll();
    }

    /**
     * Obtiene una canción por su ID.
     *
     * @param id ID de la canción
     * @return Optional con la canción si existe
     */
    public Optional<Cancion> obtenerCancionPorId(String id) {
        return cancionRepository.findById(id);
    }

    /**
     * Realiza una búsqueda avanzada de canciones con lógica AND/OR.
     * RF-004: Permite filtrar canciones por artista, género y año.
     *
     * @param busqueda Criterios de búsqueda con operador lógico
     * @return Lista de canciones que cumplen los criterios
     * @throws DatosInvalidosException Si los criterios son nulos
     */
    public List<Cancion> busquedaAvanzada(BusquedaAvanzadaDTO busqueda) {
        if (busqueda == null) {
            throw new DatosInvalidosException("Los criterios de búsqueda no pueden estar vacíos");
        }

        List<Cancion> todasLasCanciones = cancionRepository.findAll();
        String operador = busqueda.getOperadorLogico() != null ?
            busqueda.getOperadorLogico().toUpperCase() : "AND";

        if ("OR".equals(operador)) {
            // Lógica OR: la canción cumple al menos UN criterio
            return todasLasCanciones.stream()
                .filter(cancion -> {
                    boolean cumpleArtista = busqueda.getArtista() == null ||
                        busqueda.getArtista().trim().isEmpty() ||
                        (cancion.getArtista() != null &&
                         cancion.getArtista().getNombre().toLowerCase()
                            .contains(busqueda.getArtista().toLowerCase()));

                    boolean cumpleGenero = busqueda.getGenero() == null ||
                        busqueda.getGenero().trim().isEmpty() ||
                        (cancion.getGenero() != null &&
                         cancion.getGenero().toString().equalsIgnoreCase(busqueda.getGenero()));

                    boolean cumpleAnio = busqueda.getAnio() == null ||
                        cancion.getAnio() == busqueda.getAnio();

                    return cumpleArtista || cumpleGenero || cumpleAnio;
                })
                .collect(Collectors.toList());
        } else {
            // Lógica AND (por defecto): la canción cumple TODOS los criterios especificados
            return todasLasCanciones.stream()
                .filter(cancion -> {
                    boolean cumpleArtista = busqueda.getArtista() == null ||
                        busqueda.getArtista().trim().isEmpty() ||
                        (cancion.getArtista() != null &&
                         cancion.getArtista().getNombre().toLowerCase()
                            .contains(busqueda.getArtista().toLowerCase()));

                    boolean cumpleGenero = busqueda.getGenero() == null ||
                        busqueda.getGenero().trim().isEmpty() ||
                        (cancion.getGenero() != null &&
                         cancion.getGenero().toString().equalsIgnoreCase(busqueda.getGenero()));

                    boolean cumpleAnio = busqueda.getAnio() == null ||
                        cancion.getAnio() == busqueda.getAnio();

                    return cumpleArtista && cumpleGenero && cumpleAnio;
                })
                .collect(Collectors.toList());
        }
    }
}
