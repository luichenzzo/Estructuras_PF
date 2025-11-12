package com.example.demo.controladores;

import com.example.demo.dto.ArtistaRegistroDTO;
import com.example.demo.modelo.Artista;
import com.example.demo.servicios.ArtistaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Controlador REST para la gestión de artistas.
 * Proporciona endpoints para crear y consultar información de artistas musicales.
 */
@RestController
@RequestMapping("/artistas")
public class ArtistaController {

    @Autowired
    private ArtistaService artistaService;

    /**
     * Registra un nuevo artista en el sistema.
     *
     * @param artistaDTO Datos del artista a registrar
     * @return ResponseEntity con el artista creado
     */
    @PostMapping
    public ResponseEntity<Artista> guardarArtista(@RequestBody ArtistaRegistroDTO artistaDTO) {
        System.out.println("Entrada" + artistaDTO.getURLFotoArtista());
        Artista artistaGuardado = artistaService.guardarArtista(artistaDTO);
        return new ResponseEntity<>(artistaGuardado, HttpStatus.CREATED);
    }

    /**
     * Obtiene la lista completa de todos los artistas registrados.
     *
     * @return ResponseEntity con la lista de artistas
     */
    @GetMapping
    public ResponseEntity<ArrayList<Artista>> obtenerArtistas() {
        ArrayList<Artista> artistas = artistaService.obtenerArtistas();
        return new ResponseEntity<>(artistas, HttpStatus.OK);
    }

    /**
     * Obtiene únicamente los nombres de todos los artistas registrados.
     * Útil para poblar listas desplegables en el frontend.
     *
     * @return ResponseEntity con la lista de nombres de artistas
     */
    @GetMapping("/nombres")
    public ResponseEntity<List<String>> obtenerNombresArtistas() {
        return artistaService.obtenerNombresArtistas();
    }
}
