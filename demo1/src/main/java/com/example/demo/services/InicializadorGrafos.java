package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Componente de inicialización para construir estructuras de datos al arrancar la aplicación.
 * Ejecuta automáticamente la construcción de grafos e índices al iniciar Spring Boot.
 */
@Component
public class InicializadorGrafos implements CommandLineRunner {

    @Autowired
    private GrafoSocialService grafoSocialService;

    @Autowired
    private GrafoSimilitudService grafoSimilitudService;

    @Autowired
    private AutocompletadoService autocompletadoService;

    /**
     * Método ejecutado al inicio de la aplicación.
     * Construye el grafo social, el grafo de similitud de canciones y los índices de autocompletado.
     *
     * @param args Argumentos de línea de comandos
     * @throws Exception Si ocurre algún error durante la inicialización
     */
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
