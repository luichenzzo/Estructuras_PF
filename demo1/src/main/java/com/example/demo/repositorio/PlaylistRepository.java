package com.example.demo.repositorio;

import com.example.demo.modelo.Playlist;
import com.example.demo.modelo.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaylistRepository extends MongoRepository<Playlist, String> {

    List<Playlist> findByCreador(Usuario creador);

    List<Playlist> findByNombreContainingIgnoreCase(String nombre);
}

