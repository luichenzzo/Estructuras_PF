package com.example.demo.repositorio;

import com.example.demo.modelo.Artista;
import com.example.demo.modelo.GENERO;
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
