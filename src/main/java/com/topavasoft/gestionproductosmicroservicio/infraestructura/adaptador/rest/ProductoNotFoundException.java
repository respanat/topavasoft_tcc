package com.topavasoft.gestionproductosmicroservicio.infraestructura.adaptador.rest;

/**
 * Excepción personalizada para indicar que un producto no fue encontrado.
 */
public class ProductoNotFoundException extends RuntimeException {
    public ProductoNotFoundException(String message) {
        super(message);
    }
}
