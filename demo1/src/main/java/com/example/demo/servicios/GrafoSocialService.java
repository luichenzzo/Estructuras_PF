package com.example.demo.servicios;

import com.example.demo.estructuras.GrafoNoDirigido;
import com.example.demo.estructuras.ListaEnlazada;
import com.example.demo.modelo.Usuario;
import com.example.demo.repositorio.UsuarioRepository;
import com.example.demo.excepciones.UsuarioNoEncontradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Servicio para el Grafo Social de Usuarios
 * RF-023: Implementar Grafo No Dirigido para conexiones entre usuarios
 * RF-024: Algoritmo BFS para encontrar "amigos de amigos"
 */
@Service
public class GrafoSocialService {

    private GrafoNoDirigido<String> grafoSocial;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public GrafoSocialService() {
        this.grafoSocial = new GrafoNoDirigido<>();
    }

    /**
     * Agrega un usuario al grafo social
     * @param usuarioId ID del usuario
     */
    public void agregarUsuario(String usuarioId) {
        grafoSocial.agregarVertice(usuarioId);
    }

    /**
     * Conecta dos usuarios (se siguen mutuamente)
     * @param usuario1Id ID del primer usuario
     * @param usuario2Id ID del segundo usuario
     */
    public void conectarUsuarios(String usuario1Id, String usuario2Id) {
        // Verificar que ambos usuarios existen
        Optional<Usuario> u1 = usuarioRepository.findById(usuario1Id);
        Optional<Usuario> u2 = usuarioRepository.findById(usuario2Id);

        if (u1.isEmpty()) {
            throw new UsuarioNoEncontradoException(usuario1Id);
        }
        if (u2.isEmpty()) {
            throw new UsuarioNoEncontradoException(usuario2Id);
        }

        // Agregar vértices si no existen
        grafoSocial.agregarVertice(usuario1Id);
        grafoSocial.agregarVertice(usuario2Id);

        // Crear conexión bidireccional
        grafoSocial.agregarArista(usuario1Id, usuario2Id);
    }

    /**
     * Desconecta dos usuarios
     * @param usuario1Id ID del primer usuario
     * @param usuario2Id ID del segundo usuario
     */
    public void desconectarUsuarios(String usuario1Id, String usuario2Id) {
        grafoSocial.eliminarArista(usuario1Id, usuario2Id);
    }

    /**
     * Verifica si dos usuarios están conectados
     * @param usuario1Id ID del primer usuario
     * @param usuario2Id ID del segundo usuario
     * @return true si están conectados, false en caso contrario
     */
    public boolean estanConectados(String usuario1Id, String usuario2Id) {
        return grafoSocial.existeArista(usuario1Id, usuario2Id);
    }

    /**
     * Obtiene los amigos directos de un usuario
     * @param usuarioId ID del usuario
     * @return Lista de IDs de amigos
     */
    public ListaEnlazada<String> obtenerAmigos(String usuarioId) {
        ListaEnlazada<String> amigos = new ListaEnlazada<>();

        var vecinos = grafoSocial.obtenerVecinosDirectos(usuarioId);
        for (String amigo : vecinos) {
            amigos.agregar(amigo);
        }

        return amigos;
    }

    /**
     * RF-024: Encuentra "amigos de amigos" usando BFS
     * @param usuarioId ID del usuario
     * @return Lista de sugerencias de usuarios para seguir
     */
    public ListaEnlazada<String> obtenerSugerenciasAmigos(String usuarioId) {
        if (!grafoSocial.obtenerVertices().contains(usuarioId)) {
            return new ListaEnlazada<>();
        }

        return grafoSocial.encontrarSugerencias(usuarioId);
    }

    /**
     * Encuentra amigos a una distancia específica usando BFS
     * @param usuarioId ID del usuario
     * @param distancia Distancia (2 para amigos de amigos)
     * @return Lista de IDs de usuarios
     */
    public ListaEnlazada<String> obtenerAmigosADistancia(String usuarioId, int distancia) {
        return grafoSocial.bfs(usuarioId, distancia);
    }

    /**
     * Obtiene toda la red de un usuario (todos los usuarios alcanzables)
     * @param usuarioId ID del usuario
     * @return Lista de IDs de usuarios en la red
     */
    public ListaEnlazada<String> obtenerRedCompleta(String usuarioId) {
        return grafoSocial.bfsCompleto(usuarioId);
    }

    /**
     * Reconstruye el grafo social desde la base de datos
     */
    public void reconstruirGrafoSocial() {
        grafoSocial = new GrafoNoDirigido<>();

        var usuarios = usuarioRepository.findAll();

        // Agregar todos los usuarios como vértices
        for (Usuario usuario : usuarios) {
            grafoSocial.agregarVertice(usuario.getId());

            // Agregar conexiones basadas en la lista de seguidos
            if (usuario.getSeguidos() != null) {
                for (Usuario seguido : usuario.getSeguidos()) {
                    // Solo agregamos la arista si ambos se siguen (amistad bidireccional)
                    if (seguido.getSeguidos() != null) {
                        for (Usuario seguidoDelSeguido : seguido.getSeguidos()) {
                            if (seguidoDelSeguido.getId().equals(usuario.getId())) {
                                grafoSocial.agregarArista(usuario.getId(), seguido.getId());
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
}

