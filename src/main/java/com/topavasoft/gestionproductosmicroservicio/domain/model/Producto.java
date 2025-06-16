package com.topavasoft.gestionproductosmicroservicio.domain.model;

import java.time.LocalDateTime;
import java.util.Objects; // Para el método Objects.hash y Objects.equals

/**
 * Entidad de dominio que representa un Producto.
 * Esta clase contiene la lógica de negocio y las reglas relacionadas con un producto.
 */
public class Producto {

    private String id;
    private String tipoPaquete;
    private String tipoCliente;
    private double costoPaquete;
    private String metodoEnvio;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;

    // Constructor para crear un nuevo producto (sin ID inicial)
    public Producto(String tipoPaquete, String tipoCliente, double costoPaquete, String metodoEnvio) {
        // Validaciones básicas de dominio al momento de la creación
        if (tipoPaquete == null || tipoPaquete.trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de paquete no puede ser nulo o vacío.");
        }
        if (tipoCliente == null || tipoCliente.trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de cliente no puede ser nulo o vacío.");
        }
        if (costoPaquete <= 0) {
            throw new IllegalArgumentException("El costo del paquete debe ser un valor positivo.");
        }
        if (metodoEnvio == null || metodoEnvio.trim().isEmpty()) {
            throw new IllegalArgumentException("El método de envío no puede ser nulo o vacío.");
        }

        this.tipoPaquete = tipoPaquete;
        this.tipoCliente = tipoCliente;
        this.costoPaquete = costoPaquete;
        this.metodoEnvio = metodoEnvio;
        this.fechaCreacion = LocalDateTime.now(); // Se establece al crear
        this.fechaActualizacion = LocalDateTime.now(); // Se establece al crear
    }

    // Constructor para reconstruir un producto desde la base de datos (con ID)
    public Producto(String id, String tipoPaquete, String tipoCliente, double costoPaquete, String metodoEnvio, LocalDateTime fechaCreacion, LocalDateTime fechaActualizacion) {
        this.id = id;
        this.tipoPaquete = tipoPaquete;
        this.tipoCliente = tipoCliente;
        this.costoPaquete = costoPaquete;
        this.metodoEnvio = metodoEnvio;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
    }

    // --- Getters (Métodos para obtener los valores de los atributos) ---
    public String getId() {
        return id;
    }

    public String getTipoPaquete() {
        return tipoPaquete;
    }

    public String getTipoCliente() {
        return tipoCliente;
    }

    public double getCostoPaquete() {
        return costoPaquete;
    }

    public String getMetodoEnvio() {
        return metodoEnvio;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }


    // --- Setters (Métodos para modificar los valores de los atributos) ---
    // El ID no debe tener un setter público ya que es asignado por la base de datos.
    // Si fuera necesario internamente para la base de datos, podría ser un setter privado o package-private.
    // Por simplicidad, lo mantendremos asignado solo en el constructor para la reconstrucción.

    public void update(String tipoPaquete, String tipoCliente, double costoPaquete, String metodoEnvio) {
        // Validaciones al actualizar
        if (tipoPaquete == null || tipoPaquete.trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de paquete no puede ser nulo o vacío.");
        }
        if (tipoCliente == null || tipoCliente.trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de cliente no puede ser nulo o vacío.");
        }
        if (costoPaquete <= 0) {
            throw new IllegalArgumentException("El costo del paquete debe ser un valor positivo.");
        }
        if (metodoEnvio == null || metodoEnvio.trim().isEmpty()) {
            throw new IllegalArgumentException("El método de envío no puede ser nulo o vacío.");
        }

        this.tipoPaquete = tipoPaquete;
        this.tipoCliente = tipoCliente;
        this.costoPaquete = costoPaquete;
        this.metodoEnvio = metodoEnvio;
        this.fechaActualizacion = LocalDateTime.now(); // Se actualiza la fecha de modificación
    }

    // Sobrescribir equals y hashCode es una buena práctica para entidades
    // Esto permite comparar objetos Producto por su ID, lo que es útil en colecciones
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Producto product = (Producto) o;
        return Objects.equals(id, product.id); // Comparar por ID
    }

    @Override
    public int hashCode() {
        return Objects.hash(id); // Generar hash basado en ID
    }

    @Override
    public String toString() {
        return "Product{" +
               "id='" + id + '\'' +
               ", tipoPaquete='" + tipoPaquete + '\'' +
               ", tipoCliente='" + tipoCliente + '\'' +
               ", costoPaquete=" + costoPaquete +
               ", metodoEnvio='" + metodoEnvio + '\'' +
               ", fechaCreacion=" + fechaCreacion +
               ", fechaActualizacion=" + fechaActualizacion +
               '}';
    }
}
