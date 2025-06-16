package com.topavasoft.gestionproductosmicroservicio.infraestructura.adaptador.rest;

import com.google.gson.Gson;
import com.topavasoft.gestionproductosmicroservicio.application.dto.ProductoActualizarRequest;
import com.topavasoft.gestionproductosmicroservicio.application.dto.ProductoCrearRequest;
import com.topavasoft.gestionproductosmicroservicio.application.dto.ProductoResponse;
import com.topavasoft.gestionproductosmicroservicio.application.service.ProductoService;
import static spark.Spark.*; 
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Adaptador de entrada REST para el microservicio de gestión de productos.
 * Este controlador recibe las solicitudes HTTP, las mapea a los servicios de aplicación
 * y devuelve las respuestas en formato JSON.
 * (Principio SOLID: Open/Closed Principle - puede extenderse con nuevos endpoints sin modificar
 * la lógica interna del servicio; Dependency Inversion Principle - depende de ProductoService, una abstracción).
 */
public class ProductoController {

    private static final Logger LOGGER = Logger.getLogger(ProductoController.class.getName());
    private final ProductoService productoService;
    private final Gson gson; // Usaremos Gson para JSON

    public ProductoController(ProductoService productoService, Gson gson) {
        this.productoService = productoService;
        this.gson = gson; // Inyectamos Gson
    }

    public void setupEndpoints() {
        after((request, response) -> {
            response.type("application/json");
        });

        // Manejo global de excepciones para bad requests
        exception(IllegalArgumentException.class, (e, req, res) -> {
            res.status(400); // Bad Request
            res.body(gson.toJson(new ErrorResponse(e.getMessage())));
            LOGGER.log(Level.WARNING, "Bad Request: " + e.getMessage(), e);
        });

        // Manejo global de excepciones para recursos no encontrados
        exception(ProductoNotFoundException.class, (e, req, res) -> {
            res.status(404); // Not Found
            res.body(gson.toJson(new ErrorResponse(e.getMessage())));
            LOGGER.log(Level.INFO, "Producto no encontrado: " + e.getMessage());
        });

        // Manejo global de excepciones para errores internos del servidor
        exception(RuntimeException.class, (e, req, res) -> {
            res.status(500); // Internal Server Error
            res.body(gson.toJson(new ErrorResponse("Error interno del servidor: " + e.getMessage())));
            LOGGER.log(Level.SEVERE, "Error interno del servidor: " + e.getMessage(), e);
        });


        // Endpoint para Crear un Producto (POST /api/productos)
        post("/api/productos", (request, response) -> {
            try {
                ProductoCrearRequest req = gson.fromJson(request.body(), ProductoCrearRequest.class);
                
                ProductoResponse createdProduct = productoService.crearProducto(req);
                response.status(201); // Created
                return gson.toJson(createdProduct);
            } catch (IllegalArgumentException e) {
                throw e; 
            } catch (Exception e) {
                throw new RuntimeException("Error al crear producto: " + e.getMessage(), e); 
            }
        });

        get("/api/productos", (request, response) -> {
            List<ProductoResponse> productos = productoService.obtenerTodosLosProductos();
            response.status(200); // OK
            return gson.toJson(productos);
        });

        // Endpoint para Obtener un Producto por ID (GET /api/productos/:id)
        get("/api/productos/:id", (request, response) -> {
            String id = request.params(":id");
            Optional<ProductoResponse> producto = productoService.obtenerProductoPorId(id);
            if (producto.isPresent()) {
                response.status(200); // OK
                return gson.toJson(producto.get());
            } else {
                throw new ProductoNotFoundException("Producto con ID " + id + " no encontrado.");
            }
        });

        // Endpoint para Actualizar un Producto (PUT /api/productos/:id)
        put("/api/productos/:id", (request, response) -> {
            try {
                String id = request.params(":id");
                ProductoActualizarRequest req = gson.fromJson(request.body(), ProductoActualizarRequest.class);
                Optional<ProductoResponse> updatedProduct = productoService.actualizarProducto(id, req);
                if (updatedProduct.isPresent()) {
                    response.status(200); // OK
                    return gson.toJson(updatedProduct.get());
                } else {
                    throw new ProductoNotFoundException("Producto con ID " + id + " no encontrado para actualizar.");
                }
            } catch (IllegalArgumentException e) {
                throw e;
            } catch (Exception e) {
                throw new RuntimeException("Error al actualizar producto: " + e.getMessage(), e);
            }
        });

        // Endpoint para Eliminar un Producto (DELETE /api/productos/:id)
        delete("/api/productos/:id", (request, response) -> {
            String id = request.params(":id");
            if (productoService.eliminarProducto(id)) {
                response.status(204); 
                return ""; 
            } else {
                throw new ProductoNotFoundException("Producto con ID " + id + " no encontrado para eliminar.");
            }
        });
    }

    // Clase interna para manejar respuestas de error JSON
    private static class ErrorResponse {
        private final String message;

        public ErrorResponse(String message) {
            this.message = message;
        }
    }
}
