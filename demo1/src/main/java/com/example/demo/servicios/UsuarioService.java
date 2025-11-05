package com.example.demo.servicios;

import com.example.demo.modelo.Usuario;
import com.example.demo.repositorio.UsuarioRepository;
import com.example.demo.estructuras.Cola;
import com.example.demo.estructuras.ListaEnlazada;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario guardarUsuario(String nombre, String correo, String contrasena) {
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setUsuario(correo);
        usuario.setContrasena(contrasena);
        usuario.setListaFavoritos(new ListaEnlazada<>());
        usuario.setListasDeReproduccion(new ListaEnlazada<>());
        usuario.setColaReproduccion(new Cola<>());

        return usuarioRepository.save(usuario);
    }
}
