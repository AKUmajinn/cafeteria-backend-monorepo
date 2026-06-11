package com.cafeteria.catalogo.entity;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "categoria")
@Getter
@Setter
public class Categoria {
    @Id
    @GeneratedValue
    private UUID id;
    
    @Column(nullable = false, unique = true)
    private String nombre;
    
    private String descripcion;
    
    @Column(nullable = false)
    private String estado = "ACTIVO";
}