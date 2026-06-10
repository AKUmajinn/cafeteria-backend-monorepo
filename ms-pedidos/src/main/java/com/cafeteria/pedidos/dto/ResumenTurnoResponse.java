package com.cafeteria.pedidos.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ResumenTurnoResponse {
    private Long turnoId;
    private String estado;
    private String cajero;
    private BigDecimal ventasHoy;
    private Integer completadas;
    private Integer canceladas;
}