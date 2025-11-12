package com.example.demo.controladores;

import com.example.demo.modelo.Cancion;
import com.example.demo.modelo.Playlist;
import com.example.demo.modelo.Usuario;
import com.example.demo.repositorio.CancionRepository;
import com.example.demo.repositorio.PlaylistRepository;
import com.example.demo.repositorio.UsuarioRepository;
import com.example.demo.estructuras.ListaEnlazada;
import com.example.demo.estructuras.ListaDoblementeEnlazada;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para gestionar playlists
 */
@RestController
@RequestMapping("/playlists")
@CrossOrigin(origins = "*")
public class PlaylistController {

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CancionRepository cancionRepository;

    /**
     * Obtener todas las playlists de un usuario
     * GET /playlists/usuario?correo=admin@gmail.com
     */
    @GetMapping("/usuario")
    public ResponseEntity<?> getPlaylistsByUsuario(@RequestParam String correo) {
        try {
            Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(correo);
            if (!usuarioOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Usuario no encontrado");
            }

            Usuario usuario = usuarioOpt.get();
            ListaEnlazada<Playlist> playlists = usuario.getListasDeReproduccion();
            
            if (playlists == null) {
                return ResponseEntity.ok(new ArrayList<>());
            }

            // Convertir ListaEnlazada a List para JSON
            List<Playlist> playlistList = new ArrayList<>();
            for (int i = 0; i < playlists.tamanio(); i++) {
                playlistList.add(playlists.obtener(i));
            }

            return ResponseEntity.ok(playlistList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al obtener playlists: " + e.getMessage());
        }
    }

    /**
     * Obtener una playlist por ID
     * GET /playlists/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getPlaylistById(@PathVariable String id) {
        try {
            Optional<Playlist> playlistOpt = playlistRepository.findById(id);
            if (!playlistOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Playlist no encontrada");
            }

            return ResponseEntity.ok(playlistOpt.get());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al obtener playlist: " + e.getMessage());
        }
    }

    /**
     * Crear una nueva playlist
     * POST /playlists?nombre=MiPlaylist&descripcion=Desc&correoCreador=admin@gmail.com
     */
    @PostMapping
    public ResponseEntity<?> createPlaylist(
            @RequestParam String nombre,
            @RequestParam(required = false) String descripcion,
            @RequestParam String correoCreador) {
        try {
            // Buscar usuario creador
            Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(correoCreador);
            if (!usuarioOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Usuario creador no encontrado");
            }

            Usuario creador = usuarioOpt.get();

            // Crear nueva playlist
            Playlist nuevaPlaylist = new Playlist();
            nuevaPlaylist.setNombre(nombre);
            nuevaPlaylist.setCanciones(new ListaEnlazada<>());
            nuevaPlaylist.setCreador(creador);
            nuevaPlaylist.setSeguidores(new ListaDoblementeEnlazada<>());

            // Guardar playlist en la BD
            Playlist playlistGuardada = playlistRepository.save(nuevaPlaylist);

            // Agregar playlist a la lista del usuario
            if (creador.getListasDeReproduccion() == null) {
                creador.setListasDeReproduccion(new ListaEnlazada<>());
            }
            creador.getListasDeReproduccion().agregar(playlistGuardada);
            usuarioRepository.save(creador);

            return ResponseEntity.status(HttpStatus.CREATED).body(playlistGuardada);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al crear playlist: " + e.getMessage());
        }
    }

    /**
     * Actualizar nombre de playlist
     * PUT /playlists/{id}?nombre=NuevoNombre
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePlaylistName(
            @PathVariable String id,
            @RequestParam String nombre) {
        try {
            Optional<Playlist> playlistOpt = playlistRepository.findById(id);
            if (!playlistOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Playlist no encontrada");
            }

            Playlist playlist = playlistOpt.get();
            playlist.setNombre(nombre);
            Playlist playlistActualizada = playlistRepository.save(playlist);

            return ResponseEntity.ok(playlistActualizada);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al actualizar playlist: " + e.getMessage());
        }
    }

    /**
     * Eliminar una playlist
     * DELETE /playlists/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePlaylist(@PathVariable String id) {
        try {
            Optional<Playlist> playlistOpt = playlistRepository.findById(id);
            if (!playlistOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Playlist no encontrada");
            }

            Playlist playlist = playlistOpt.get();
            Usuario creador = playlist.getCreador();

            // Eliminar de la lista del usuario
            if (creador != null && creador.getListasDeReproduccion() != null) {
                creador.getListasDeReproduccion().eliminar(playlist);
                usuarioRepository.save(creador);
            }

            // Eliminar de la BD
            playlistRepository.deleteById(id);

            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al eliminar playlist: " + e.getMessage());
        }
    }

    /**
     * Agregar canción a playlist
     * POST /playlists/{id}/canciones?tituloCancion=TituloCancion
     */
    @PostMapping("/{id}/canciones")
    public ResponseEntity<?> addCancionToPlaylist(
            @PathVariable String id,
            @RequestParam String tituloCancion) {
        try {
            Optional<Playlist> playlistOpt = playlistRepository.findById(id);
            if (!playlistOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Playlist no encontrada");
            }

            Optional<Cancion> cancionOpt = cancionRepository.findByTitulo(tituloCancion);
            if (!cancionOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Canción no encontrada");
            }

            Playlist playlist = playlistOpt.get();
            Cancion cancion = cancionOpt.get();

            if (playlist.getCanciones() == null) {
                playlist.setCanciones(new ListaEnlazada<>());
            }

            // Verificar si ya existe
            boolean yaExiste = false;
            for (int i = 0; i < playlist.getCanciones().tamanio(); i++) {
                if (playlist.getCanciones().obtener(i).getTitulo().equals(tituloCancion)) {
                    yaExiste = true;
                    break;
                }
            }

            if (yaExiste) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("La canción ya está en la playlist");
            }

            playlist.getCanciones().agregar(cancion);
            Playlist playlistActualizada = playlistRepository.save(playlist);

            return ResponseEntity.ok(playlistActualizada);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al agregar canción: " + e.getMessage());
        }
    }

