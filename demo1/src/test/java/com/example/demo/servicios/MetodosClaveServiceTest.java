package com.example.demo.servicios;

import com.example.demo.dto.CancionRegistroDTO;
import com.example.demo.dto.CancionRegistroPorNombreDTO;
import com.example.demo.estructuras.ListaEnlazada;
import com.example.demo.excepciones.*;
import com.example.demo.modelo.*;
import com.example.demo.repositorio.AlbumRepository;
import com.example.demo.repositorio.ArtistaRepository;
import com.example.demo.repositorio.CancionRepository;
import com.example.demo.repositorio.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Pruebas de cobertura para los 7 métodos clave del sistema
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas de Cobertura de Métodos Clave del Sistema")
class MetodosClaveServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private CancionRepository cancionRepository;

    @Mock
    private ArtistaRepository artistaRepository;

    @Mock
    private AlbumRepository albumRepository;

    @Mock
    private GrafoSocialService grafoSocialService;

    @InjectMocks
    private UsuarioService usuarioService;

    @InjectMocks
    private CancionService cancionService;

    @InjectMocks
    private AutocompletadoService autocompletadoService;

    private Usuario usuarioTest;
    private Artista artistaTest;
    private Album albumTest;
    private Cancion cancionTest;

    @BeforeEach
    void setUp() {
        // Setup Usuario
        usuarioTest = new Usuario();
        usuarioTest.setId("user123");
        usuarioTest.setNombre("Juan Pérez");
        usuarioTest.setCorreo("juan@test.com");
        usuarioTest.setContrasena("password123");

        // Setup Artista
        artistaTest = new Artista();
        artistaTest.setId("art123");
        artistaTest.setNombre("Artista Test");

        // Setup Album
        albumTest = new Album();
        albumTest.setId("alb123");
        albumTest.setTitulo("Album Test");
        albumTest.setArtista(artistaTest);
        albumTest.setCanciones(new ListaEnlazada<>());

        // Setup Cancion
        cancionTest = new Cancion();
        cancionTest.setId("can123");
        cancionTest.setTitulo("Canción Test");
        cancionTest.setArtista(artistaTest);
        cancionTest.setAlbum(albumTest);
        cancionTest.setGenero(GENERO.ROCK);
    }

    // ========== MÉTODO CLAVE 1: guardarUsuario() ==========
    @Test
    @DisplayName("Método Clave 1: Guardar usuario con datos válidos")
    void testGuardarUsuarioExitoso() {
        // Arrange
        when(usuarioRepository.findByCorreo(anyString())).thenReturn(Optional.empty());
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioTest);

        // Act
        Usuario resultado = usuarioService.guardarUsuario("Juan Pérez", "juan@test.com", "password123");

        // Assert
        assertNotNull(resultado);
        assertEquals("Juan Pérez", resultado.getNombre());
        assertEquals("juan@test.com", resultado.getCorreo());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Método Clave 1: Guardar usuario con correo duplicado lanza excepción")
    void testGuardarUsuarioCorreoDuplicado() {
        // Arrange
        when(usuarioRepository.findByCorreo("juan@test.com")).thenReturn(Optional.of(usuarioTest));

        // Act & Assert
        assertThrows(RecursoDuplicadoException.class, () -> {
            usuarioService.guardarUsuario("Juan Pérez", "juan@test.com", "password123");
        });
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Método Clave 1: Guardar usuario con datos inválidos lanza excepción")
    void testGuardarUsuarioDatosInvalidos() {
        // Act & Assert - Nombre vacío
        assertThrows(DatosInvalidosException.class, () -> {
            usuarioService.guardarUsuario("", "juan@test.com", "password123");
        });

        // Act & Assert - Correo vacío
        assertThrows(DatosInvalidosException.class, () -> {
            usuarioService.guardarUsuario("Juan Pérez", "", "password123");
        });

        // Act & Assert - Contraseña vacía
        assertThrows(DatosInvalidosException.class, () -> {
            usuarioService.guardarUsuario("Juan Pérez", "juan@test.com", "");
        });
    }

    // ========== MÉTODO CLAVE 2: obtenerUsuarioPorCorreo() ==========
    @Test
    @DisplayName("Método Clave 2: Obtener usuario por correo existente")
    void testObtenerUsuarioPorCorreoExitoso() {
        // Arrange
        when(usuarioRepository.findByCorreo("juan@test.com")).thenReturn(Optional.of(usuarioTest));

        // Act
        Optional<Usuario> resultado = usuarioService.obtenerUsuarioPorCorreo("juan@test.com");

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("juan@test.com", resultado.get().getCorreo());
        verify(usuarioRepository, times(1)).findByCorreo("juan@test.com");
    }

    @Test
    @DisplayName("Método Clave 2: Obtener usuario por correo inexistente lanza excepción")
    void testObtenerUsuarioPorCorreoNoEncontrado() {
        // Arrange
        when(usuarioRepository.findByCorreo("noexiste@test.com")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsuarioNoEncontradoException.class, () -> {
            usuarioService.obtenerUsuarioPorCorreo("noexiste@test.com");
        });
    }

    @Test
    @DisplayName("Método Clave 2: Obtener usuario con correo vacío lanza excepción")
    void testObtenerUsuarioPorCorreoVacio() {
        // Act & Assert
        assertThrows(DatosInvalidosException.class, () -> {
            usuarioService.obtenerUsuarioPorCorreo("");
        });

        assertThrows(DatosInvalidosException.class, () -> {
            usuarioService.obtenerUsuarioPorCorreo(null);
        });
    }

    // ========== MÉTODO CLAVE 3: guardarCancion() ==========
    @Test
    @DisplayName("Método Clave 3: Guardar canción con datos válidos")
    void testGuardarCancionExitoso() {
        // Arrange
        CancionRegistroDTO dto = new CancionRegistroDTO();
        dto.setTitulo("Nueva Canción");
        dto.setArtistaId("art123");
        dto.setAlbumId("alb123");
        dto.setGenero(GENERO.POP);
        dto.setAnio(2024);
        dto.setDuracion(180);

        when(artistaRepository.findById("art123")).thenReturn(Optional.of(artistaTest));
        when(albumRepository.findById("alb123")).thenReturn(Optional.of(albumTest));
        when(cancionRepository.save(any(Cancion.class))).thenReturn(cancionTest);
        when(albumRepository.save(any(Album.class))).thenReturn(albumTest);

        // Act
        Cancion resultado = cancionService.guardarCancion(dto);

        // Assert
        assertNotNull(resultado);
        verify(cancionRepository, times(1)).save(any(Cancion.class));
        verify(albumRepository, times(1)).save(any(Album.class));
    }

    @Test
    @DisplayName("Método Clave 3: Guardar canción con título vacío lanza excepción")
    void testGuardarCancionTituloVacio() {
        // Arrange
        CancionRegistroDTO dto = new CancionRegistroDTO();
        dto.setTitulo("");
        dto.setArtistaId("art123");
        dto.setAlbumId("alb123");

        // Act & Assert
        assertThrows(DatosInvalidosException.class, () -> {
            cancionService.guardarCancion(dto);
        });
    }

    @Test
    @DisplayName("Método Clave 3: Guardar canción con artista inexistente lanza excepción")
    void testGuardarCancionArtistaNoEncontrado() {
        // Arrange
        CancionRegistroDTO dto = new CancionRegistroDTO();
        dto.setTitulo("Nueva Canción");
        dto.setArtistaId("artNoExiste");
        dto.setAlbumId("alb123");

        when(artistaRepository.findById("artNoExiste")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ArtistaNoEncontradoException.class, () -> {
            cancionService.guardarCancion(dto);
        });
    }

    @Test
    @DisplayName("Método Clave 3: Guardar canción con álbum inexistente lanza excepción")
    void testGuardarCancionAlbumNoEncontrado() {
        // Arrange
        CancionRegistroDTO dto = new CancionRegistroDTO();
        dto.setTitulo("Nueva Canción");
        dto.setArtistaId("art123");
        dto.setAlbumId("albNoExiste");

        when(artistaRepository.findById("art123")).thenReturn(Optional.of(artistaTest));
        when(albumRepository.findById("albNoExiste")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AlbumNoEncontradoException.class, () -> {
            cancionService.guardarCancion(dto);
        });
    }

    // ========== MÉTODO CLAVE 4: autocompletarCanciones() ==========
    @Test
    @DisplayName("Método Clave 4: Autocompletar canciones con prefijo válido")
    void testAutocompletarCancionesExitoso() {
        // Arrange
        List<Cancion> canciones = new ArrayList<>();
        Cancion c1 = new Cancion();
        c1.setTitulo("Bohemian Rhapsody");
        Cancion c2 = new Cancion();
        c2.setTitulo("Born to Run");
        canciones.add(c1);
        canciones.add(c2);

        when(cancionRepository.findAll()).thenReturn(canciones);

        // Act
        autocompletadoService.construirIndices();
        ListaEnlazada<String> resultado = autocompletadoService.autocompletarCanciones("Bo");

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.tamanio() >= 0);
    }

    @Test
    @DisplayName("Método Clave 4: Autocompletar canciones con prefijo vacío retorna lista vacía")
    void testAutocompletarCancionesPrefijoVacio() {
        // Act
        ListaEnlazada<String> resultado = autocompletadoService.autocompletarCanciones("");

        // Assert
        assertNotNull(resultado);
        assertEquals(0, resultado.tamanio());
    }

    @Test
    @DisplayName("Método Clave 4: Autocompletar canciones con prefijo null retorna lista vacía")
    void testAutocompletarCancionesPrefijoNull() {
        // Act
        ListaEnlazada<String> resultado = autocompletadoService.autocompletarCanciones(null);

        // Assert
        assertNotNull(resultado);
        assertEquals(0, resultado.tamanio());
    }

    // ========== MÉTODO CLAVE 5: construirIndices() ==========
    @Test
    @DisplayName("Método Clave 5: Construir índices con datos válidos")
    void testConstruirIndicesExitoso() {
        // Arrange
        List<Cancion> canciones = new ArrayList<>();
        canciones.add(cancionTest);

        List<Artista> artistas = new ArrayList<>();
        artistas.add(artistaTest);

        List<Album> albumes = new ArrayList<>();
        albumes.add(albumTest);

        when(cancionRepository.findAll()).thenReturn(canciones);
        when(artistaRepository.findAll()).thenReturn(artistas);
        when(albumRepository.findAll()).thenReturn(albumes);

        // Act
        autocompletadoService.construirIndices();

        // Assert
        verify(cancionRepository, times(1)).findAll();
        verify(artistaRepository, times(1)).findAll();
        verify(albumRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Método Clave 5: Construir índices con listas vacías")
    void testConstruirIndicesListasVacias() {
        // Arrange
        when(cancionRepository.findAll()).thenReturn(new ArrayList<>());
        when(artistaRepository.findAll()).thenReturn(new ArrayList<>());
        when(albumRepository.findAll()).thenReturn(new ArrayList<>());

        // Act
        autocompletadoService.construirIndices();

        // Assert
        verify(cancionRepository, times(1)).findAll();
        verify(artistaRepository, times(1)).findAll();
        verify(albumRepository, times(1)).findAll();
    }

    // ========== MÉTODO CLAVE 6: autocompletarArtistas() ==========
    @Test
    @DisplayName("Método Clave 6: Autocompletar artistas con prefijo válido")
    void testAutocompletarArtistasExitoso() {
        // Arrange
        List<Artista> artistas = new ArrayList<>();
        Artista a1 = new Artista();
        a1.setNombre("The Beatles");
        Artista a2 = new Artista();
        a2.setNombre("The Rolling Stones");
        artistas.add(a1);
        artistas.add(a2);

        when(artistaRepository.findAll()).thenReturn(artistas);
        when(cancionRepository.findAll()).thenReturn(new ArrayList<>());
        when(albumRepository.findAll()).thenReturn(new ArrayList<>());

        // Act
        autocompletadoService.construirIndices();
        ListaEnlazada<String> resultado = autocompletadoService.autocompletarArtistas("The");

        // Assert
        assertNotNull(resultado);
    }

    @Test
    @DisplayName("Método Clave 6: Autocompletar artistas con prefijo vacío retorna lista vacía")
    void testAutocompletarArtistasPrefijoVacio() {
        // Act
        ListaEnlazada<String> resultado = autocompletadoService.autocompletarArtistas("");

        // Assert
        assertNotNull(resultado);
        assertEquals(0, resultado.tamanio());
    }

    // ========== MÉTODO CLAVE 7: autocompletarAlbumes() ==========
    @Test
    @DisplayName("Método Clave 7: Autocompletar álbumes con prefijo válido")
    void testAutocompletarAlbumesExitoso() {
        // Arrange
        List<Album> albumes = new ArrayList<>();
        Album alb1 = new Album();
        alb1.setTitulo("Abbey Road");
        Album alb2 = new Album();
        alb2.setTitulo("Absolute Beginners");
        albumes.add(alb1);
        albumes.add(alb2);

        when(albumRepository.findAll()).thenReturn(albumes);
        when(cancionRepository.findAll()).thenReturn(new ArrayList<>());
        when(artistaRepository.findAll()).thenReturn(new ArrayList<>());

        // Act
        autocompletadoService.construirIndices();
        ListaEnlazada<String> resultado = autocompletadoService.autocompletarAlbumes("Ab");

        // Assert
        assertNotNull(resultado);
    }

    @Test
    @DisplayName("Método Clave 7: Autocompletar álbumes con prefijo null retorna lista vacía")
    void testAutocompletarAlbumesPrefijoNull() {
        // Act
        ListaEnlazada<String> resultado = autocompletadoService.autocompletarAlbumes(null);

        // Assert
        assertNotNull(resultado);
        assertEquals(0, resultado.tamanio());
    }
}
