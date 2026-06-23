package com.cafeteria.pedidos.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "turnos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Turno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private LocalDateTime fechaApertura;
    private LocalDateTime fechaCierre;
    private String cajeroApertura;
    
    @Column(name = "estado")
    private String estado; 
    
    private BigDecimal ventasTotales;
    private Integer ordenesCompletadas;
    private Integer ordenesCanceladas;
}