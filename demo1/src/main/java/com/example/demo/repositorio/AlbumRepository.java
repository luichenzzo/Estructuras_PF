package com.example.demo.repositorio;

import com.example.demo.modelo.Album;
import com.example.demo.modelo.Artista;
import com.example.demo.modelo.GENERO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumRepository extends MongoRepository<Album, String> {

    List<Album> findByArtista(Artista artista);

    List<Album> findByGenero(GENERO genero);

    List<Album> findByAnio(int anio);

    List<Album> findByTituloContainingIgnoreCase(String titulo);

}

