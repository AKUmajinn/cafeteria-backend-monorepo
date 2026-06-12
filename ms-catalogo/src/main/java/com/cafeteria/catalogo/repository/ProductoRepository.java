package com.cafeteria.catalogo.repository;

import com.cafeteria.catalogo.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, UUID> {
    
    // Método útil para tu requerimiento: GET /catalogo/{id}
    // Evita que se pueda consultar un producto que ya fue eliminado lógicamente
    Optional<Producto> findByIdAndEstado(UUID id, String estado);
}