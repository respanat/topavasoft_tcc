package com.topavasoft.gestionproductosmicroservicio.application.dto;

/**
 * DTO para la solicitud de creación de un nuevo producto.
 * Representa los datos que se esperan en el cuerpo de una solicitud POST.
 */
public class ProductoCrearRequest {
    private String tipoPaquete;
    private String tipoCliente;
    private double costoPaquete;
    private String metodoEnvio;

    // Constructor vacío es necesario para la deserialización de JSON (por Gson)
    public ProductoCrearRequest() {
    }

    public ProductoCrearRequest(String tipoPaquete, String tipoCliente, double costoPaquete, String metodoEnvio) {
        this.tipoPaquete = tipoPaquete;
        this.tipoCliente = tipoCliente;
        this.costoPaquete = costoPaquete;
        this.metodoEnvio = metodoEnvio;
    }

    // Getters
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

    // Setters (Necesarios para la deserialización de JSON si no se usa un constructor completo)
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
}
