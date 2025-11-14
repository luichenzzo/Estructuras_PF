package com.example.demo.controladores;

import com.example.demo.modelo.Cancion;
import com.example.demo.modelo.Playlist;
import com.example.demo.modelo.Usuario;
import com.example.demo.repositorio.CancionRepository;
import com.example.demo.repositorio.PlaylistRepository;
import com.example.demo.repositorio.UsuarioRepository;
import com.example.demo.estructuras.ListaEnlazada;
import com.example.demo.estructuras.ListaDoblementeEnlazada;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;

/**
 * Controlador REST para gestionar playlists
 */
@RestController
@RequestMapping("/playlists")
public class PlaylistController {

    private static final Logger logger = LoggerFactory.getLogger(PlaylistController.class);

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CancionRepository cancionRepository;

    // Helper: convertir una Playlist a un DTO simple (Map) para evitar serializar estructuras personalizadas
    private Map<String, Object> playlistToDto(Playlist p) {
        if (p == null) return null;
        Map<String, Object> dto = new HashMap<>();
        dto.put("id", p.getId());
        dto.put("nombre", p.getNombre());
        dto.put("descripcion", p.getDescripcion());
        dto.put("correoCreador", p.getCreador() != null ? p.getCreador().getCorreo() : null);

        List<Map<String, Object>> canciones = new ArrayList<>();
        ListaEnlazada<Cancion> listaCanciones = p.getCanciones();
        if (listaCanciones != null) {
            for (int i = 0; i < listaCanciones.tamanio(); i++) {
                Cancion c = listaCanciones.obtener(i);
                if (c != null) {
                    Map<String, Object> cDto = new HashMap<>();
                    cDto.put("id", c.getId());
                    cDto.put("titulo", c.getTitulo());
                    canciones.add(cDto);
                }
            }
        }
        dto.put("canciones", canciones);

        List<String> seguidores = new ArrayList<>();
        ListaDoblementeEnlazada<Usuario> listaSeguidores = p.getSeguidores();
        if (listaSeguidores != null) {
            for (int i = 0; i < listaSeguidores.tamanio(); i++) {
                Usuario u = listaSeguidores.obtener(i);
                if (u != null) seguidores.add(u.getCorreo());
            }
        }
        dto.put("seguidores", seguidores);

        return dto;
    }

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

            // Convertir ListaEnlazada a List<DTO> para JSON (evitar serializar estructuras personalizadas)
            List<Map<String, Object>> playlistDtos = new ArrayList<>();
            for (int i = 0; i < playlists.tamanio(); i++) {
                playlistDtos.add(playlistToDto(playlists.obtener(i)));
            }

            return ResponseEntity.ok(playlistDtos);
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

            return ResponseEntity.ok(playlistToDto(playlistOpt.get()));
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
            @RequestParam String correoCreador,
            HttpServletRequest request) {
        try {
            // Log request details to help diagnose 500 errors from frontend
            logger.info("POST /playlists invoked. URL={} query={} remoteAddr={}",
                    request.getRequestURL(), request.getQueryString(), request.getRemoteAddr());
            logger.info("createPlaylist params -> nombre='{}', correoCreador='{}', descripcion='{}'",
                    nombre, correoCreador, descripcion);

            // (optional) log some headers useful for debugging
            String ua = request.getHeader("User-Agent");
            if (ua != null) logger.debug("User-Agent: {}", ua);

            // Buscar usuario creador
            Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(correoCreador);
            if (!usuarioOpt.isPresent()) {
                logger.warn("Usuario creador no encontrado: {}", correoCreador);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Usuario creador no encontrado");
            }

            Usuario creador = usuarioOpt.get();

            // Crear nueva playlist
            Playlist nuevaPlaylist = new Playlist();
            nuevaPlaylist.setNombre(nombre);
            nuevaPlaylist.setDescripcion(descripcion);
            // No inicializamos las colecciones personalizadas antes de guardar porque
            // Spring Data Mongo puede fallar al mapear tipos personalizados (ListaEnlazada, etc.).
            // Las dejamos null para que Mongo almacene solo campos serializables.
            nuevaPlaylist.setCanciones(null);
            nuevaPlaylist.setCreador(creador);
            nuevaPlaylist.setSeguidores(null);

            // Guardar playlist en la BD
            Playlist playlistGuardada = playlistRepository.save(nuevaPlaylist);

            // Agregar playlist a la lista del usuario
            // No persistimos la modificación de la lista de reproduccion del usuario aquí
            // (evita intentar guardar la estructura personalizada). Si necesitas mantener
            // la relación en el usuario, es mejor migrar ese campo a un List<String> de IDs
            // o implementar un convertidor personalizado.

            return ResponseEntity.status(HttpStatus.CREATED).body(playlistToDto(playlistGuardada));
        } catch (Exception e) {
            // Log full exception with stacktrace
            logger.error("Error al crear playlist (nombre={}, correoCreador={})", nombre, correoCreador, e);
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

            return ResponseEntity.ok(playlistToDto(playlistActualizada));
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

            // Buscar canción por título - manejar múltiples resultados
            List<Cancion> cancionesEncontradas = cancionRepository.findAll().stream()
                .filter(c -> c.getTitulo().equals(tituloCancion))
                .collect(java.util.stream.Collectors.toList());
            
            if (cancionesEncontradas.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Canción no encontrada");
            }
            
            // Tomar la primera canción encontrada
            Cancion cancion = cancionesEncontradas.get(0);

            Playlist playlist = playlistOpt.get();

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

            return ResponseEntity.ok(playlistToDto(playlistActualizada));
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

            return ResponseEntity.ok(playlistToDto(playlistActualizada));
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
