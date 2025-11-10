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

@RestController
@RequestMapping("/artistas")
public class ArtistaController {

    @Autowired
    private ArtistaService artistaService;

    @PostMapping
    public ResponseEntity<Artista> guardarArtista(@RequestBody ArtistaRegistroDTO artistaDTO) {
        System.out.println("Entrada" + artistaDTO.getURLFotoArtista());
        Artista artistaGuardado = artistaService.guardarArtista(artistaDTO);
        return new ResponseEntity<>(artistaGuardado, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ArrayList<Artista>> obtenerArtistas() {
        ArrayList<Artista> artistas = artistaService.obtenerArtistas();
        return new ResponseEntity<>(artistas, HttpStatus.OK);
    }

    @GetMapping("/nombres")
    public ResponseEntity<List<String>> obtenerNombresArtistas() {
        return artistaService.obtenerNombresArtistas();
    }
}
