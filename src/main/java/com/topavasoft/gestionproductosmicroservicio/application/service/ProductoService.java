package com.topavasoft.gestionproductosmicroservicio.application.service;

import com.topavasoft.gestionproductosmicroservicio.application.dto.ProductoActualizarRequest;
import com.topavasoft.gestionproductosmicroservicio.application.dto.ProductoCrearRequest;
import com.topavasoft.gestionproductosmicroservicio.application.dto.ProductoResponse;
import com.topavasoft.gestionproductosmicroservicio.domain.model.Producto;
import com.topavasoft.gestionproductosmicroservicio.domain.port.ProductoRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio de aplicación para la gestión de productos.
 * Implementa la lógica de los casos de uso (CRUD) coordinando la entidad de dominio
 * y el puerto de repositorio.
 * (Principio SOLID: Single Responsibility Principle - este servicio se encarga de la lógica de aplicación
 * de los productos; Dependency Inversion Principle - depende de la abstracción ProductoRepository).
 */
public class ProductoService {

    private final ProductoRepository productoRepository;

    // Constructor para inyección de dependencia del repositorio
    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    /**
     * Crea un nuevo producto.
     * @param request DTO con los datos del producto a crear.
     * @return DTO del producto creado.
     */
    public ProductoResponse crearProducto(ProductoCrearRequest request) {
        // Mapear DTO de request a entidad de dominio
        // Las validaciones de negocio se harán en el constructor de la entidad Producto
        Producto nuevoProducto = new Producto(
            request.getTipoPaquete(),
            request.getTipoCliente(),
            request.getCostoPaquete(),
            request.getMetodoEnvio()
        );

        // Guardar el producto usando el repositorio (que es una abstracción)
        Producto productoGuardado = productoRepository.save(nuevoProducto);

        // Mapear la entidad de dominio guardada a DTO de respuesta
        return new ProductoResponse(productoGuardado);
    }

    public Optional<ProductoResponse> obtenerProductoPorId(String id) {
        Optional<Producto> productoOptional = productoRepository.findById(id);
        // Mapear la entidad de dominio a DTO de respuesta, si existe
        return productoOptional.map(ProductoResponse::new);
    }

    public List<ProductoResponse> obtenerTodosLosProductos() {
        List<Producto> productos = productoRepository.findAll();
        // Mapear la lista de entidades de dominio a lista de DTOs de respuesta
        return productos.stream()
                        .map(ProductoResponse::new)
                        .collect(Collectors.toList());
    }

    public Optional<ProductoResponse> actualizarProducto(String id, ProductoActualizarRequest request) {
        Optional<Producto> productoExistenteOptional = productoRepository.findById(id);

        if (productoExistenteOptional.isEmpty()) {
            return Optional.empty(); // Producto no encontrado
        }

        Producto productoExistente = productoExistenteOptional.get();

        // Actualizar la entidad de dominio. Las validaciones se harán en el método update de Producto.
        productoExistente.update(
            request.getTipoPaquete(),
            request.getTipoCliente(),
            request.getCostoPaquete(),
            request.getMetodoEnvio()
        );

        // Guardar el producto actualizado usando el repositorio
        Producto productoActualizado = productoRepository.update(productoExistente);

        // Mapear la entidad de dominio actualizada a DTO de respuesta
        return Optional.of(new ProductoResponse(productoActualizado));
    }

    public boolean eliminarProducto(String id) {
        Optional<Producto> productoOptional = productoRepository.findById(id);
        if (productoOptional.isPresent()) {
            productoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
