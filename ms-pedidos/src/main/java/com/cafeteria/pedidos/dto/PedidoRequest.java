package com.cafeteria.pedidos.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class PedidoRequest {
    private String cajero;
    private String tipo;
    private List<DetalleRequest> detalles;
}