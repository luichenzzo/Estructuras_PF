package com.example.demo.repository;

import com.example.demo.model.Playlist;
import com.example.demo.model.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaylistRepository extends MongoRepository<Playlist, String> {

    List<Playlist> findByCreador(Usuario creador);

    List<Playlist> findByNombreContainingIgnoreCase(String nombre);
}

