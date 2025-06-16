package com.topavasoft.gestionproductosmicroservicio.infraestructura.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level; 
import java.util.logging.Logger;

/**
 * Clase de configuración para inicializar Firebase.
 * Esta clase gestiona la conexión con Firebase utilizando las credenciales de la cuenta de servicio.
 */
public class FirebaseConfig {

    private static final Logger LOGGER = Logger.getLogger(FirebaseConfig.class.getName());
    private static boolean firebaseInitialized = false;

    public static void initialize() {
        if (firebaseInitialized) {
            LOGGER.info("Firebase ya ha sido inicializado. Saltando la inicialización.");
            return; // Evita inicializar múltiples veces
        }

        try {
            InputStream serviceAccount = FirebaseConfig.class.getClassLoader().getResourceAsStream("firebase-credentials.json");

            if (serviceAccount == null) {
                throw new FileNotFoundException("El archivo de credenciales de Firebase no se encontró en src/main/resources. Asegúrate de haberlo copiado correctamente.");
            }

            FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

            FirebaseApp.initializeApp(options);
            firebaseInitialized = true;
            LOGGER.info("Firebase inicializado con éxito.");

        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Error: Archivo de credenciales de Firebase no encontrado. " + e.getMessage(), e);
            throw new RuntimeException("No se pudo inicializar Firebase: Archivo de credenciales no encontrado.", e);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error al leer las credenciales de Firebase: " + e.getMessage(), e);
            throw new RuntimeException("No se pudo inicializar Firebase: Error de IO.", e);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error inesperado durante la inicialización de Firebase: " + e.getMessage(), e);
            throw new RuntimeException("No se pudo inicializar Firebase: Error inesperado.", e);
        }
    }
}
