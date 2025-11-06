package com.example.demo.servicios;

import com.example.demo.dto.AlbumRegistroDTO;
import com.example.demo.dto.AlbumRegistroPorNombreDTO;
import com.example.demo.modelo.Album;
import com.example.demo.modelo.Artista;
import com.example.demo.repositorio.AlbumRepository;
import com.example.demo.repositorio.ArtistaRepository;
import com.example.demo.estructuras.ListaEnlazada;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlbumService {

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private ArtistaRepository artistaRepository;

    // Método original: guardar álbum usando el ID del artista
    public Album guardarAlbum(AlbumRegistroDTO albumDTO) {
        // Buscar el artista por ID
        Optional<Artista> artistaOpt = artistaRepository.findById(albumDTO.getArtistaId());

        if (artistaOpt.isEmpty()) {
            throw new RuntimeException("Artista no encontrado con ID: " + albumDTO.getArtistaId());
        }

        Artista artista = artistaOpt.get();

        // Crear el álbum
        Album album = new Album();
        album.setTitulo(albumDTO.getTitulo());
        album.setAnio(albumDTO.getAnio());
        album.setArtista(artista);
        album.setGenero(albumDTO.getGenero());
        album.setURLPortadaAlbum(albumDTO.getURLPortadaAlbum());
        album.setCanciones(new ListaEnlazada<>());

        // Guardar el álbum primero en la BD
        Album albumGuardado = albumRepository.save(album);

        // Agregar el álbum a la lista de álbumes del artista
        artista.getAlbumes().agregar(albumGuardado);
        artistaRepository.save(artista);

        return albumGuardado;
    }

    // Nuevo método: guardar álbum usando el NOMBRE del artista (más lógico para el front)
    public Album guardarAlbumPorNombreArtista(AlbumRegistroPorNombreDTO albumDTO) {
        // Buscar el artista por nombre
        Optional<Artista> artistaOpt = artistaRepository.findByNombre(albumDTO.getNombreArtista());

        if (artistaOpt.isEmpty()) {
            throw new RuntimeException("Artista no encontrado con nombre: " + albumDTO.getNombreArtista());
        }

        Artista artista = artistaOpt.get();

        // Crear el álbum
        Album album = new Album();
        album.setTitulo(albumDTO.getTitulo());
        album.setAnio(albumDTO.getAnio());
        album.setArtista(artista);
        album.setGenero(albumDTO.getGenero());
        album.setURLPortadaAlbum(albumDTO.getURLPortadaAlbum());
        album.setCanciones(new ListaEnlazada<>());

        // Guardar el álbum primero en la BD
        Album albumGuardado = albumRepository.save(album);

        // Agregar el álbum a la lista de álbumes del artista
        artista.getAlbumes().agregar(albumGuardado);
        artistaRepository.save(artista);

        return albumGuardado;
    }

    public List<Album> obtenerAlbumes() {
        return albumRepository.findAll();

    }

    // java
    public Optional<Album> obtenerAlbumPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return Optional.empty();
        }

        List<Album> resultados = albumRepository.findByTituloContainingIgnoreCase(nombre.trim());
        if (resultados == null || resultados.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(resultados.get(0));
    }


}
