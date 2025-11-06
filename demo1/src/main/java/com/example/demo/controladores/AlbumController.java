package com.example.demo.controladores;

import com.example.demo.dto.AlbumRegistroDTO;
import com.example.demo.dto.AlbumRegistroPorNombreDTO;
import com.example.demo.estructuras.Lista;
import com.example.demo.modelo.Album;
import com.example.demo.modelo.Usuario;
import com.example.demo.servicios.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/albumes")
public class AlbumController {

    @Autowired
    private AlbumService albumService;

    // Endpoint original: Recibe el ID del artista
    @PostMapping("/id")
    public ResponseEntity<?> guardarAlbum(@RequestBody AlbumRegistroDTO albumDTO) {
        try {
            Album albumGuardado = albumService.guardarAlbum(albumDTO);
            return new ResponseEntity<>(albumGuardado, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno del servidor", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Nuevo endpoint: Recibe el NOMBRE del artista (más lógico para el front)
    @PostMapping("/nombre")
    public ResponseEntity<?> guardarAlbumPorNombre(@RequestBody AlbumRegistroPorNombreDTO albumDTO) {
        try {
            Album albumGuardado = albumService.guardarAlbumPorNombreArtista(albumDTO);
            return new ResponseEntity<>(albumGuardado, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno del servidor", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/todos")
    public List<Album> obtenerAlbumes() {
        return albumService.obtenerAlbumes();
    }


    @GetMapping("/nombre")
    public ResponseEntity<?> obtenerAlbumPorNombre(@RequestParam("nombre") String nombre) {
        try {
            if (nombre == null || nombre.trim().isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("El parámetro 'nombre' es requerido y no puede estar vacío");
            }

            Optional<Album> albumOpt = albumService.obtenerAlbumPorNombre(nombre.trim());
            return albumOpt
                    .<ResponseEntity<?>>map(album -> new ResponseEntity<>(album, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>("Álbum no encontrado", HttpStatus.NOT_FOUND));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno del servidor", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
