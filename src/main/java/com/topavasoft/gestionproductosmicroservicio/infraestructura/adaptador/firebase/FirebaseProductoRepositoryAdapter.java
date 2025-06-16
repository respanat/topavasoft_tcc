package com.topavasoft.gestionproductosmicroservicio.infraestructura.adaptador.firebase;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.google.cloud.Timestamp;
import com.topavasoft.gestionproductosmicroservicio.domain.model.Producto;
import com.topavasoft.gestionproductosmicroservicio.domain.port.ProductoRepository;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Adaptador de infraestructura para Firebase Firestore.
 * Implementa la interfaz ProductoRepository del dominio, manejando la lógica
 * de persistencia de productos en Firebase.
 * (Principio SOLID: Liskov Substitution Principle - cumple el contrato de ProductoRepository;
 * Dependency Inversion Principle - el dominio no sabe de esta implementación concreta).
 */
public class FirebaseProductoRepositoryAdapter implements ProductoRepository {

    private static final Logger LOGGER = Logger.getLogger(FirebaseProductoRepositoryAdapter.class.getName());
    private final Firestore db;
    private final CollectionReference productosCollection;

    public FirebaseProductoRepositoryAdapter() {
        // Obtener la instancia de Firestore una vez que Firebase ya ha sido inicializado
        this.db = FirestoreClient.getFirestore();
        this.productosCollection = db.collection("productos");
    }

    private Producto mapDocumentToProduct(DocumentSnapshot document) {
        if (document == null || !document.exists()) {
            return null;
        }

        // Obtener los Timestamps de Firestore
        Timestamp firebaseFechaCreacion = document.getTimestamp("fechaCreacion");
        Timestamp firebaseFechaActualizacion = document.getTimestamp("fechaActualizacion");

        // Convertir com.google.cloud.Timestamp a java.time.LocalDateTime
        LocalDateTime fechaCreacion = firebaseFechaCreacion != null 
            ? LocalDateTime.ofInstant(firebaseFechaCreacion.toDate().toInstant(), ZoneOffset.UTC) 
            : null;
        LocalDateTime fechaActualizacion = firebaseFechaActualizacion != null 
            ? LocalDateTime.ofInstant(firebaseFechaActualizacion.toDate().toInstant(), ZoneOffset.UTC) 
            : null;

        return new Producto(
            document.getId(),
            document.getString("tipoPaquete"),
            document.getString("tipoCliente"),
            document.getDouble("costoPaquete"),
            document.getString("metodoEnvio"),
            fechaCreacion,
            fechaActualizacion
        );
    }

    private Map<String, Object> mapProductToMap(Producto product) {
        // Mapea la entidad de dominio Producto a un Map para Firestore
        Map<String, Object> docData = new HashMap<>();
        docData.put("tipoPaquete", product.getTipoPaquete());
        docData.put("tipoCliente", product.getTipoCliente());
        docData.put("costoPaquete", product.getCostoPaquete());
        docData.put("metodoEnvio", product.getMetodoEnvio());
        docData.put("fechaCreacion", product.getFechaCreacion() != null ? com.google.cloud.Timestamp.of(java.sql.Timestamp.valueOf(product.getFechaCreacion())) : null);
        docData.put("fechaActualizacion", product.getFechaActualizacion() != null ? com.google.cloud.Timestamp.of(java.sql.Timestamp.valueOf(product.getFechaActualizacion())) : null);
        return docData;
    }

    @Override
    public Producto save(Producto product) {
        try {
            // Si el producto ya tiene un ID, es una actualización
            if (product.getId() != null && !product.getId().isEmpty()) {
                return update(product); // Delegar a la lógica de actualización
            }

            // Para un nuevo producto, Firestore generará un ID
            DocumentReference docRef = productosCollection.document();
            ApiFuture<WriteResult> result = docRef.set(mapProductToMap(product));
            result.get(); // Esperar la confirmación de la escritura
            LOGGER.info("Producto guardado con ID: " + docRef.getId());

            // Retornar el producto con el ID generado por Firestore
            return new Producto(
                docRef.getId(),
                product.getTipoPaquete(),
                product.getTipoCliente(),
                product.getCostoPaquete(),
                product.getMetodoEnvio(),
                product.getFechaCreacion(),
                product.getFechaActualizacion()
            );
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.log(Level.SEVERE, "Error al guardar producto en Firebase: " + e.getMessage(), e);
            throw new RuntimeException("Error al guardar producto en Firebase.", e);
        }
    }

    @Override
    public Optional<Producto> findById(String id) {
        try {
            DocumentReference docRef = productosCollection.document(id);
            ApiFuture<DocumentSnapshot> future = docRef.get();
            DocumentSnapshot document = future.get(); // Esperar el resultado
            if (document.exists()) {
                LOGGER.info("Producto encontrado con ID: " + id);
                return Optional.ofNullable(mapDocumentToProduct(document));
            } else {
                LOGGER.info("Producto con ID " + id + " no encontrado.");
                return Optional.empty();
            }
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.log(Level.SEVERE, "Error al buscar producto por ID en Firebase: " + e.getMessage(), e);
            throw new RuntimeException("Error al buscar producto por ID en Firebase.", e);
        }
    }

    @Override
    public List<Producto> findAll() {
        List<Producto> productos = new ArrayList<>();
        try {
            ApiFuture<QuerySnapshot> future = productosCollection.get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                productos.add(mapDocumentToProduct(document));
            }
            LOGGER.info("Se encontraron " + productos.size() + " productos.");
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.log(Level.SEVERE, "Error al buscar todos los productos en Firebase: " + e.getMessage(), e);
            throw new RuntimeException("Error al buscar todos los productos en Firebase.", e);
        }
        return productos;
    }

    @Override
    public Producto update(Producto product) {
        if (product.getId() == null || product.getId().isEmpty()) {
            throw new IllegalArgumentException("El ID del producto es necesario para la actualización.");
        }
        try {
            DocumentReference docRef = productosCollection.document(product.getId());
            ApiFuture<WriteResult> result = docRef.set(mapProductToMap(product)); // set() reemplaza el documento
            result.get(); // Esperar la confirmación
            LOGGER.info("Producto actualizado con ID: " + product.getId());
            return product; // Retornamos el producto actualizado
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.log(Level.SEVERE, "Error al actualizar producto en Firebase: " + e.getMessage(), e);
            throw new RuntimeException("Error al actualizar producto en Firebase.", e);
        }
    }

    @Override
    public void deleteById(String id) {
        try {
            ApiFuture<WriteResult> result = productosCollection.document(id).delete();
            result.get(); // Esperar la confirmación
            LOGGER.info("Producto eliminado con ID: " + id);
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.log(Level.SEVERE, "Error al eliminar producto en Firebase: " + e.getMessage(), e);
            throw new RuntimeException("Error al eliminar producto en Firebase.", e);
        }
    }
}
