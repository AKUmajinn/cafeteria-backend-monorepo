package com.cafeteria.catalogo.dto;

import java.util.UUID;
import lombok.Data;

@Data
public class ProductoRequest {
    private UUID categoriaId;
    private String nombre;
    private String descripcion;
    private Double precioBase;
    private String imagenUrl;
}