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
        return new ResponseEntity<>(usuarioOpt.get(), HttpStatus.OK);
    }


    //Este metodo espera el correo, no el nombre de usuario
    //TODO: Maybe tendría mas sentido si recibe el nombre o el ID
    @GetMapping("/like")
    public ResponseEntity<List<Cancion>> likearCancion (@RequestParam String nombreUsuario, @RequestParam String tituloCancion) {
        List<Cancion> canciones = usuarioService.likearCancion(nombreUsuario, tituloCancion);
        return new ResponseEntity<>(canciones, HttpStatus.OK);

    }


}
