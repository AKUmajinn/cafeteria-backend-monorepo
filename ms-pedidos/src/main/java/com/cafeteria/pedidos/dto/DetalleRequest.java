package com.cafeteria.pedidos.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class DetalleRequest {
    private Long productoId;
    private String nombreProducto;
    private Integer cantidad;
    private BigDecimal precioUnitario;
}