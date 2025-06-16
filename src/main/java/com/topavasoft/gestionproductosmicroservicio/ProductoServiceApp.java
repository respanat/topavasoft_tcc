package com.topavasoft.gestionproductosmicroservicio;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder; // Para un mejor formato JSON
import com.topavasoft.gestionproductosmicroservicio.application.service.ProductoService;
import com.topavasoft.gestionproductosmicroservicio.infraestructura.adaptador.firebase.FirebaseProductoRepositoryAdapter;
import com.topavasoft.gestionproductosmicroservicio.infraestructura.adaptador.rest.ProductoController;
import com.topavasoft.gestionproductosmicroservicio.infraestructura.config.FirebaseConfig;
import spark.Spark; // Para stop()

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase principal que inicializa y arranca el microservicio de productos.
 * Esta es la clase "Main" de nuestra aplicación, donde se configuran las dependencias
 * y se lanzan los adaptadores.
 * (Principio SOLID: Dependency Inversion Principle - Se instancian las implementaciones concretas
 * y se inyectan en las abstracciones, como ProductoService).
 */
public class ProductoServiceApp {

    private static final Logger LOGGER = Logger.getLogger(ProductoServiceApp.class.getName());

    public static void main(String[] args) {
        LOGGER.info("Iniciando el microservicio de gestión de productos...");

        // 1. Inicializar Firebase
        // Esto debe hacerse primero para que FirestoreClient.getFirestore() funcione.
        FirebaseConfig.initialize();
        LOGGER.info("Firebase ha sido inicializado.");

        // 2. Configurar Gson para el manejo de JSON (con pretty printing para legibilidad)
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        LOGGER.info("Gson configurado.");

        // 3. Instanciar los adaptadores de infraestructura y la capa de aplicación
        // Se crea la implementación concreta del repositorio
        FirebaseProductoRepositoryAdapter productoRepository = new FirebaseProductoRepositoryAdapter();
        LOGGER.info("FirebaseProductoRepositoryAdapter instanciado.");

        // Se inyecta la implementación concreta en el servicio de aplicación (dependencia de abstracción)
        ProductoService productoService = new ProductoService(productoRepository);
        LOGGER.info("ProductoService instanciado y conectado al repositorio.");

        // Se inyecta el servicio de aplicación y Gson en el controlador REST
        ProductoController productoController = new ProductoController(productoService, gson);
        LOGGER.info("ProductoController instanciado.");

        // 4. Configurar los endpoints REST de Spark Framework
        productoController.setupEndpoints();
        LOGGER.info("Endpoints REST configurados en Spark Framework.");

        // 5. Opcional: Agregar un hook de apagado para limpiar recursos 
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOGGER.info("Apagando el microservicio...");
            Spark.stop(); // Detiene el servidor Spark
            LOGGER.info("Microservicio apagado.");
        }));

        LOGGER.info("Microservicio de gestión de productos iniciado y escuchando en el puerto por defecto (4567) o el configurado (ej. 8080).");
        LOGGER.info("Puedes probar los endpoints REST ahora.");
    }
}
