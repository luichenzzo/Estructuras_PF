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

    @Autowired
    private GrafoSocialService grafoSocialService;

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
        Optional<Usuario> usuarioExistente = usuarioRepository.findByCorreo(correo);
        if (usuarioExistente.isPresent()) {
            throw new RecursoDuplicadoException("Ya existe un usuario con el correo: " + correo);
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setCorreo(correo);
        usuario.setContrasena(contrasena);
        usuario.setListaFavoritos(new ListaEnlazada<>());
        usuario.setListasDeReproduccion(new ListaEnlazada<>());
        usuario.setColaReproduccion(new Cola<>());
        usuario.setSeguidores(new ListaEnlazada<>());
        usuario.setSeguidos(new ListaEnlazada<>());

        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> obtenerUsuarioPorCorreo(String correo) {
        if (correo == null || correo.trim().isEmpty()) {
            throw new DatosInvalidosException("El correo no puede estar vacío");
        }

        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(correo);
        if (usuarioOpt.isEmpty()) {
            throw new UsuarioNoEncontradoException(correo);
        }

        return usuarioOpt;
    }

    // Helper: obtiene Usuario o lanza UsuarioNoEncontradoException / DatosInvalidosException
    private Usuario getUsuarioByCorreo(String correo) {
        if (correo == null || correo.trim().isEmpty()) {
            throw new DatosInvalidosException("El correo de usuario no puede estar vacío");
        }
        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(correo);
        if (usuarioOpt.isEmpty()) {
            throw new UsuarioNoEncontradoException(correo);
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

    public List<Cancion> likearCancion(String correoUsuario, String tituloCancion) {
        Usuario usuario = getUsuarioByCorreo(correoUsuario);
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

    public List<Cancion> dislikearCancion(String correoUsuario, String tituloCancion) {
        Usuario usuario = getUsuarioByCorreo(correoUsuario);
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
    public void seguirUsuario(String correoUsuario, String correoSeguir) {
        Usuario usuario = getUsuarioByCorreo(correoUsuario);
        Usuario usuarioAseguir = getUsuarioByCorreo(correoSeguir);

        // Evitar que un usuario se siga a sí mismo
        if (usuario.getId().equals(usuarioAseguir.getId())) {
            throw new DatosInvalidosException("Un usuario no puede seguirse a sí mismo");
        }

        // Inicializar listas si son null
        if (usuario.getSeguidos() == null) {
            usuario.setSeguidos(new ListaEnlazada<>());
        }
        if (usuarioAseguir.getSeguidores() == null) {
            usuarioAseguir.setSeguidores(new ListaEnlazada<>());
        }

        // Verificar si ya sigue al usuario
        if (usuario.getSeguidos().contiene(usuarioAseguir)) {
            throw new RecursoDuplicadoException("El usuario ya sigue a: " + correoSeguir);
        }

        // Agregar y persistir
        Usuario shallowAseguir = new Usuario(
                usuarioAseguir.getId(),
                usuarioAseguir.getCorreo(),
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
                usuario.getCorreo(),
                null,
                usuario.getNombre(),
                null,
                null,
                null,
                null,
                null
        );

        usuario.getSeguidos().agregar(shallowAseguir);
        usuarioAseguir.getSeguidores().agregar(shallowUsuario);

        // Persist both sides
        usuarioRepository.save(usuario);
        usuarioRepository.save(usuarioAseguir);

        // Actualizar el grafo social: verificar si es conexión bidireccional (amistad)
        if (verificarAmistadBidireccional(usuario.getId(), usuarioAseguir.getId())) {
            grafoSocialService.conectarUsuarios(usuario.getId(), usuarioAseguir.getId());
        }
    }

    // TODO: Reallly have to check this
    public void unseguirUsuario(String correoUsuario, String correoUnseguir) {
        Usuario usuario = getUsuarioByCorreo(correoUsuario);
        Usuario usuarioAunseguir = getUsuarioByCorreo(correoUnseguir);

        // Inicializar listas si son null
        if (usuario.getSeguidos() == null) {
            usuario.setSeguidos(new ListaEnlazada<>());
        }
        if (usuarioAunseguir.getSeguidores() == null) {
            usuarioAunseguir.setSeguidores(new ListaEnlazada<>());
        }

        // Verificar que sí sigue al usuario
        if (!usuario.getSeguidos().contiene(usuarioAunseguir)) {
            throw new com.example.demo.excepciones.RecursoNoEncontradoException("El usuario no sigue a: " + correoUnseguir);
        }

        // Construir referencias "shallow" similares a las usadas al agregar
        Usuario shallowAunseguir = new Usuario(
                usuarioAunseguir.getId(),
                usuarioAunseguir.getCorreo(),
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
                usuario.getCorreo(),
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
            throw new com.example.demo.excepciones.RecursoNoEncontradoException("No se pudo dejar de seguir a: " + correoUnseguir);
        }

        // Persistir cambios en ambas entidades
        usuarioRepository.save(usuario);
        usuarioRepository.save(usuarioAunseguir);

        // Actualizar el grafo social: eliminar la conexión bidireccional
        grafoSocialService.desconectarUsuarios(usuario.getId(), usuarioAunseguir.getId());
    }

    /**
     * Verifica si dos usuarios se siguen mutuamente (amistad bidireccional)
     */
    private boolean verificarAmistadBidireccional(String userId1, String userId2) {
        Optional<Usuario> u1Opt = usuarioRepository.findById(userId1);
        Optional<Usuario> u2Opt = usuarioRepository.findById(userId2);

        if (u1Opt.isEmpty() || u2Opt.isEmpty()) {
            return false;
        }

        Usuario u1 = u1Opt.get();
        Usuario u2 = u2Opt.get();

        // Verificar si u1 sigue a u2 Y u2 sigue a u1
        boolean u1SigueU2 = false;
        boolean u2SigueU1 = false;

        if (u1.getSeguidos() != null) {
            for (Usuario seguido : u1.getSeguidos()) {
                if (seguido.getId().equals(userId2)) {
                    u1SigueU2 = true;
                    break;
                }
            }
        }

        if (u2.getSeguidos() != null) {
            for (Usuario seguido : u2.getSeguidos()) {
                if (seguido.getId().equals(userId1)) {
                    u2SigueU1 = true;
                    break;
                }
            }
        }

        return u1SigueU2 && u2SigueU1;
    }

    public List<Usuario> obtenerSeguidores(String correoUsuario) {
        Usuario usuario = getUsuarioByCorreo(correoUsuario);

        // Inicializar lista si es null
        if (usuario.getSeguidores() == null) {
            usuario.setSeguidores(new ListaEnlazada<>());
        }

        // Convertir la lista enlazada a java.util.List y retornarla
        List<Usuario> seguidores = new ArrayList<>();
        for (Usuario u : usuario.getSeguidores()) {
            seguidores.add(u);
        }

        return seguidores;
    }

    public Optional<Usuario> loginUsuario(String correo, String contrasena) {
        if (correo == null || correo.trim().isEmpty() || contrasena == null || contrasena.trim().isEmpty()) {
            throw new DatosInvalidosException("Correo y contraseña son obligatorios");
        }

        // Usar el método que busca por correo y contraseña directamente
        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreoAndContrasena(correo, contrasena);

        if (usuarioOpt.isEmpty()) {
            throw new UsuarioNoEncontradoException("Correo o contraseña incorrectos para: " + correo);
        }

        return usuarioOpt;
    }

    /**
     * Obtiene un usuario por su ID
     * @param id ID del usuario
     * @return Optional con el usuario si existe
     */
    public Optional<Usuario> obtenerUsuarioPorId(String id) {
        return usuarioRepository.findById(id);
    }
}
