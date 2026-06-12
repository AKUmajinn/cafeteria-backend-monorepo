package com.cafeteria.catalogo.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.UUID;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "grupo_modificador")
@Getter
@Setter
public class GrupoModificador {
    @Id
    @GeneratedValue
    private UUID id;
    
    @Column(nullable = false)
    private String nombre;
    
    @Column(name = "es_obligatorio", nullable = false)
    private Boolean esObligatorio = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    @JsonBackReference
    private Producto producto;

    @OneToMany(mappedBy = "grupoModificador", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<ItemModificador> items;
}