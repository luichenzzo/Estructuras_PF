package com.example.demo.repository;

import com.example.demo.model.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends MongoRepository<Usuario, String> {

    Optional<Usuario> findByCorreo(String correo);

    Optional<Usuario> findByCorreoAndContrasena(String correo, String contrasena);

    boolean existsByCorreo(String correo);

}
