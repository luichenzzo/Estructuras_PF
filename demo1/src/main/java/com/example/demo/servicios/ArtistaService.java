package com.example.demo.servicios;

import com.example.demo.dto.ArtistaRegistroDTO;
import com.example.demo.modelo.Artista;
import com.example.demo.repositorio.ArtistaRepository;
import com.example.demo.estructuras.ListaDoblementeEnlazada;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArtistaService {

    @Autowired
    private ArtistaRepository artistaRepository;

    public Artista guardarArtista(ArtistaRegistroDTO artistaDTO) {
        Artista artista = new Artista();
        artista.setNombre(artistaDTO.getNombre());
        artista.setNacionalidad(artistaDTO.getNacionalidad());
        artista.setGeneroPrincipal(artistaDTO.getGeneroPrincipal());
        artista.setGeneroSecundario(artistaDTO.getGeneroSecundario());
        artista.setURLFotoArtista(artistaDTO.getURLFotoArtista());
        artista.setAlbumes(new ListaDoblementeEnlazada<>());

        return artistaRepository.save(artista);
    }
}

