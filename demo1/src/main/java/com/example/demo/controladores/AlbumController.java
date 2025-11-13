package com.example.demo.controladores;

import com.example.demo.dto.AlbumRegistroDTO;
import com.example.demo.dto.AlbumRegistroPorNombreDTO;
import com.example.demo.dto.CargaMasivaResultadoDTO;
import com.example.demo.modelo.Album;
import com.example.demo.servicios.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Controlador REST para la gestión de álbumes musicales.
 * Proporciona endpoints para crear, consultar y administrar álbumes.
 */
@RestController
@RequestMapping("/albumes")
public class AlbumController {

    @Autowired
    private AlbumService albumService;

    /**
     * Guarda un nuevo álbum utilizando el ID del artista.
     *
     * @param albumDTO Datos del álbum a crear, incluyendo el ID del artista
     * @return ResponseEntity con el álbum creado
     */
    @PostMapping("/id")
    public ResponseEntity<Album> guardarAlbum(@RequestBody AlbumRegistroDTO albumDTO) {
        Album albumGuardado = albumService.guardarAlbum(albumDTO);
        return new ResponseEntity<>(albumGuardado, HttpStatus.CREATED);
    }

    /**
     * Guarda un nuevo álbum utilizando el nombre del artista.
     * Este endpoint es más intuitivo para el frontend ya que no requiere conocer el ID del artista.
     *
     * @param albumDTO Datos del álbum a crear, incluyendo el nombre del artista
     * @return ResponseEntity con el álbum creado
     */
    @PostMapping("/nombre")
    public ResponseEntity<Album> guardarAlbumPorNombre(@RequestBody AlbumRegistroPorNombreDTO albumDTO) {
        Album albumGuardado = albumService.guardarAlbumPorNombreArtista(albumDTO);
        return new ResponseEntity<>(albumGuardado, HttpStatus.CREATED);
    }

    /**
     * Obtiene la lista completa de todos los álbumes registrados en el sistema.
     *
     * @return ResponseEntity con la lista de álbumes
     */
    @GetMapping("/todos")
    public ResponseEntity<List<Album>> obtenerAlbumes() {
        List<Album> albumes = albumService.obtenerAlbumes();
        return new ResponseEntity<>(albumes, HttpStatus.OK);
    }

    /**
     * Busca un álbum por su nombre.
     *
     * @param nombre Nombre del álbum a buscar
     * @return ResponseEntity con el álbum encontrado
     */
    @GetMapping("/nombre")
    public ResponseEntity<Album> obtenerAlbumPorNombre(@RequestParam("nombre") String nombre) {
        Album album = albumService.obtenerAlbumPorNombre(nombre.trim()).get();
        return new ResponseEntity<>(album, HttpStatus.OK);
    }

    /**
     * Actualiza un álbum existente utilizando el nombre del artista.
     *
     * @param id ID del álbum a actualizar
     * @param albumDTO Datos actualizados del álbum
     * @return ResponseEntity con el álbum actualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<Album> actualizarAlbum(@PathVariable String id, @RequestBody AlbumRegistroPorNombreDTO albumDTO) {
        Album albumActualizado = albumService.actualizarAlbum(id, albumDTO);
        return new ResponseEntity<>(albumActualizado, HttpStatus.OK);
    }

    /**
     * Elimina un álbum por su ID.
     *
     * @param id ID del álbum a eliminar
     * @return ResponseEntity con estado NO_CONTENT si fue exitoso
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAlbum(@PathVariable String id) {
        albumService.eliminarAlbum(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Carga masiva de álbumes desde un archivo CSV.
     * Formato esperado: Titulo,Anio,NombreArtista,Genero,URLPortadaAlbum
     *
     * @param archivo Archivo CSV con los álbumes
     * @return ResponseEntity con el resultado de la carga
     */
    @PostMapping("/carga-masiva")
    public ResponseEntity<CargaMasivaResultadoDTO> cargarAlbumesDesdeCSV(
            @RequestParam("archivo") MultipartFile archivo) {
        CargaMasivaResultadoDTO resultado = albumService.cargarAlbumesDesdeCSV(archivo);
        return ResponseEntity.ok(resultado);
    }
}
