package com.example.demo.repositorio;

import com.example.demo.modelo.Administrador;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdministradorRepository extends MongoRepository<Administrador, String> {
}

