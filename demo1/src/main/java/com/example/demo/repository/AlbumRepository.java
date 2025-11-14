package com.example.demo.repository;

import com.example.demo.model.Album;
import com.example.demo.model.Artista;
import com.example.demo.model.GENERO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumRepository extends MongoRepository<Album, String> {

    List<Album> findByArtista(Artista artista);

    List<Album> findByGenero(GENERO genero);

    List<Album> findByAnio(int anio);

    List<Album> findByTituloContainingIgnoreCase(String titulo);

    java.util.Optional<Album> findByTituloAndArtista(String titulo, Artista artista);

}

