package com.example.demo.servicios;

import com.example.demo.modelo.Usuario;
import com.example.demo.repositorio.UsuarioRepository;
import com.example.demo.estructuras.Cola;
import com.example.demo.estructuras.ListaEnlazada;
import com.example.demo.excepciones.DatosInvalidosException;
import com.example.demo.excepciones.RecursoDuplicadoException;
import com.example.demo.excepciones.UsuarioNoEncontradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario guardarUsuario(String nombre, String correo, String contrasena) {
        // Validar datos
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new DatosInvalidosException("El nombre es obligatorio");
        }
        if (correo == null || correo.trim().isEmpty()) {
            throw new DatosInvalidosException("El correo es obligatorio");
        }
        if (contrasena == null || contrasena.trim().isEmpty()) {
            throw new DatosInvalidosException("La contraseña es obligatoria");
        }

        // Verificar si el usuario ya existe
        Optional<Usuario> usuarioExistente = usuarioRepository.findByUsuario(correo);
        if (usuarioExistente.isPresent()) {
            throw new RecursoDuplicadoException("Ya existe un usuario con el correo: " + correo);
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setUsuario(correo);
        usuario.setContrasena(contrasena);
        usuario.setListaFavoritos(new ListaEnlazada<>());
        usuario.setListasDeReproduccion(new ListaEnlazada<>());
        usuario.setColaReproduccion(new Cola<>());

        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> obtenerUsuarioPorCorreo(String usuario) {
        if (usuario == null || usuario.trim().isEmpty()) {
            throw new DatosInvalidosException("El correo no puede estar vacío");
        }

        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsuario(usuario);
        if (usuarioOpt.isEmpty()) {
            throw new UsuarioNoEncontradoException(usuario);
        }

        return usuarioOpt;
    }
}
