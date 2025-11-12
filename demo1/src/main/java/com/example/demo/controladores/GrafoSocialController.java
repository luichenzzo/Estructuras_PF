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
 * Controlador REST para el grafo social de usuarios.
 * RF-023: Implementa grafo no dirigido para conexiones entre usuarios.
 * RF-024: Utiliza algoritmo BFS para encontrar amigos de amigos.
 */
@RestController
@RequestMapping("/api/grafo-social")
public class GrafoSocialController {

    @Autowired
    private GrafoSocialService grafoSocialService;

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Conecta dos usuarios en el grafo social (relación de amistad bidireccional).
     *
     * @param usuario1Id ID del primer usuario
     * @param usuario2Id ID del segundo usuario
     * @return ResponseEntity con mensaje de confirmación
     */
    @PostMapping("/conectar")
    public ResponseEntity<String> conectarUsuarios(
            @RequestParam String usuario1Id,
            @RequestParam String usuario2Id) {

        grafoSocialService.conectarUsuarios(usuario1Id, usuario2Id);
        return ResponseEntity.ok("Usuarios conectados exitosamente");
    }

    /**
     * Desconecta dos usuarios en el grafo social.
     *
     * @param usuario1Id ID del primer usuario
     * @param usuario2Id ID del segundo usuario
     * @return ResponseEntity con mensaje de confirmación
     */
    @PostMapping("/desconectar")
    public ResponseEntity<String> desconectarUsuarios(
            @RequestParam String usuario1Id,
            @RequestParam String usuario2Id) {

        grafoSocialService.desconectarUsuarios(usuario1Id, usuario2Id);
        return ResponseEntity.ok("Usuarios desconectados exitosamente");
    }

    /**
     * Verifica si dos usuarios están conectados directamente en el grafo social.
     *
     * @param usuario1Id ID del primer usuario
     * @param usuario2Id ID del segundo usuario
     * @return ResponseEntity con true si están conectados, false en caso contrario
     */
    @GetMapping("/estan-conectados")
    public ResponseEntity<Boolean> estanConectados(
            @RequestParam String usuario1Id,
            @RequestParam String usuario2Id) {

        boolean conectados = grafoSocialService.estanConectados(usuario1Id, usuario2Id);
        return ResponseEntity.ok(conectados);
    }

    /**
     * Obtiene la lista de amigos directos de un usuario.
     *
     * @param usuarioId ID del usuario
     * @return ResponseEntity con lista de usuarios amigos
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
     * Obtiene sugerencias de amigos usando BFS (amigos de amigos).
     * RF-024: Implementa algoritmo BFS para encontrar conexiones indirectas.
     * RF-008: Proporciona sugerencias de usuarios a quienes seguir.
     *
     * @param usuarioId ID del usuario
     * @return ResponseEntity con lista de usuarios sugeridos
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
     * Obtiene amigos a una distancia específica usando BFS.
     *
     * @param usuarioId ID del usuario
     * @param distancia Distancia en el grafo (ej: 2 para amigos de amigos)
     * @return ResponseEntity con lista de usuarios a la distancia especificada
     */
    @GetMapping("/amigos-distancia/{usuarioId}")
    public ResponseEntity<List<Usuario>> obtenerAmigosADistancia(
            @PathVariable String usuarioId,
            @RequestParam int distancia) {

        ListaEnlazada<String> idsAmigos = grafoSocialService.obtenerAmigosADistancia(usuarioId, distancia);

        List<Usuario> amigos = new ArrayList<>();
        for (String id : idsAmigos) {
            Optional<Usuario> usuario = usuarioService.obtenerUsuarioPorId(id);
            usuario.ifPresent(amigos::add);
        }

        return ResponseEntity.ok(amigos);
    }
}
