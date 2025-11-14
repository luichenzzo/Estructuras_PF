package com.example.demo.repository;

import com.example.demo.model.Administrador;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdministradorRepository extends MongoRepository<Administrador, String> {
}

