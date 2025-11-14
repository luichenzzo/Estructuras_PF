package com.example.demo.controllers;

import com.example.demo.model.Cancion;
import com.example.demo.services.UsuarioService;
import com.example.demo.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
     * @return ResponseEntity con el usuario encontrado
     */
    @GetMapping
    public ResponseEntity<Usuario> obtenerUsuarioPorCorreo(@RequestParam String correo) {
        Usuario usuario = usuarioService.obtenerUsuarioPorCorreo(correo).get();
        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }

    /**
     * Obtiene todos los usuarios registrados en el sistema.
     *
     * @return ResponseEntity con la lista de todos los usuarios
     */
    @GetMapping("/todos")
    public ResponseEntity<List<Usuario>> obtenerTodosLosUsuarios() {
        List<Usuario> usuarios = usuarioService.obtenerTodosLosUsuarios();
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
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
     * @return ResponseEntity con estado OK si fue exitoso
     */
    @GetMapping("/seguir")
    public ResponseEntity<Void> seguirUsuario(@RequestParam String correoUsuario, @RequestParam String correoSeguir) {
        usuarioService.seguirUsuario(correoUsuario, correoSeguir);
        return ResponseEntity.ok().build();
    }

    /**
     * Permite que un usuario deje de seguir a otro usuario.
     *
     * @param correoUsuario   Correo del usuario que quiere dejar de seguir
     * @param correoUnseguir  Correo del usuario a dejar de seguir
     * @return ResponseEntity con estado OK si fue exitoso
     */
    @GetMapping("/unseguir")
    public ResponseEntity<Void> unseguirUsuario(@RequestParam String correoUsuario, @RequestParam String correoUnseguir) {
        usuarioService.unseguirUsuario(correoUsuario, correoUnseguir);
        return ResponseEntity.ok().build();
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
     * @return ResponseEntity con el usuario si las credenciales son correctas
     */
    @GetMapping("/login")
    public ResponseEntity<Usuario> loginUsuario(@RequestParam String correo, @RequestParam String contrasena) {
        Usuario usuario = usuarioService.loginUsuario(correo, contrasena);
        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }

    /**
     * Actualiza los datos de un usuario existente.
     *
     * @param correo Correo actual del usuario
     * @param nuevoCorreo Nuevo correo (opcional)
     * @param nombre Nuevo nombre (opcional)
     * @param nuevaContrasena Nueva contraseña (opcional)
     * @return ResponseEntity con el usuario actualizado
     */
    @PutMapping
    public ResponseEntity<Usuario> actualizarUsuario(
            @RequestParam String correo,
            @RequestParam(required = false) String nuevoCorreo,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String nuevaContrasena) {
        Usuario usuarioActualizado = usuarioService.actualizarUsuario(correo, nuevoCorreo, nombre, nuevaContrasena);
        return new ResponseEntity<>(usuarioActualizado, HttpStatus.OK);
    }

    /**
     * Elimina un usuario del sistema.
     *
     * @param correo Correo del usuario a eliminar
     * @return ResponseEntity con estado NO_CONTENT si fue exitoso
     */
    @DeleteMapping
    public ResponseEntity<Void> eliminarUsuario(@RequestParam String correo) {
        usuarioService.eliminarUsuario(correo);
        return ResponseEntity.noContent().build();
    }
}
