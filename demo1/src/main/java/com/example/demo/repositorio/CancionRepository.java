package com.example.demo.repositorio;

import com.example.demo.modelo.Album;
import com.example.demo.modelo.Artista;
import com.example.demo.modelo.Cancion;
import com.example.demo.modelo.GENERO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CancionRepository extends MongoRepository<Cancion, String> {

    List<Cancion> findByArtista(Artista artista);

    List<Cancion> findByAlbum(Album album);

    List<Cancion> findByGenero(GENERO genero);

    List<Cancion> findByAnio(int anio);

    List<Cancion> findByTituloContainingIgnoreCase(String titulo);
}

