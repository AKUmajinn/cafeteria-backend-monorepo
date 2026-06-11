package com.cafeteria.catalogo.repository;

import com.cafeteria.catalogo.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, UUID> {
    
    // Método para cumplir con tu requerimiento: GET /catalogo/categorias
    // Nos permite traer solo las que tengan estado "ACTIVO"
    List<Categoria> findByEstado(String estado);
}