package com.example.demo.repository;

import com.example.demo.model.Artista;
import com.example.demo.model.GENERO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArtistaRepository extends MongoRepository<Artista, String> {

    Optional<Artista> findByNombre(String nombre);

    List<Artista> findByGeneroPrincipal(GENERO genero);

    List<Artista> findByNacionalidad(String nacionalidad);
}
