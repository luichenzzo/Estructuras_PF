package com.example.demo.services;


import com.example.demo.model.Cancion;
import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.repository.CancionRepository;
import com.example.demo.structures.ListaEnlazada;
import com.example.demo.exceptions.UsuarioNoEncontradoException;
import com.example.demo.exceptions.DatosInvalidosException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GeneralService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CancionRepository cancionRepository;

    @Autowired
    private GrafoSimilitudService grafoSimilitudService;

    /**
     * RF-005: Genera una playlist "Descubrimiento Semanal" basada en los gustos del usuario
     * Utiliza las canciones favoritas del usuario y genera recomendaciones basadas en similitud
     * @param correoUsuario Correo del usuario
     * @return Lista de canciones recomendadas
     */
    public List<Cancion> generarDescubrimientoSemanal(String correoUsuario) {
        if (correoUsuario == null || correoUsuario.trim().isEmpty()) {
            throw new DatosInvalidosException("El correo de usuario no puede estar vacío");
        }

        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(correoUsuario);
        if (usuarioOpt.isEmpty()) {
            throw new UsuarioNoEncontradoException(correoUsuario);
        }

        Usuario usuario = usuarioOpt.get();
        ListaEnlazada<Cancion> favoritos = usuario.getListaFavoritos();

        // Si el usuario no tiene favoritos, devolver canciones aleatorias
        if (favoritos == null || !favoritos.iterator().hasNext()) {
            List<Cancion> todasLasCanciones = cancionRepository.findAll();
            Collections.shuffle(todasLasCanciones);
            return todasLasCanciones.subList(0, Math.min(20, todasLasCanciones.size()));
        }

        // Usar un Set para evitar duplicados
        Set<String> cancionesRecomendadasIds = new HashSet<>();
        List<Cancion> cancionesRecomendadas = new ArrayList<>();

        // Por cada canción favorita, obtener canciones similares
        int contadorFavoritos = 0;
        for (Cancion favorita : favoritos) {
            if (contadorFavoritos >= 5) break; // Limitar a las primeras 5 canciones favoritas

            try {
                ListaEnlazada<String> similares = grafoSimilitudService.obtenerCancionesSimilares(favorita.getId());

                int contadorSimilares = 0;
                for (String idSimilar : similares) {
                    if (contadorSimilares >= 4) break; // Hasta 4 similares por favorita

                    // No agregar si ya está en favoritos o en recomendadas
                    if (!cancionesRecomendadasIds.contains(idSimilar)) {
                        Optional<Cancion> cancionOpt = cancionRepository.findById(idSimilar);
                        if (cancionOpt.isPresent()) {
                            Cancion cancion = cancionOpt.get();
                            // Verificar que no esté en favoritos
                            boolean estaEnFavoritos = false;
                            for (Cancion fav : favoritos) {
                                if (fav.getId().equals(idSimilar)) {
                                    estaEnFavoritos = true;
                                    break;
                                }
                            }

                            if (!estaEnFavoritos) {
                                cancionesRecomendadas.add(cancion);
                                cancionesRecomendadasIds.add(idSimilar);
                                contadorSimilares++;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                // Si no hay similares para esta canción, continuar con la siguiente
                continue;
            }

            contadorFavoritos++;
        }

        // Si no hay suficientes recomendaciones, completar con canciones aleatorias
        if (cancionesRecomendadas.size() < 20) {
            List<Cancion> todasLasCanciones = cancionRepository.findAll();
            Collections.shuffle(todasLasCanciones);

            for (Cancion cancion : todasLasCanciones) {
                if (cancionesRecomendadas.size() >= 20) break;

                if (!cancionesRecomendadasIds.contains(cancion.getId())) {
                    // Verificar que no esté en favoritos
                    boolean estaEnFavoritos = false;
                    for (Cancion fav : favoritos) {
                        if (fav.getId().equals(cancion.getId())) {
                            estaEnFavoritos = true;
                            break;
                        }
                    }

                    if (!estaEnFavoritos) {
                        cancionesRecomendadas.add(cancion);
                        cancionesRecomendadasIds.add(cancion.getId());
                    }
                }
            }
        }

        return cancionesRecomendadas;
    }

    /**
     * RF-009: Genera un reporte CSV de las canciones favoritas de un usuario
     * @param correoUsuario Correo del usuario
     * @return String con el contenido CSV
     */
    public String generarReporteCSV(String correoUsuario) {
        if (correoUsuario == null || correoUsuario.trim().isEmpty()) {
            throw new DatosInvalidosException("El correo de usuario no puede estar vacío");
        }

        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(correoUsuario);
        if (usuarioOpt.isEmpty()) {
            throw new UsuarioNoEncontradoException(correoUsuario);
        }

        Usuario usuario = usuarioOpt.get();
        ListaEnlazada<Cancion> favoritos = usuario.getListaFavoritos();

        StringBuilder csv = new StringBuilder();
        // Encabezados
        csv.append("Título,Artista,Álbum,Género,Año,Duración (min)\n");

        // Si no hay favoritos, retornar solo encabezados
        if (favoritos == null || !favoritos.iterator().hasNext()) {
            return csv.toString();
        }

        // Agregar cada canción favorita
        for (Cancion cancion : favoritos) {
            csv.append(escaparCSV(cancion.getTitulo())).append(",");
            csv.append(escaparCSV(cancion.getArtista() != null ? cancion.getArtista().getNombre() : "")).append(",");
            csv.append(escaparCSV(cancion.getAlbum() != null ? cancion.getAlbum().getTitulo() : "")).append(",");
            csv.append(escaparCSV(cancion.getGenero() != null ? cancion.getGenero().toString() : "")).append(",");
            csv.append(cancion.getAnio()).append(",");

            // Convertir duración a minutos (asumiendo que está en segundos)
            double minutos = cancion.getDuracion() / 60.0;
            csv.append(String.format("%.2f", minutos));

            csv.append("\n");
        }

        return csv.toString();
    }

    /**
     * Escapa valores para formato CSV (maneja comas y comillas)
     */
    private String escaparCSV(String valor) {
        if (valor == null) {
            return "";
        }

        // Si contiene coma, comilla o salto de línea, envolver en comillas
        if (valor.contains(",") || valor.contains("\"") || valor.contains("\n")) {
            // Duplicar comillas internas
            valor = valor.replace("\"", "\"\"");
            return "\"" + valor + "\"";
        }

        return valor;
    }
}
