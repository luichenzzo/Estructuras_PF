package com.example.demo.servicios;

import com.example.demo.dto.CancionRegistroDTO;
import com.example.demo.dto.CancionRegistroPorNombreDTO;
import com.example.demo.dto.BusquedaAvanzadaDTO;
import com.example.demo.dto.CargaMasivaResultadoDTO;
import com.example.demo.modelo.Album;
import com.example.demo.modelo.Artista;
import com.example.demo.modelo.Cancion;
import com.example.demo.modelo.GENERO;
import com.example.demo.repositorio.AlbumRepository;
import com.example.demo.repositorio.ArtistaRepository;
import com.example.demo.repositorio.CancionRepository;
import com.example.demo.excepciones.ArtistaNoEncontradoException;
import com.example.demo.excepciones.AlbumNoEncontradoException;
import com.example.demo.excepciones.DatosInvalidosException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
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

    /**
     * Carga masiva de canciones desde un archivo CSV.
     * Formato esperado del CSV: Titulo,NombreArtista,TituloAlbum,Genero,Anio,Duracion,URLCancion
     *
     * @param archivo Archivo CSV con las canciones
     * @return Resultado de la carga masiva con estadísticas y errores
     * @throws DatosInvalidosException Si el archivo está vacío o tiene formato incorrecto
     */
    public CargaMasivaResultadoDTO cargarCancionesDesdeCSV(MultipartFile archivo) {
        CargaMasivaResultadoDTO resultado = new CargaMasivaResultadoDTO();

        if (archivo == null || archivo.isEmpty()) {
            throw new DatosInvalidosException("El archivo CSV no puede estar vacío");
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(archivo.getInputStream(), StandardCharsets.UTF_8))) {

            String linea;
            int numeroLinea = 0;
            boolean esPrimeraLinea = true;

            while ((linea = reader.readLine()) != null) {
                numeroLinea++;

                // Saltar la línea de encabezados
                if (esPrimeraLinea) {
                    esPrimeraLinea = false;
                    continue;
                }

                // Saltar líneas vacías
                if (linea.trim().isEmpty()) {
                    continue;
                }

                resultado.setTotalProcesadas(resultado.getTotalProcesadas() + 1);

                try {
                    // Parsear la línea CSV (considerando valores entre comillas)
                    String[] valores = parsearLineaCSV(linea);

                    if (valores.length < 7) {
                        resultado.agregarError(numeroLinea, "Formato incorrecto. Se esperan 7 columnas");
                        resultado.setFallidas(resultado.getFallidas() + 1);
                        continue;
                    }

                    // Crear DTO con los datos del CSV
                    CancionRegistroPorNombreDTO cancionDTO = new CancionRegistroPorNombreDTO();
                    cancionDTO.setTitulo(valores[0].trim());
                    cancionDTO.setNombreArtista(valores[1].trim());
                    cancionDTO.setTituloAlbum(valores[2].trim());

                    // Parsear género
                    try {
                        cancionDTO.setGenero(GENERO.valueOf(valores[3].trim().toUpperCase()));
                    } catch (IllegalArgumentException e) {
                        resultado.agregarError(numeroLinea, "Género inválido: " + valores[3]);
                        resultado.setFallidas(resultado.getFallidas() + 1);
                        continue;
                    }

                    // Parsear año
                    try {
                        cancionDTO.setAnio(Integer.parseInt(valores[4].trim()));
                    } catch (NumberFormatException e) {
                        resultado.agregarError(numeroLinea, "Año inválido: " + valores[4]);
                        resultado.setFallidas(resultado.getFallidas() + 1);
                        continue;
                    }

                    // Parsear duración
                    try {
                        cancionDTO.setDuracion(Double.parseDouble(valores[5].trim()));
                    } catch (NumberFormatException e) {
                        resultado.agregarError(numeroLinea, "Duración inválida: " + valores[5]);
                        resultado.setFallidas(resultado.getFallidas() + 1);
                        continue;
                    }

                    cancionDTO.setURLCancion(valores[6].trim());

                    // Intentar guardar la canción
                    guardarCancionPorNombres(cancionDTO);
                    resultado.setExitosas(resultado.getExitosas() + 1);

                } catch (ArtistaNoEncontradoException e) {
                    resultado.agregarError(numeroLinea, "Artista no encontrado: " + e.getMessage());
                    resultado.setFallidas(resultado.getFallidas() + 1);
                } catch (AlbumNoEncontradoException e) {
                    resultado.agregarError(numeroLinea, "Álbum no encontrado: " + e.getMessage());
                    resultado.setFallidas(resultado.getFallidas() + 1);
                } catch (DatosInvalidosException e) {
                    resultado.agregarError(numeroLinea, e.getMessage());
                    resultado.setFallidas(resultado.getFallidas() + 1);
                } catch (Exception e) {
                    resultado.agregarError(numeroLinea, "Error inesperado: " + e.getMessage());
                    resultado.setFallidas(resultado.getFallidas() + 1);
                }
            }

        } catch (Exception e) {
            throw new DatosInvalidosException("Error al procesar el archivo CSV: " + e.getMessage());
        }

        return resultado;
    }

    /**
     * Parsea una línea CSV manejando valores entre comillas.
     *
     * @param linea Línea del CSV
     * @return Array con los valores parseados
     */
    private String[] parsearLineaCSV(String linea) {
        List<String> valores = new ArrayList<>();
        StringBuilder valorActual = new StringBuilder();
        boolean dentroDeComillas = false;

        for (int i = 0; i < linea.length(); i++) {
            char c = linea.charAt(i);

            if (c == '"') {
                // Manejar comillas dobles escapadas
                if (i + 1 < linea.length() && linea.charAt(i + 1) == '"') {
                    valorActual.append('"');
                    i++; // Saltar la siguiente comilla
                } else {
                    dentroDeComillas = !dentroDeComillas;
                }
            } else if (c == ',' && !dentroDeComillas) {
                valores.add(valorActual.toString());
                valorActual = new StringBuilder();
            } else {
                valorActual.append(c);
            }
        }

        // Agregar el último valor
        valores.add(valorActual.toString());

        return valores.toArray(new String[0]);
    }

    /**
     * Actualiza una canción existente utilizando los nombres del artista y álbum.
     *
     * @param id ID de la canción a actualizar
     * @param cancionDTO Datos actualizados de la canción
     * @return Canción actualizada
     * @throws com.example.demo.excepciones.RecursoNoEncontradoException Si no existe la canción
     */
    public Cancion actualizarCancion(String id, CancionRegistroPorNombreDTO cancionDTO) {
        // Validar ID
        if (id == null || id.trim().isEmpty()) {
            throw new DatosInvalidosException("El ID de la canción no puede estar vacío");
        }

        // Buscar la canción existente
        Optional<Cancion> cancionOpt = cancionRepository.findById(id);
        if (cancionOpt.isEmpty()) {
            throw new com.example.demo.excepciones.RecursoNoEncontradoException("No se encontró la canción con ID: " + id);
        }

        Cancion cancionExistente = cancionOpt.get();
        Album albumAnterior = cancionExistente.getAlbum();

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

        Album albumNuevo = albumOpt.get();

        // Actualizar los datos de la canción
        cancionExistente.setTitulo(cancionDTO.getTitulo());
        cancionExistente.setArtista(artista);
        cancionExistente.setAlbum(albumNuevo);
        cancionExistente.setGenero(cancionDTO.getGenero());
        cancionExistente.setAnio(cancionDTO.getAnio());
        cancionExistente.setDuracion(cancionDTO.getDuracion());
        cancionExistente.setURLCancion(cancionDTO.getURLCancion());
        cancionExistente.setURLPortadaCancion(albumNuevo.getURLPortadaAlbum());

        // Guardar la canción actualizada
        Cancion cancionActualizada = cancionRepository.save(cancionExistente);

        // Si cambió de álbum, actualizar las listas de canciones
        if (!albumAnterior.getId().equals(albumNuevo.getId())) {
            // Remover del álbum anterior
            albumAnterior.getCanciones().eliminar(cancionExistente);
            albumRepository.save(albumAnterior);

            // Agregar al nuevo álbum
            albumNuevo.getCanciones().agregar(cancionActualizada);
            albumRepository.save(albumNuevo);
        }

        return cancionActualizada;
    }

    /**
     * Elimina una canción por su ID.
     *
     * @param id ID de la canción a eliminar
     * @throws com.example.demo.excepciones.RecursoNoEncontradoException Si no existe la canción
     */
    public void eliminarCancion(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new DatosInvalidosException("El ID de la canción no puede estar vacío");
        }

        Optional<Cancion> cancionOpt = cancionRepository.findById(id);
        if (cancionOpt.isEmpty()) {
            throw new com.example.demo.excepciones.RecursoNoEncontradoException("No se encontró la canción con ID: " + id);
        }

        cancionRepository.deleteById(id);
    }
}
