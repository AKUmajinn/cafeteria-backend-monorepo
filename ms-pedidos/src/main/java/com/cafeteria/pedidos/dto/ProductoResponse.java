package com.cafeteria.pedidos.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.UUID;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductoResponse {
    private UUID id;
    private String nombre;
    private Double precioBase;
}