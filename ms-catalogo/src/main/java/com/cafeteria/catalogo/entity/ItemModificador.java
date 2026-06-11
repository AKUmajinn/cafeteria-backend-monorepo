package com.cafeteria.catalogo.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "item_modificador")
@Getter
@Setter
public class ItemModificador {
    @Id
    @GeneratedValue
    private UUID id;
    
    @Column(nullable = false)
    private String nombre;
    
    @Column(name = "precio_adicional", nullable = false)
    private Double precioAdicional = 0.0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grupo_modificador_id", nullable = false)
    @JsonBackReference
    private GrupoModificador grupoModificador;
}