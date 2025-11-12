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

/**
 * Servicio para la gestión de usuarios.
 * Maneja la lógica de negocio relacionada con usuarios, autenticación, favoritos y relaciones sociales.
 */
@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CancionRepository cancionRepository;

    @Autowired
    private GrafoSocialService grafoSocialService;

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * @param nombre     Nombre del usuario
     * @param correo     Correo electrónico único
     * @param contrasena Contraseña del usuario
     * @return Usuario guardado
     * @throws DatosInvalidosException Si algún campo está vacío
     * @throws RecursoDuplicadoException Si ya existe un usuario con ese correo
     */
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

    /**
     * Obtiene un usuario por su correo electrónico.
     *
     * @param correo Correo del usuario
     * @return Optional con el usuario si existe
     * @throws DatosInvalidosException Si el correo está vacío
     * @throws UsuarioNoEncontradoException Si no existe el usuario
     */
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

    /**
     * Obtiene un usuario por su ID.
     *
     * @param id ID del usuario
     * @return Optional con el usuario si existe
     */
    public Optional<Usuario> obtenerUsuarioPorId(String id) {
        return usuarioRepository.findById(id);
    }

    /**
     * Obtiene un usuario por correo o lanza excepción si no existe.
     *
     * @param correo Correo del usuario
     * @return Usuario encontrado
     * @throws DatosInvalidosException Si el correo está vacío
     * @throws UsuarioNoEncontradoException Si no existe el usuario
     */
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

    /**
     * Busca una canción por título (coincidencia exacta o primera coincidencia).
     *
     * @param tituloCancion Título de la canción
     * @return Canción encontrada
     * @throws DatosInvalidosException Si el título está vacío
     * @throws CancionNoEncontradaException Si no existe la canción
     */
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

    /**
     * Agrega una canción a la lista de favoritos del usuario.
     *
     * @param correoUsuario Correo del usuario
     * @param tituloCancion Título de la canción
     * @return Lista actualizada de canciones favoritas
     * @throws RecursoDuplicadoException Si la canción ya está en favoritos
     */
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

    /**
     * Elimina una canción de la lista de favoritos del usuario.
     *
     * @param correoUsuario Correo del usuario
     * @param tituloCancion Título de la canción
     * @return Lista actualizada de canciones favoritas
     * @throws com.example.demo.excepciones.RecursoNoEncontradoException Si la canción no está en favoritos
     */
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
        for (Cancion c : usuario.getListaFavoritos()) {
            if (c.getId().equals(cancionEncontrada.getId())) {
                System.out.println("Cancion encontrada en favoritos: " + c.toString());
                flag = true;
            }
        }
        if (!flag) {
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

    /**
     * Permite que un usuario siga a otro usuario.
     *
     * @param correoUsuario Correo del usuario que quiere seguir
     * @param correoSeguir  Correo del usuario a seguir
     * @throws DatosInvalidosException Si un usuario intenta seguirse a sí mismo
     * @throws RecursoDuplicadoException Si ya sigue al usuario
     */
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
                null,
                usuarioAseguir.getNombre(),
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

    /**
     * Permite que un usuario deje de seguir a otro usuario.
     *
     * @param correoUsuario  Correo del usuario que quiere dejar de seguir
     * @param correoUnseguir Correo del usuario a dejar de seguir
     * @throws com.example.demo.excepciones.RecursoNoEncontradoException Si no sigue al usuario
     */
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
     * Verifica si dos usuarios se siguen mutuamente (amistad bidireccional).
     *
     * @param userId1 ID del primer usuario
     * @param userId2 ID del segundo usuario
     * @return true si ambos se siguen mutuamente, false en caso contrario
     */
    private boolean verificarAmistadBidireccional(String userId1, String userId2) {
        Optional<Usuario> u1Opt = usuarioRepository.findById(userId1);
        Optional<Usuario> u2Opt = usuarioRepository.findById(userId2);

        if (u1Opt.isEmpty() || u2Opt.isEmpty()) {
            return false;
        }

        Usuario u1 = u1Opt.get();
        Usuario u2 = u2Opt.get();

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

    /**
     * Obtiene la lista de seguidores de un usuario.
     *
     * @param correoUsuario Correo del usuario
     * @return Lista de usuarios seguidores
     */
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

    /**
     * Autentica un usuario con su correo y contraseña.
     *
     * @param correo     Correo del usuario
     * @param contrasena Contraseña del usuario
     * @return Optional con el usuario si las credenciales son correctas
     */
    public Optional<Usuario> loginUsuario(String correo, String contrasena) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(correo);

        if (usuarioOpt.isEmpty()) {
            return Optional.empty();
        }

        Usuario usuario = usuarioOpt.get();
        if (usuario.getContrasena().equals(contrasena)) {
            return Optional.of(usuario);
        }

        return Optional.empty();
    }
}
