package com.cafeteria.auth.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity 
@Data 
@Table(name="usuarios")
public class Usuario {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String username;
    
    @Column(nullable = false, length = 255)
    private String password;
    
    private String rol;
    
    private String telefono;
    
    private String email;
}