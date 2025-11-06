package com.example.demo.controladores;

import com.example.demo.dto.ArtistaRegistroDTO;
import com.example.demo.modelo.Artista;
import com.example.demo.servicios.ArtistaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/artistas")
public class ArtistaController {

    @Autowired
    private ArtistaService artistaService;

    @PostMapping
    public ResponseEntity<Artista> guardarArtista(@RequestBody ArtistaRegistroDTO artistaDTO) {
        try {
            Artista artistaGuardado = artistaService.guardarArtista(artistaDTO);
            return new ResponseEntity<>(artistaGuardado, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping
    public ArrayList<Artista> obtenerArtistas() {
        return artistaService.obtenerArtistas();
    }
}

