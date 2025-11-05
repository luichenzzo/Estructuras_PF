package com.example.demo.controladores;

import com.example.demo.dto.AlbumRegistroDTO;
import com.example.demo.dto.AlbumRegistroPorNombreDTO;
import com.example.demo.modelo.Album;
import com.example.demo.servicios.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
