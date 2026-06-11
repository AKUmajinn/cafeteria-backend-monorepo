package com.cafeteria.catalogo.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.UUID;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "producto")
@Getter
@Setter
public class Producto {
    @Id
    @GeneratedValue
    private UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;
    
    @Column(nullable = false)
    private String nombre;
    
    private String descripcion;
    
    @Column(name = "precio_base", nullable = false)
    private Double precioBase;
    
    @Column(name = "imagen_url")
    private String imagenUrl;
    
    @Column(nullable = false)
    private String estado = "ACTIVO";

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<GrupoModificador> gruposModificadores;
}