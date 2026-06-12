package com.cafeteria.pedidos.dto;
import lombok.Data;
import java.util.UUID;

@Data
public class ProductoResponse {
    private UUID id;
    private String nombre;
    private Double precioBase;
}