    /**
     * Eliminar canción de playlist
     * DELETE /playlists/{id}/canciones?tituloCancion=TituloCancion
     */
    @DeleteMapping("/{id}/canciones")
    public ResponseEntity<?> removeCancionFromPlaylist(
            @PathVariable String id,
            @RequestParam String tituloCancion) {
        try {
            Optional<Playlist> playlistOpt = playlistRepository.findById(id);
            if (!playlistOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Playlist no encontrada");
            }

            Playlist playlist = playlistOpt.get();
            
            if (playlist.getCanciones() == null || playlist.getCanciones().tamanio() == 0) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("La playlist está vacía");
            }

            // Buscar y eliminar la canción
            boolean eliminada = false;
            for (int i = 0; i < playlist.getCanciones().tamanio(); i++) {
                if (playlist.getCanciones().obtener(i).getTitulo().equals(tituloCancion)) {
                    playlist.getCanciones().eliminarEn(i);
                    eliminada = true;
                    break;
                }
            }

            if (!eliminada) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Canción no encontrada en la playlist");
            }

            playlistRepository.save(playlist);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al eliminar canción: " + e.getMessage());
        }
    }

    /**
     * Seguir una playlist
     * POST /playlists/{id}/seguir?correoUsuario=user@gmail.com
     */
    @PostMapping("/{id}/seguir")
    public ResponseEntity<?> followPlaylist(
            @PathVariable String id,
            @RequestParam String correoUsuario) {
        try {
            Optional<Playlist> playlistOpt = playlistRepository.findById(id);
            Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(correoUsuario);

            if (!playlistOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Playlist no encontrada");
            }
            if (!usuarioOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Usuario no encontrado");
            }

            Playlist playlist = playlistOpt.get();
            Usuario usuario = usuarioOpt.get();

            if (playlist.getSeguidores() == null) {
                playlist.setSeguidores(new ListaDoblementeEnlazada<>());
            }

            playlist.getSeguidores().agregar(usuario);
            Playlist playlistActualizada = playlistRepository.save(playlist);

            return ResponseEntity.ok(playlistActualizada);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al seguir playlist: " + e.getMessage());
        }
    }

    /**
     * Dejar de seguir una playlist
     * DELETE /playlists/{id}/seguir?correoUsuario=user@gmail.com
     */
    @DeleteMapping("/{id}/seguir")
    public ResponseEntity<?> unfollowPlaylist(
            @PathVariable String id,
            @RequestParam String correoUsuario) {
        try {
            Optional<Playlist> playlistOpt = playlistRepository.findById(id);
            if (!playlistOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Playlist no encontrada");
            }

            Playlist playlist = playlistOpt.get();

            if (playlist.getSeguidores() != null) {
                for (int i = 0; i < playlist.getSeguidores().tamanio(); i++) {
                    if (playlist.getSeguidores().obtener(i).getCorreo().equals(correoUsuario)) {
                        playlist.getSeguidores().eliminarEn(i);
                        break;
                    }
                }
                playlistRepository.save(playlist);
            }

            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al dejar de seguir playlist: " + e.getMessage());
        }
    }
}
