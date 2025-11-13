package com.example.demo.servicios;

import com.example.demo.dto.AlbumRegistroDTO;
import com.example.demo.dto.AlbumRegistroPorNombreDTO;
import com.example.demo.modelo.Album;
import com.example.demo.modelo.Artista;
import com.example.demo.repositorio.AlbumRepository;
import com.example.demo.repositorio.ArtistaRepository;
import com.example.demo.estructuras.ListaEnlazada;
import com.example.demo.excepciones.ArtistaNoEncontradoException;
import com.example.demo.excepciones.AlbumNoEncontradoException;
import com.example.demo.excepciones.DatosInvalidosException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de álbumes musicales.
 * Proporciona lógica de negocio para crear y consultar álbumes.
 */
@Service
public class AlbumService {

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private ArtistaRepository artistaRepository;

    /**
     * Guarda un nuevo álbum utilizando el ID del artista.
     *
     * @param albumDTO Datos del álbum incluyendo ID del artista
     * @return Álbum guardado
     * @throws DatosInvalidosException Si el título del álbum está vacío
     * @throws ArtistaNoEncontradoException Si el artista no existe
     */
    public Album guardarAlbum(AlbumRegistroDTO albumDTO) {
        // Validar datos
        if (albumDTO.getTitulo() == null || albumDTO.getTitulo().trim().isEmpty()) {
            throw new DatosInvalidosException("El título del álbum es obligatorio");
        }

        // Buscar el artista por ID
        Optional<Artista> artistaOpt = artistaRepository.findById(albumDTO.getArtistaId());

        if (artistaOpt.isEmpty()) {
            throw new ArtistaNoEncontradoException(albumDTO.getArtistaId());
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

    /**
     * Guarda un nuevo álbum utilizando el nombre del artista en lugar del ID.
     *
     * @param albumDTO Datos del álbum incluyendo nombre del artista
     * @return Álbum guardado
     * @throws DatosInvalidosException Si faltan datos obligatorios
     * @throws ArtistaNoEncontradoException Si el artista no existe
     */
    public Album guardarAlbumPorNombreArtista(AlbumRegistroPorNombreDTO albumDTO) {
        // Validar datos
        if (albumDTO.getTitulo() == null || albumDTO.getTitulo().trim().isEmpty()) {
            throw new DatosInvalidosException("El título del álbum es obligatorio");
        }
        if (albumDTO.getNombreArtista() == null || albumDTO.getNombreArtista().trim().isEmpty()) {
            throw new DatosInvalidosException("El nombre del artista es obligatorio");
        }

        // Buscar el artista por nombre
        Optional<Artista> artistaOpt = artistaRepository.findByNombre(albumDTO.getNombreArtista());

        if (artistaOpt.isEmpty()) {
            throw new ArtistaNoEncontradoException(albumDTO.getNombreArtista());
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

    /**
     * Obtiene todos los álbumes registrados en el sistema.
     *
     * @return Lista de todos los álbumes
     */
    public List<Album> obtenerAlbumes() {
        return albumRepository.findAll();
    }

    /**
     * Busca un álbum por su nombre.
     *
     * @param nombre Nombre del álbum a buscar
     * @return Optional con el álbum encontrado
     * @throws DatosInvalidosException Si el nombre está vacío
     * @throws AlbumNoEncontradoException Si no se encuentra el álbum
     */
    public Optional<Album> obtenerAlbumPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new DatosInvalidosException("El nombre del álbum no puede estar vacío");
        }

        List<Album> resultados = albumRepository.findByTituloContainingIgnoreCase(nombre.trim());
        if (resultados == null || resultados.isEmpty()) {
            throw new AlbumNoEncontradoException(nombre);
        }
        return Optional.of(resultados.get(0));
    }

    /**
     * Actualiza un álbum existente utilizando el nombre del artista.
     *
     * @param id ID del álbum a actualizar
     * @param albumDTO Datos actualizados del álbum
     * @return Álbum actualizado
     * @throws com.example.demo.excepciones.RecursoNoEncontradoException Si no existe el álbum
     */
    public Album actualizarAlbum(String id, AlbumRegistroPorNombreDTO albumDTO) {
        // Validar ID
        if (id == null || id.trim().isEmpty()) {
            throw new DatosInvalidosException("El ID del álbum no puede estar vacío");
        }

        // Buscar el álbum existente
        Optional<Album> albumOpt = albumRepository.findById(id);
        if (albumOpt.isEmpty()) {
            throw new com.example.demo.excepciones.RecursoNoEncontradoException("No se encontró el álbum con ID: " + id);
        }

        Album albumExistente = albumOpt.get();
        Artista artistaAnterior = albumExistente.getArtista();

        // Validar datos
        if (albumDTO.getTitulo() == null || albumDTO.getTitulo().trim().isEmpty()) {
            throw new DatosInvalidosException("El título del álbum es obligatorio");
        }
        if (albumDTO.getNombreArtista() == null || albumDTO.getNombreArtista().trim().isEmpty()) {
            throw new DatosInvalidosException("El nombre del artista es obligatorio");
        }

        // Buscar el artista por nombre
        Optional<Artista> artistaOpt = artistaRepository.findByNombre(albumDTO.getNombreArtista());
        if (artistaOpt.isEmpty()) {
            throw new ArtistaNoEncontradoException(albumDTO.getNombreArtista());
        }

        Artista artistaNuevo = artistaOpt.get();

        // Actualizar los datos del álbum
        albumExistente.setTitulo(albumDTO.getTitulo());
        albumExistente.setAnio(albumDTO.getAnio());
        albumExistente.setArtista(artistaNuevo);
        albumExistente.setGenero(albumDTO.getGenero());
        albumExistente.setURLPortadaAlbum(albumDTO.getURLPortadaAlbum());

        // Guardar el álbum actualizado
        Album albumActualizado = albumRepository.save(albumExistente);

        // Si cambió de artista, actualizar las listas
        if (!artistaAnterior.getId().equals(artistaNuevo.getId())) {
            // Remover del artista anterior
            artistaAnterior.getAlbumes().eliminar(albumExistente);
            artistaRepository.save(artistaAnterior);

            // Agregar al nuevo artista
            artistaNuevo.getAlbumes().agregar(albumActualizado);
            artistaRepository.save(artistaNuevo);
        }

        return albumActualizado;
    }

    /**
     * Elimina un álbum por su ID.
     *
     * @param id ID del álbum a eliminar
     * @throws com.example.demo.excepciones.RecursoNoEncontradoException Si no existe el álbum
     */
    public void eliminarAlbum(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new DatosInvalidosException("El ID del álbum no puede estar vacío");
        }

        Optional<Album> albumOpt = albumRepository.findById(id);
        if (albumOpt.isEmpty()) {
            throw new com.example.demo.excepciones.RecursoNoEncontradoException("No se encontró el álbum con ID: " + id);
        }

        albumRepository.deleteById(id);
    }
}
