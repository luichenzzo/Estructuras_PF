package com.example.demo.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Componente de inicialización para construir los grafos al iniciar la aplicación
 */
@Component
public class InicializadorGrafos implements CommandLineRunner {

    @Autowired
    private GrafoSocialService grafoSocialService;

    @Autowired
    private GrafoSimilitudService grafoSimilitudService;

    @Autowired
    private AutocompletadoService autocompletadoService;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("===========================================");
        System.out.println("Inicializando estructuras de datos...");
        System.out.println("===========================================");

        try {
            // Construir grafo social
            System.out.println("Construyendo grafo social...");
            grafoSocialService.reconstruirGrafoSocial();
            System.out.println("✓ Grafo social construido exitosamente");

            // Construir grafo de similitud
            System.out.println("Construyendo grafo de similitud de canciones...");
            grafoSimilitudService.construirGrafoSimilitud();
            System.out.println("✓ Grafo de similitud construido exitosamente");

            // Construir índices de autocompletado
            System.out.println("Construyendo índices de autocompletado (Trie)...");
            autocompletadoService.construirIndices();
            System.out.println("✓ Índices de autocompletado construidos exitosamente");

            System.out.println("===========================================");
            System.out.println("Todas las estructuras de datos inicializadas correctamente");
            System.out.println("===========================================");

        } catch (Exception e) {
            System.err.println("Error al inicializar estructuras de datos: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

