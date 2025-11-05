package com.example.demo.servicios;

import com.example.demo.dto.CancionRegistroDTO;
import com.example.demo.dto.CancionRegistroPorNombreDTO;
import com.example.demo.modelo.Album;
import com.example.demo.modelo.Artista;
import com.example.demo.modelo.Cancion;
import com.example.demo.repositorio.AlbumRepository;
import com.example.demo.repositorio.ArtistaRepository;
import com.example.demo.repositorio.CancionRepository;
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
        // Buscar el artista por ID
        Optional<Artista> artistaOpt = artistaRepository.findById(cancionDTO.getArtistaId());
        if (artistaOpt.isEmpty()) {
            throw new RuntimeException("Artista no encontrado con ID: " + cancionDTO.getArtistaId());
        }

        // Buscar el álbum por ID
        Optional<Album> albumOpt = albumRepository.findById(cancionDTO.getAlbumId());
        if (albumOpt.isEmpty()) {
            throw new RuntimeException("Álbum no encontrado con ID: " + cancionDTO.getAlbumId());
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
        // Buscar el artista por nombre
        Optional<Artista> artistaOpt = artistaRepository.findByNombre(cancionDTO.getNombreArtista());
        if (artistaOpt.isEmpty()) {
            throw new RuntimeException("Artista no encontrado con nombre: " + cancionDTO.getNombreArtista());
        }

        Artista artista = artistaOpt.get();

        // Buscar el álbum por título y artista
        List<Album> albumesDelArtista = albumRepository.findByArtista(artista);
        Optional<Album> albumOpt = albumesDelArtista.stream()
                .filter(album -> album.getTitulo().equalsIgnoreCase(cancionDTO.getTituloAlbum()))
                .findFirst();

        if (albumOpt.isEmpty()) {
            throw new RuntimeException("Álbum no encontrado con título: " + cancionDTO.getTituloAlbum() 
                    + " para el artista: " + cancionDTO.getNombreArtista());
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
}

