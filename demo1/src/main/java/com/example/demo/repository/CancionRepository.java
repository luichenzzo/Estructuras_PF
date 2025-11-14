package com.example.demo.repository;

import com.example.demo.model.Album;
import com.example.demo.model.Artista;
import com.example.demo.model.Cancion;
import com.example.demo.model.GENERO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CancionRepository extends MongoRepository<Cancion, String> {

    List<Cancion> findByArtista(Artista artista);

    List<Cancion> findByAlbum(Album album);

    List<Cancion> findByGenero(GENERO genero);

    List<Cancion> findByAnio(int anio);

    List<Cancion> findByTituloContainingIgnoreCase(String titulo);
    
    Optional<Cancion> findByTitulo(String titulo);
}

