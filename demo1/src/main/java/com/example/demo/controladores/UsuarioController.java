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

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<Usuario> guardarUsuario(
            @RequestParam String nombre,
            @RequestParam String correo,
            @RequestParam String contrasena) {
        Usuario usuarioGuardado = usuarioService.guardarUsuario(nombre, correo, contrasena);
        return new ResponseEntity<>(usuarioGuardado, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Usuario> obtenerUsuarioPorCorreo(@RequestParam String correo) {
        Optional<Usuario> usuarioOpt = usuarioService.obtenerUsuarioPorCorreo(correo);
        if (usuarioOpt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(usuarioOpt.get(), HttpStatus.OK);
    }


    //Este metodo espera el correo, no el nombre de usuario
    //TODO: Maybe tendría mas sentido si recibe el nombre o el ID
    @GetMapping("/like")
    public ResponseEntity<List<Cancion>> likearCancion (@RequestParam String correoUsuario, @RequestParam String tituloCancion) {
        List<Cancion> canciones = usuarioService.likearCancion(correoUsuario, tituloCancion);
        return new ResponseEntity<>(canciones, HttpStatus.OK);

    }

    // Endpoint para quitar like (dislike) reutilizando la lógica del servicio
    @GetMapping("/dislike")
    public ResponseEntity<List<Cancion>> dislikearCancion (@RequestParam String correoUsuario, @RequestParam String tituloCancion) {
        List<Cancion> canciones = usuarioService.dislikearCancion(correoUsuario, tituloCancion);
        return new ResponseEntity<>(canciones, HttpStatus.OK);
    }

    @GetMapping("/seguir")
    public ResponseEntity<Void> seguirUsuario (@RequestParam String correoUsuario, @RequestParam String correoSeguir){
        try {
            // Call the service which performs the follow operation. We ignore the returned list
            // and respond with a simple OK on success.
            usuarioService.seguirUsuario(correoUsuario, correoSeguir);
            return ResponseEntity.ok().build();
        } catch (RuntimeException ex) {
            // Map service/runtime errors to 401 UNAUTHORIZED as requested.
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

    }


    @GetMapping("/unseguir")
    public ResponseEntity<Void> unseguirUsuario (@RequestParam String correoUsuario, @RequestParam String correoUnseguir){
        try {
            usuarioService.unseguirUsuario(correoUsuario, correoUnseguir);
            return ResponseEntity.ok().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/seguidores")
    public ResponseEntity<List<Usuario>> obtenerSeguidores (@RequestParam String correoUsuario){
        List<Usuario> seguidores = usuarioService.obtenerSeguidores(correoUsuario);
        return new ResponseEntity<>(seguidores, HttpStatus.OK);
    }


    @GetMapping("/login")
    public ResponseEntity<Usuario> loginUsuario (@RequestParam String correo, @RequestParam String contrasena){
        Optional<Usuario> usuarioOpt = usuarioService.loginUsuario(correo, contrasena);
        if (usuarioOpt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(usuarioOpt.get(), HttpStatus.OK);
    }

}
