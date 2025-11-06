package com.example.demo.servicios;

import com.example.demo.dto.CancionRegistroDTO;
import com.example.demo.dto.CancionRegistroPorNombreDTO;
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

@Service
public class CancionService {

    @Autowired
    private CancionRepository cancionRepository;

    @Autowired
    private ArtistaRepository artistaRepository;

    @Autowired
    private AlbumRepository albumRepository;

    // Método 1: Guardar canción usando IDs
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

    // Método 2: Guardar canción usando NOMBRES (más lógico para el front)
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

    public List<Cancion> obtenerCanciones() {

        return cancionRepository.findAll();
    }
}
