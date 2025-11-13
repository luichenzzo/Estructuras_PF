package com.example.demo.servicios;

import com.example.demo.dto.AlbumRegistroDTO;
import com.example.demo.dto.AlbumRegistroPorNombreDTO;
import com.example.demo.dto.CargaMasivaResultadoDTO;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
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

    /**
     * Carga masiva de álbumes desde un archivo CSV.
     * Formato esperado: Titulo,Anio,NombreArtista,Genero,URLPortadaAlbum
     *
     * @param archivo Archivo CSV con los álbumes
     * @return Resultado de la carga con estadísticas y errores
     */
    public CargaMasivaResultadoDTO cargarAlbumesDesdeCSV(MultipartFile archivo) {
        CargaMasivaResultadoDTO resultado = new CargaMasivaResultadoDTO();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(archivo.getInputStream()))) {
            String linea;
            int numeroLinea = 0;
            reader.readLine(); // Saltar header

            while ((linea = reader.readLine()) != null) {
                numeroLinea++;
                resultado.setTotalProcesadas(resultado.getTotalProcesadas() + 1);

                try {
                    String[] campos = parsearLineaCSV(linea);

                    // Validar número de campos
                    if (campos.length != 5) {
                        resultado.agregarError(numeroLinea, "Formato inválido. Se esperan 5 campos, se encontraron " + campos.length);
                        continue;
                    }

                    // Validar campos vacíos
                    if (campos[0].trim().isEmpty()) {
                        resultado.agregarError(numeroLinea, "El título del álbum no puede estar vacío");
                        continue;
                    }

                    if (campos[2].trim().isEmpty()) {
                        resultado.agregarError(numeroLinea, "El nombre del artista no puede estar vacío");
                        continue;
                    }

                    // Buscar el artista
                    String nombreArtista = campos[2].trim();
                    Optional<Artista> artistaOpt = artistaRepository.findByNombre(nombreArtista);
                    
                    if (artistaOpt.isEmpty()) {
                        resultado.agregarError(numeroLinea, "Artista no encontrado: " + nombreArtista);
                        continue;
                    }

                    Artista artista = artistaOpt.get();

                    // Verificar si el álbum ya existe para este artista
                    String tituloAlbum = campos[0].trim();
                    Optional<Album> albumExistente = albumRepository.findByTituloAndArtista(tituloAlbum, artista);
                    if (albumExistente.isPresent()) {
                        resultado.agregarError(numeroLinea, "Álbum duplicado: " + tituloAlbum + " de " + nombreArtista);
                        continue;
                    }

                    // Parsear año
                    int anio;
                    try {
                        anio = Integer.parseInt(campos[1].trim());
                    } catch (NumberFormatException e) {
                        resultado.agregarError(numeroLinea, "Año inválido: " + campos[1].trim());
                        continue;
                    }

                    // Convertir género a ENUM
                    com.example.demo.modelo.GENERO genero;
                    try {
                        genero = com.example.demo.modelo.GENERO.valueOf(campos[3].trim().toUpperCase());
                    } catch (IllegalArgumentException e) {
                        resultado.agregarError(numeroLinea, "Género no válido: " + campos[3].trim());
                        continue;
                    }

                    // Crear y guardar álbum
                    Album album = new Album();
                    album.setTitulo(tituloAlbum);
                    album.setAnio(anio);
                    album.setArtista(artista);
                    album.setGenero(genero);
                    album.setURLPortadaAlbum(campos[4].trim());
                    album.setCanciones(new ListaEnlazada<>());

                    Album albumGuardado = albumRepository.save(album);

                    // Agregar el álbum a la lista del artista
                    artista.getAlbumes().agregar(albumGuardado);
                    artistaRepository.save(artista);

                    resultado.setExitosas(resultado.getExitosas() + 1);

                } catch (Exception e) {
                    resultado.agregarError(numeroLinea, e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo CSV: " + e.getMessage(), e);
        }

        return resultado;
    }

    /**
     * Parsea una línea CSV manejando campos con comillas.
     *
     * @param linea Línea CSV a parsear
     * @return Array de campos
     */
    private String[] parsearLineaCSV(String linea) {
        List<String> campos = new ArrayList<>();
        StringBuilder campoActual = new StringBuilder();
        boolean dentroComillas = false;

        for (char c : linea.toCharArray()) {
            if (c == '"') {
                dentroComillas = !dentroComillas;
            } else if (c == ',' && !dentroComillas) {
                campos.add(campoActual.toString());
                campoActual = new StringBuilder();
            } else {
                campoActual.append(c);
            }
        }
        campos.add(campoActual.toString());

        return campos.toArray(new String[0]);
    }
}
