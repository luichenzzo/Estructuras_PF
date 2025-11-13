package com.example.demo.controladores;

import com.example.demo.dto.BusquedaAvanzadaDTO;
import com.example.demo.dto.CancionRegistroDTO;
import com.example.demo.dto.CancionRegistroPorNombreDTO;
import com.example.demo.dto.CargaMasivaResultadoDTO;
import com.example.demo.modelo.Cancion;
import com.example.demo.servicios.CancionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Controlador REST para la gestión de canciones.
 * Proporciona endpoints para crear, consultar y buscar canciones.
 */
@RestController
@RequestMapping("/canciones")
public class CancionController {

    @Autowired
    private CancionService cancionService;

    /**
     * Registra una nueva canción utilizando los IDs del artista y álbum.
     *
     * @param cancionDTO Datos de la canción incluyendo IDs del artista y álbum
     * @return ResponseEntity con la canción creada
     */
    @PostMapping
    public ResponseEntity<Cancion> guardarCancion(@RequestBody CancionRegistroDTO cancionDTO) {
        Cancion cancionGuardada = cancionService.guardarCancion(cancionDTO);
        return new ResponseEntity<>(cancionGuardada, HttpStatus.CREATED);
    }

    /**
     * Registra una nueva canción utilizando los nombres del artista y álbum.
     * Este endpoint es más intuitivo para el frontend al no requerir IDs.
     *
     * @param cancionDTO Datos de la canción incluyendo nombres del artista y álbum
     * @return ResponseEntity con la canción creada
     */
    @PostMapping("/por-nombres")
    public ResponseEntity<Cancion> guardarCancionPorNombres(@RequestBody CancionRegistroPorNombreDTO cancionDTO) {
        Cancion cancionGuardada = cancionService.guardarCancionPorNombres(cancionDTO);
        return new ResponseEntity<>(cancionGuardada, HttpStatus.CREATED);
    }

    /**
     * Obtiene la lista completa de todas las canciones registradas.
     *
     * @return ResponseEntity con la lista de canciones
     */
    @GetMapping
    public ResponseEntity<List<Cancion>> obtenerCanciones() {
        List<Cancion> canciones = cancionService.obtenerCanciones();
        return new ResponseEntity<>(canciones, HttpStatus.OK);
    }

    /**
     * Obtiene una canción específica por su ID.
     *
     * @param id ID de la canción
     * @return ResponseEntity con la canción encontrada
     */
    @GetMapping("/{id}")
    public ResponseEntity<Cancion> obtenerCancionPorId(@PathVariable String id) {
        return cancionService.obtenerCancionPorId(id)
                .map(cancion -> new ResponseEntity<>(cancion, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Realiza una búsqueda avanzada de canciones con lógica AND/OR.
     *
     * @param busqueda Criterios de búsqueda con operador lógico (AND/OR)
     * @return ResponseEntity con la lista de canciones que cumplen los criterios
     */
    @PostMapping("/busqueda-avanzada")
    public ResponseEntity<List<Cancion>> busquedaAvanzada(@RequestBody BusquedaAvanzadaDTO busqueda) {
        List<Cancion> resultados = cancionService.busquedaAvanzada(busqueda);
        return new ResponseEntity<>(resultados, HttpStatus.OK);
    }

    /**
     * Carga masiva de canciones desde un archivo CSV.
     * Formato esperado del CSV: Titulo,NombreArtista,TituloAlbum,Genero,Anio,Duracion,URLCancion
     *
     * @param archivo Archivo CSV con las canciones
     * @return ResponseEntity con el resultado de la carga masiva
     */
    @PostMapping("/carga-masiva")
    public ResponseEntity<CargaMasivaResultadoDTO> cargarCancionesDesdeCSV(
            @RequestParam("archivo") MultipartFile archivo) {
        CargaMasivaResultadoDTO resultado = cancionService.cargarCancionesDesdeCSV(archivo);
        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }

    /**
     * Actualiza una canción existente utilizando los nombres del artista y álbum.
     *
     * @param id ID de la canción a actualizar
     * @param cancionDTO Datos actualizados de la canción
     * @return ResponseEntity con la canción actualizada
     */
    @PutMapping("/{id}")
    public ResponseEntity<Cancion> actualizarCancion(@PathVariable String id, @RequestBody CancionRegistroPorNombreDTO cancionDTO) {
        Cancion cancionActualizada = cancionService.actualizarCancion(id, cancionDTO);
        return new ResponseEntity<>(cancionActualizada, HttpStatus.OK);
    }

    /**
     * Elimina una canción por su ID.
     *
     * @param id ID de la canción a eliminar
     * @return ResponseEntity con estado NO_CONTENT si fue exitoso
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCancion(@PathVariable String id) {
        cancionService.eliminarCancion(id);
        return ResponseEntity.noContent().build();
    }
}
