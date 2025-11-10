package com.example.demo.servicios;

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
        usuario.setSeguidores(new ListaEnlazada<>());
        usuario.setSeguidos(new ListaEnlazada<>());

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

    // Helper: obtiene Usuario o lanza UsuarioNoEncontradoException / DatosInvalidosException
    private Usuario getUsuarioByNombre(String nombreUsuario) {
        if (nombreUsuario == null || nombreUsuario.trim().isEmpty()) {
            throw new DatosInvalidosException("El nombre de usuario no puede estar vacío");
        }
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsuario(nombreUsuario);
        if (usuarioOpt.isEmpty()) {
            throw new UsuarioNoEncontradoException(nombreUsuario);
        }
        return usuarioOpt.get();
    }

    // Helper: busca una canción por título (coincidencia exacta ignorando mayúsculas o primera coincidencia)
    private Cancion encontrarCancionPorTitulo(String tituloCancion) {
        if (tituloCancion == null || tituloCancion.trim().isEmpty()) {
            throw new DatosInvalidosException("El título de la canción no puede estar vacío");
        }

        List<Cancion> resultados = cancionRepository.findByTituloContainingIgnoreCase(tituloCancion.trim());
        Cancion cancionEncontrada = null;
        if (resultados != null && !resultados.isEmpty()) {
            for (Cancion c : resultados) {
                if (c.getTitulo() != null && c.getTitulo().equalsIgnoreCase(tituloCancion.trim())) {
                    cancionEncontrada = c;
                    break;
                }
            }
            if (cancionEncontrada == null) {
                cancionEncontrada = resultados.get(0);
            }
        }

        if (cancionEncontrada == null) {
            throw new CancionNoEncontradaException(tituloCancion);
        }

        return cancionEncontrada;
    }

    public List<Cancion> likearCancion(String nombreUsuario, String tituloCancion) {
        Usuario usuario = getUsuarioByNombre(nombreUsuario);
        Cancion cancionEncontrada = encontrarCancionPorTitulo(tituloCancion);

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
        Usuario usuario = getUsuarioByNombre(nombreUsuario);
        Cancion cancionEncontrada = encontrarCancionPorTitulo(tituloCancion);

        // Inicializar lista de favoritos si es null
        if (usuario.getListaFavoritos() == null) {
            usuario.setListaFavoritos(new ListaEnlazada<>());
        }
        System.out.println("Lista: ");
        System.out.println(usuario.getListaFavoritos().toString());
        System.out.println(cancionEncontrada.toString());
        // Verificar que la canción exista en favoritos
        boolean flag = false;
        for( Cancion c : usuario.getListaFavoritos()){
            if (c.getId().equals(cancionEncontrada.getId())) {
                System.out.println("Cancion encontrada en favoritos: " + c.toString());
                flag = true;
            }
        }
        if (!flag) {
            // Si la canción no está en favoritos, lanzar excepción de recurso no encontrado para indicar que no puede quitarse
            throw new com.example.demo.excepciones.RecursoNoEncontradoException("La canción no está en la lista de favoritos del usuario");
        }

        // Remover la canción y persistir
        usuario.getListaFavoritos().eliminar(cancionEncontrada);
        usuarioRepository.save(usuario);

        // Convertir la lista enlazada a java.util.List y retornarla
        List<Cancion> favoritos = new ArrayList<>();
        for (Cancion c : usuario.getListaFavoritos()) {
            favoritos.add(c);
        }

        return favoritos;
    }


    //TODO: Metodo permite seguirse a uno mismo, además no verifica correctamente si ya sigue al usuario.
    public void seguirUsuario(String nombreUsuario, String usuarioSeguir) {
        Usuario usuario = getUsuarioByNombre(nombreUsuario);
        Usuario usuarioAseguir = getUsuarioByNombre(usuarioSeguir);

        // Inicializar listas si son null
        if (usuario.getSeguidos() == null) {
            usuario.setSeguidos(new ListaEnlazada<>());
        }
        if (usuarioAseguir.getSeguidores() == null) {
            usuarioAseguir.setSeguidores(new ListaEnlazada<>());
        }

        // Verificar si ya sigue al usuario
        if (usuario.getSeguidos().contiene(usuarioAseguir)) {
            throw new RecursoDuplicadoException("El usuario ya sigue a: " + usuarioSeguir);
        }

        // Agregar y persistir
        // To avoid circular references (usuario <-> usuarioAseguir) that cause StackOverflow when the
        // MongoDB mapper or Lombok-generated toString/equals traverse the graph, we add "shallow"
        // Usuario instances (only id, usuario, nombre) into the lists instead of the full objects.
        Usuario shallowAseguir = new Usuario(
                usuarioAseguir.getId(),
                usuarioAseguir.getUsuario(),
                null, // contrasena
                usuarioAseguir.getNombre(),
                null, // listaFavoritos
                null, // listasDeReproduccion
                null, // colaReproduccion
                null, // seguidores
                null  // seguidos
        );

        Usuario shallowUsuario = new Usuario(
                usuario.getId(),
                usuario.getUsuario(),
                null,
                usuario.getNombre(),
                null,
                null,
                null,
                null,
                null
        );

        usuario.getSeguidos().agregar(shallowAseguir);
        System.out.println(usuario.getSeguidos().toString());
        usuarioAseguir.getSeguidores().agregar(shallowUsuario);

        // Persist both sides (they now contain only shallow references to each other)
        usuarioRepository.save(usuario);
        usuarioRepository.save(usuarioAseguir);

    }

    // TODO: Reallly have to check this
    public void unseguirUsuario(String nombreUsuario, String usuarioUnseguir) {
        Usuario usuario = getUsuarioByNombre(nombreUsuario);
        Usuario usuarioAunseguir = getUsuarioByNombre(usuarioUnseguir);

        // Inicializar listas si son null
        if (usuario.getSeguidos() == null) {
            usuario.setSeguidos(new ListaEnlazada<>());
        }
        if (usuarioAunseguir.getSeguidores() == null) {
            usuarioAunseguir.setSeguidores(new ListaEnlazada<>());
        }

        // Verificar que sí sigue al usuario
        if (!usuario.getSeguidos().contiene(usuarioAunseguir)) {
            throw new com.example.demo.excepciones.RecursoNoEncontradoException("El usuario no sigue a: " + usuarioUnseguir);
        }

        // Construir referencias "shallow" similares a las usadas al agregar
        Usuario shallowAunseguir = new Usuario(
                usuarioAunseguir.getId(),
                usuarioAunseguir.getUsuario(),
                null,
                usuarioAunseguir.getNombre(),
                null,
                null,
                null,
                null,
                null
        );

        Usuario shallowUsuario = new Usuario(
                usuario.getId(),
                usuario.getUsuario(),
                null,
                usuario.getNombre(),
                null,
                null,
                null,
                null,
                null
        );

        // Intentar eliminar de ambas listas
        boolean removedFromSeguidos = usuario.getSeguidos().eliminar(shallowAunseguir);
        boolean removedFromSeguidores = usuarioAunseguir.getSeguidores().eliminar(shallowUsuario);

        if (!removedFromSeguidos || !removedFromSeguidores) {
            // Si por alguna razón no se eliminaron correctamente, lanzar excepción
            throw new com.example.demo.excepciones.RecursoNoEncontradoException("No se pudo dejar de seguir a: " + usuarioUnseguir);
        }

        // Persistir cambios en ambas entidades
        usuarioRepository.save(usuario);
        usuarioRepository.save(usuarioAunseguir);
    }
}
