package com.example.demo.servicios;

import com.example.demo.estructuras.ListaRedonda;
import com.example.demo.modelo.Cancion;
import com.example.demo.modelo.Usuario;
import com.example.demo.repositorio.UsuarioRepository;
import com.example.demo.estructuras.Cola;
import com.example.demo.estructuras.ListaEnlazada;
import com.example.demo.excepciones.DatosInvalidosException;
import com.example.demo.excepciones.RecursoDuplicadoException;
import com.example.demo.excepciones.UsuarioNoEncontradoException;
import com.example.demo.excepciones.CancionNoEncontradaException;
import com.example.demo.repositorio.CancionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CancionRepository cancionRepository;

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
        usuario.setSeguidores(new ListaRedonda<>());
        usuario.setSeguidos(new ListaRedonda<>());

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

    public List<Cancion> likearCancion(String nombreUsuario, String tituloCancion) {

        // Validaciones básicas
        if (nombreUsuario == null || nombreUsuario.trim().isEmpty()) {
            throw new DatosInvalidosException("El nombre de usuario no puede estar vacío");
        }
        if (tituloCancion == null || tituloCancion.trim().isEmpty()) {
            throw new DatosInvalidosException("El título de la canción no puede estar vacío");
        }

        // Buscar usuario
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsuario(nombreUsuario);
        if (usuarioOpt.isEmpty()) {
            throw new UsuarioNoEncontradoException(nombreUsuario);
        }
        Usuario usuario = usuarioOpt.get();

        // Buscar canción por título (buscar coincidencias y luego intentar un match exacto ignorando mayúsculas)
        List<Cancion> resultados = cancionRepository.findByTituloContainingIgnoreCase(tituloCancion.trim());
        Cancion cancionEncontrada = null;
        if (resultados != null && !resultados.isEmpty()) {
            for (Cancion c : resultados) {
                if (c.getTitulo() != null && c.getTitulo().equalsIgnoreCase(tituloCancion.trim())) {
                    cancionEncontrada = c;
                    break;
                }
            }
            // si no hubo igualdad exacta, tomar la primera coincidencia
            if (cancionEncontrada == null) {
                cancionEncontrada = resultados.get(0);
            }
        }

        if (cancionEncontrada == null) {
            throw new CancionNoEncontradaException(tituloCancion);
        }

        // Inicializar lista de favoritos si es null
        if (usuario.getListaFavoritos() == null) {
            usuario.setListaFavoritos(new ListaEnlazada<>());
        }

        // Verificar duplicado
        if (usuario.getListaFavoritos().contiene(cancionEncontrada)) {
            throw new RecursoDuplicadoException("La canción ya está en la lista de favoritos del usuario");
        }

        // Agregar y persistir
        usuario.getListaFavoritos().agregar(cancionEncontrada);
        usuarioRepository.save(usuario);

        // Convertir la lista enlazada a java.util.List y retornarla
        List<Cancion> favoritos = new ArrayList<>();
        for (Cancion c : usuario.getListaFavoritos()) {
            favoritos.add(c);
        }

        return favoritos;
    }

    public List<Cancion> dislikearCancion(String nombreUsuario, String tituloCancion) {
        System.out.println("Dislike request received for user: " + nombreUsuario + " and song: " + tituloCancion);
        // Validaciones básicas
        if (nombreUsuario == null || nombreUsuario.trim().isEmpty()) {
            throw new DatosInvalidosException("El nombre de usuario no puede estar vacío");
        }
        if (tituloCancion == null || tituloCancion.trim().isEmpty()) {
            throw new DatosInvalidosException("El título de la canción no puede estar vacío");
        }

        // Buscar usuario
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsuario(nombreUsuario);
        if (usuarioOpt.isEmpty()) {
            throw new UsuarioNoEncontradoException(nombreUsuario);
        }
        Usuario usuario = usuarioOpt.get();
        System.out.println("paso 1");

        // Buscar canción por título (buscar coincidencias y luego intentar un match exacto ignorando mayúsculas)
        List<Cancion> resultados = cancionRepository.findByTituloContainingIgnoreCase(tituloCancion.trim());
        Cancion cancionEncontrada = null;
        if (resultados != null && !resultados.isEmpty()) {
            for (Cancion c : resultados) {
                System.out.println(c.getTitulo());
                if (c.getTitulo() != null && c.getTitulo().equalsIgnoreCase(tituloCancion.trim())) {
                    cancionEncontrada = c;
                    System.out.println("Match!");
                    break;
                }
            }
            // si no hubo igualdad exacta, tomar la primera coincidencia
            if (cancionEncontrada == null) {
                cancionEncontrada = resultados.get(0);
            }
        }

        if (cancionEncontrada == null) {
            throw new CancionNoEncontradaException(tituloCancion);
        }

        // Inicializar lista de favoritos si es null
        if (usuario.getListaFavoritos() == null) {
            usuario.setListaFavoritos(new ListaEnlazada<>());
        }
        // Verificar que exista en favoritos (si no está, lanzar excepción)
        if (!usuario.getListaFavoritos().contiene(cancionEncontrada)) {
            throw new CancionNoEncontradaException("La canción no está en la lista de favoritos del usuario");
        }

        // Remover y persistir
        usuario.getListaFavoritos().eliminar(cancionEncontrada);
        usuarioRepository.save(usuario);
        System.out.println("paso 2");
        // Convertir la lista enlazada a java.util.List y retornarla
        List<Cancion> favoritos = new ArrayList<>();
        for (Cancion c : usuario.getListaFavoritos()) {
            favoritos.add(c);
        }

        return favoritos;
    }

}
