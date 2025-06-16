package com.topavasoft.gestionproductosmicroservicio.application.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.topavasoft.gestionproductosmicroservicio.domain.model.Producto; 

/**
 * DTO para la respuesta de un producto.
 * Representa los datos de un producto que se enviarán como respuesta JSON.
 */
public class ProductoResponse {
    private String id;
    private String tipoPaquete;
    private String tipoCliente;
    private double costoPaquete;
    private String metodoEnvio;
    private String fechaCreacion; // Como String para el formato JSON
    private String fechaActualizacion; // Como String para el formato JSON

    // Constructor vacío es necesario para la serialización de JSON (por Gson)
    public ProductoResponse() {
    }

    // Constructor que toma una entidad de dominio Producto y la mapea a este DTO
    public ProductoResponse(Producto producto) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME; 
        this.id = producto.getId();
        this.tipoPaquete = producto.getTipoPaquete();
        this.tipoCliente = producto.getTipoCliente();
        this.costoPaquete = producto.getCostoPaquete();
        this.metodoEnvio = producto.getMetodoEnvio();
        this.fechaCreacion = producto.getFechaCreacion() != null ? producto.getFechaCreacion().format(formatter) : null;
        this.fechaActualizacion = producto.getFechaActualizacion() != null ? producto.getFechaActualizacion().format(formatter) : null;
    }

    // Getters (para la serialización de JSON)
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

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public String getFechaActualizacion() {
        return fechaActualizacion;
    }

    // Setters 
    public void setId(String id) {
        this.id = id;
    }

    public void setTipoPaquete(String tipoPaquete) {
        this.tipoPaquete = tipoPaquete;
    }

    public void setTipoCliente(String tipoCliente) {
        this.tipoCliente = tipoCliente;
    }

    public void setCostoPaquete(double costoPaquete) {
        this.costoPaquete = costoPaquete;
    }

    public void setMetodoEnvio(String metodoEnvio) {
        this.metodoEnvio = metodoEnvio;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public void setFechaActualizacion(String fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
}
