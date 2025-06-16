package com.topavasoft.gestionproductosmicroservicio.domain.port;

import com.topavasoft.gestionproductosmicroservicio.domain.model.Producto; // Importamos la entidad Product
import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida (Output Port) que define las operaciones de persistencia
 * para la entidad Product.
 * Esta interfaz es parte de la capa de Dominio y es independiente de la implementación de la base de datos.
 * (Principio SOLID: Interface Segregation Principle - aunque aquí es una única interfaz,
 * y Dependency Inversion Principle - el dominio depende de una abstracción (esta interfaz),
 * no de una implementación concreta de infraestructura).
 */
public interface ProductoRepository {

    Producto save(Producto producto);
    Optional<Producto> findById(String id);
    List<Producto> findAll();
    Producto update(Producto producto);
    void deleteById(String id);
}
