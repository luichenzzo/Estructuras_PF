package com.example.demo.modelo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Entidad que representa un administrador del sistema.
 * Extiende de Usuario con privilegios administrativos adicionales.
 */
@Document(collection = "administradores")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Administrador extends Usuario {

}
