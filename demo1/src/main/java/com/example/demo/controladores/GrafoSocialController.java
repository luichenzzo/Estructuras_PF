package com.example.demo.controladores;

import com.example.demo.estructuras.ListaEnlazada;
import com.example.demo.servicios.GrafoSocialService;
import com.example.demo.servicios.UsuarioService;
import com.example.demo.modelo.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Controlador para el Grafo Social de Usuarios
 * RF-023: Grafo No Dirigido
 * RF-024: Algoritmo BFS para amigos de amigos
 */
@RestController
@RequestMapping("/api/grafo-social")
    public class GrafoSocialController {

    @Autowired
    private GrafoSocialService grafoSocialService;

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Conecta dos usuarios (siguen mutuamente)
     */
    @PostMapping("/conectar")
    public ResponseEntity<String> conectarUsuarios(
            @RequestParam String usuario1Id,
            @RequestParam String usuario2Id) {

        grafoSocialService.conectarUsuarios(usuario1Id, usuario2Id);
        return ResponseEntity.ok("Usuarios conectados exitosamente");
    }

    /**
     * Desconecta dos usuarios
     */
    @PostMapping("/desconectar")
    public ResponseEntity<String> desconectarUsuarios(
            @RequestParam String usuario1Id,
            @RequestParam String usuario2Id) {

        grafoSocialService.desconectarUsuarios(usuario1Id, usuario2Id);
        return ResponseEntity.ok("Usuarios desconectados exitosamente");
    }

    /**
     * Verifica si dos usuarios están conectados
     */
    @GetMapping("/estan-conectados")
    public ResponseEntity<Boolean> estanConectados(
            @RequestParam String usuario1Id,
            @RequestParam String usuario2Id) {

        boolean conectados = grafoSocialService.estanConectados(usuario1Id, usuario2Id);
        return ResponseEntity.ok(conectados);
    }

    /**
     * Obtiene los amigos directos de un usuario
     */
    @GetMapping("/amigos/{usuarioId}")
    public ResponseEntity<List<Usuario>> obtenerAmigos(@PathVariable String usuarioId) {
        ListaEnlazada<String> idsAmigos = grafoSocialService.obtenerAmigos(usuarioId);

        List<Usuario> amigos = new ArrayList<>();
        for (String id : idsAmigos) {
            Optional<Usuario> usuario = usuarioService.obtenerUsuarioPorId(id);
            usuario.ifPresent(amigos::add);
        }

        return ResponseEntity.ok(amigos);
    }

    /**
     * RF-024: Obtiene sugerencias de amigos usando BFS (amigos de amigos)
     * RF-008: Recibir sugerencias de usuarios a quienes seguir
     */
    @GetMapping("/sugerencias/{usuarioId}")
    public ResponseEntity<List<Usuario>> obtenerSugerencias(@PathVariable String usuarioId) {
        ListaEnlazada<String> idsSugerencias = grafoSocialService.obtenerSugerenciasAmigos(usuarioId);

        List<Usuario> sugerencias = new ArrayList<>();
        for (String id : idsSugerencias) {
            Optional<Usuario> usuario = usuarioService.obtenerUsuarioPorId(id);
            usuario.ifPresent(sugerencias::add);
        }

        return ResponseEntity.ok(sugerencias);
    }

    /**
     * Obtiene amigos a una distancia específica
     */
    @GetMapping("/amigos-distancia/{usuarioId}/{distancia}")
    public ResponseEntity<List<Usuario>> obtenerAmigosADistancia(
            @PathVariable String usuarioId,
            @PathVariable int distancia) {

        ListaEnlazada<String> idsAmigos = grafoSocialService.obtenerAmigosADistancia(usuarioId, distancia);

        List<Usuario> usuarios = new ArrayList<>();
        for (String id : idsAmigos) {
            Optional<Usuario> usuario = usuarioService.obtenerUsuarioPorId(id);
            usuario.ifPresent(usuarios::add);
        }

        return ResponseEntity.ok(usuarios);
    }

    /**
     * Obtiene toda la red de un usuario
     */
    @GetMapping("/red-completa/{usuarioId}")
    public ResponseEntity<List<Usuario>> obtenerRedCompleta(@PathVariable String usuarioId) {
        ListaEnlazada<String> idsRed = grafoSocialService.obtenerRedCompleta(usuarioId);

        List<Usuario> red = new ArrayList<>();
        for (String id : idsRed) {
            Optional<Usuario> usuario = usuarioService.obtenerUsuarioPorId(id);
            usuario.ifPresent(red::add);
        }

        return ResponseEntity.ok(red);
    }

    /**
     * Reconstruye el grafo social desde la base de datos
     */
    @PostMapping("/reconstruir")
    public ResponseEntity<String> reconstruirGrafo() {
        grafoSocialService.reconstruirGrafoSocial();
        return ResponseEntity.ok("Grafo social reconstruido exitosamente");
    }
}

