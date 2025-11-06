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

@RestController
@RequestMapping("/albumes")
public class AlbumController {

    @Autowired
    private AlbumService albumService;

    // Endpoint original: Recibe el ID del artista
    @PostMapping("/id")
    public ResponseEntity<Album> guardarAlbum(@RequestBody AlbumRegistroDTO albumDTO) {
        Album albumGuardado = albumService.guardarAlbum(albumDTO);
        return new ResponseEntity<>(albumGuardado, HttpStatus.CREATED);
    }

    // Nuevo endpoint: Recibe el NOMBRE del artista (más lógico para el front)
    @PostMapping("/nombre")
    public ResponseEntity<Album> guardarAlbumPorNombre(@RequestBody AlbumRegistroPorNombreDTO albumDTO) {
        Album albumGuardado = albumService.guardarAlbumPorNombreArtista(albumDTO);
        return new ResponseEntity<>(albumGuardado, HttpStatus.CREATED);
    }

    @GetMapping("/todos")
    public ResponseEntity<List<Album>> obtenerAlbumes() {
        List<Album> albumes = albumService.obtenerAlbumes();
        return new ResponseEntity<>(albumes, HttpStatus.OK);
    }

    @GetMapping("/nombre")
    public ResponseEntity<Album> obtenerAlbumPorNombre(@RequestParam("nombre") String nombre) {
        Optional<Album> albumOpt = albumService.obtenerAlbumPorNombre(nombre.trim());
        return new ResponseEntity<>(albumOpt.get(), HttpStatus.OK);
    }
}
