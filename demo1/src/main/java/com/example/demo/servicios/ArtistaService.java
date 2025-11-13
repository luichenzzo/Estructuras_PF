package com.example.demo.servicios;

import com.example.demo.dto.ArtistaRegistroDTO;
import com.example.demo.dto.CargaMasivaResultadoDTO;
import com.example.demo.modelo.Artista;
import com.example.demo.repositorio.ArtistaRepository;
import com.example.demo.estructuras.ListaDoblementeEnlazada;
import com.example.demo.excepciones.DatosInvalidosException;
import com.example.demo.excepciones.RecursoDuplicadoException;
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

    /**
     * Actualiza un artista existente.
     *
     * @param id ID del artista a actualizar
     * @param artistaDTO Datos actualizados del artista
     * @return Artista actualizado
     * @throws com.example.demo.excepciones.RecursoNoEncontradoException Si no existe el artista
     */
    public Artista actualizarArtista(String id, ArtistaRegistroDTO artistaDTO) {
        // Validar ID
        if (id == null || id.trim().isEmpty()) {
            throw new DatosInvalidosException("El ID del artista no puede estar vacío");
        }

        // Buscar el artista existente
        Optional<Artista> artistaOpt = artistaRepository.findById(id);
        if (artistaOpt.isEmpty()) {
            throw new com.example.demo.excepciones.RecursoNoEncontradoException("No se encontró el artista con ID: " + id);
        }

        Artista artistaExistente = artistaOpt.get();

        // Validar datos
        if (artistaDTO.getNombre() == null || artistaDTO.getNombre().trim().isEmpty()) {
            throw new DatosInvalidosException("El nombre del artista es obligatorio");
        }

        // Verificar si el nuevo nombre ya existe (solo si cambió el nombre)
        if (!artistaExistente.getNombre().equals(artistaDTO.getNombre())) {
            Optional<Artista> artistaDuplicado = artistaRepository.findByNombre(artistaDTO.getNombre());
            if (artistaDuplicado.isPresent()) {
                throw new RecursoDuplicadoException("Ya existe un artista con el nombre: " + artistaDTO.getNombre());
            }
        }

        // Actualizar los datos
        artistaExistente.setNombre(artistaDTO.getNombre());
        artistaExistente.setNacionalidad(artistaDTO.getNacionalidad());
        artistaExistente.setGeneroPrincipal(artistaDTO.getGeneroPrincipal());
        artistaExistente.setGeneroSecundario(artistaDTO.getGeneroSecundario());
        artistaExistente.setURLFotoArtista(artistaDTO.getURLFotoArtista());

        return artistaRepository.save(artistaExistente);
    }

    /**
     * Elimina un artista por su ID.
     *
     * @param id ID del artista a eliminar
     * @throws com.example.demo.excepciones.RecursoNoEncontradoException Si no existe el artista
     */
    public void eliminarArtista(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new com.example.demo.excepciones.DatosInvalidosException("El ID del artista no puede estar vacío");
        }

        Optional<Artista> artistaOpt = artistaRepository.findById(id);
        if (artistaOpt.isEmpty()) {
            throw new com.example.demo.excepciones.RecursoNoEncontradoException("No se encontró el artista con ID: " + id);
        }

        artistaRepository.deleteById(id);
    }

    /**
     * Carga masiva de artistas desde un archivo CSV.
     * Formato esperado: Nombre,Nacionalidad,GeneroPrincipal,GeneroSecundario,URLFotoArtista
     *
     * @param archivo Archivo CSV con los artistas
     * @return Resultado de la carga con estadísticas y errores
     */
    public CargaMasivaResultadoDTO cargarArtistasDesdeCSV(MultipartFile archivo) {
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
                        resultado.agregarError(numeroLinea, "El nombre del artista no puede estar vacío");
                        continue;
                    }

                    // Verificar si el artista ya existe
                    Optional<Artista> artistaExistente = artistaRepository.findByNombre(campos[0].trim());
                    if (artistaExistente.isPresent()) {
                        resultado.agregarError(numeroLinea, "Artista duplicado: " + campos[0].trim());
                        continue;
                    }

                    // Crear y guardar artista
                    Artista artista = new Artista();
                    artista.setNombre(campos[0].trim());
                    artista.setNacionalidad(campos[1].trim());
                    
                    // Convertir géneros a ENUM
                    try {
                        artista.setGeneroPrincipal(com.example.demo.modelo.GENERO.valueOf(campos[2].trim().toUpperCase()));
                    } catch (IllegalArgumentException e) {
                        resultado.agregarError(numeroLinea, "Género principal no válido: " + campos[2].trim());
                        continue;
                    }
                    
                    // GeneroSecundario puede ser vacío
                    if (!campos[3].trim().isEmpty()) {
                        try {
                            artista.setGeneroSecundario(com.example.demo.modelo.GENERO.valueOf(campos[3].trim().toUpperCase()));
                        } catch (IllegalArgumentException e) {
                            resultado.agregarError(numeroLinea, "Género secundario no válido: " + campos[3].trim());
                            continue;
                        }
                    }
                    
                    artista.setURLFotoArtista(campos[4].trim());
                    artista.setAlbumes(new ListaDoblementeEnlazada<>());

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
