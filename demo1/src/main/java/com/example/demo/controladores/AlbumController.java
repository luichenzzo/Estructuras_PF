package com.example.demo.controladores;

import com.example.demo.dto.AlbumRegistroDTO;
import com.example.demo.dto.AlbumRegistroPorNombreDTO;
import com.example.demo.modelo.Album;
import com.example.demo.servicios.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
        Optional<Album> albumOpt = albumService.obtenerAlbumPorNombre(nombre.trim());
        return new ResponseEntity<>(albumOpt.get(), HttpStatus.OK);
    }
}
