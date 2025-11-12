package com.example.demo.controladores;

import com.example.demo.modelo.Cancion;
import com.example.demo.servicios.UsuarioService;
import com.example.demo.modelo.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para la gestión de usuarios.
 * Maneja operaciones relacionadas con usuarios, autenticación, favoritos y relaciones sociales.
 */
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * @param nombre     Nombre del usuario
     * @param correo     Correo electrónico (único)
     * @param contrasena Contraseña del usuario
     * @return ResponseEntity con el usuario creado
     */
    @PostMapping
    public ResponseEntity<Usuario> guardarUsuario(
            @RequestParam String nombre,
            @RequestParam String correo,
            @RequestParam String contrasena) {
        Usuario usuarioGuardado = usuarioService.guardarUsuario(nombre, correo, contrasena);
        return new ResponseEntity<>(usuarioGuardado, HttpStatus.CREATED);
    }

    /**
     * Obtiene la información de un usuario por su correo electrónico.
     *
     * @param correo Correo electrónico del usuario
     * @return ResponseEntity con el usuario encontrado o NOT_FOUND si no existe
     */
    @GetMapping
    public ResponseEntity<Usuario> obtenerUsuarioPorCorreo(@RequestParam String correo) {
        Optional<Usuario> usuarioOpt = usuarioService.obtenerUsuarioPorCorreo(correo);
        if (usuarioOpt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(usuarioOpt.get(), HttpStatus.OK);
    }

    /**
     * Agrega una canción a la lista de favoritos del usuario.
     *
     * @param correoUsuario  Correo del usuario
     * @param tituloCancion  Título de la canción a marcar como favorita
     * @return ResponseEntity con la lista actualizada de canciones favoritas
     */
    @GetMapping("/like")
    public ResponseEntity<List<Cancion>> likearCancion(@RequestParam String correoUsuario, @RequestParam String tituloCancion) {
        List<Cancion> canciones = usuarioService.likearCancion(correoUsuario, tituloCancion);
        return new ResponseEntity<>(canciones, HttpStatus.OK);
    }

    /**
     * Elimina una canción de la lista de favoritos del usuario.
     *
     * @param correoUsuario  Correo del usuario
     * @param tituloCancion  Título de la canción a eliminar de favoritos
     * @return ResponseEntity con la lista actualizada de canciones favoritas
     */
    @GetMapping("/dislike")
    public ResponseEntity<List<Cancion>> dislikearCancion(@RequestParam String correoUsuario, @RequestParam String tituloCancion) {
        List<Cancion> canciones = usuarioService.dislikearCancion(correoUsuario, tituloCancion);
        return new ResponseEntity<>(canciones, HttpStatus.OK);
    }

    /**
     * Permite que un usuario siga a otro usuario.
     *
     * @param correoUsuario Correo del usuario que quiere seguir
     * @param correoSeguir  Correo del usuario a seguir
     * @return ResponseEntity con estado OK si fue exitoso, UNAUTHORIZED si hubo error
     */
    @GetMapping("/seguir")
    public ResponseEntity<Void> seguirUsuario(@RequestParam String correoUsuario, @RequestParam String correoSeguir) {
        try {
            usuarioService.seguirUsuario(correoUsuario, correoSeguir);
            return ResponseEntity.ok().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Permite que un usuario deje de seguir a otro usuario.
     *
     * @param correoUsuario   Correo del usuario que quiere dejar de seguir
     * @param correoUnseguir  Correo del usuario a dejar de seguir
     * @return ResponseEntity con estado OK si fue exitoso, UNAUTHORIZED si hubo error
     */
    @GetMapping("/unseguir")
    public ResponseEntity<Void> unseguirUsuario(@RequestParam String correoUsuario, @RequestParam String correoUnseguir) {
        try {
            usuarioService.unseguirUsuario(correoUsuario, correoUnseguir);
            return ResponseEntity.ok().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Obtiene la lista de seguidores de un usuario.
     *
     * @param correoUsuario Correo del usuario
     * @return ResponseEntity con la lista de usuarios seguidores
     */
    @GetMapping("/seguidores")
    public ResponseEntity<List<Usuario>> obtenerSeguidores(@RequestParam String correoUsuario) {
        List<Usuario> seguidores = usuarioService.obtenerSeguidores(correoUsuario);
        return new ResponseEntity<>(seguidores, HttpStatus.OK);
    }

    /**
     * Autentica un usuario con su correo y contraseña.
     *
     * @param correo     Correo electrónico del usuario
     * @param contrasena Contraseña del usuario
     * @return ResponseEntity con el usuario si las credenciales son correctas, UNAUTHORIZED si no
     */
    @GetMapping("/login")
    public ResponseEntity<Usuario> loginUsuario(@RequestParam String correo, @RequestParam String contrasena) {
        Optional<Usuario> usuarioOpt = usuarioService.loginUsuario(correo, contrasena);
        if (usuarioOpt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(usuarioOpt.get(), HttpStatus.OK);
    }
}
