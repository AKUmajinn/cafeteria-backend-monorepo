package com.cafeteria.catalogo.repository;

import com.cafeteria.catalogo.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, UUID> {
    
    Optional<Producto> findByIdAndEstado(UUID id, String estado);
    
    @Query("SELECT p FROM Producto p WHERE p.estado = 'ACTIVO' " +
           "AND (:nombre IS NULL OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', CAST(:nombre AS String), '%'))) " +
           "AND (:categoriaId IS NULL OR p.categoria.id = :categoriaId) " +
           "AND (:precioMin IS NULL OR p.precioBase >= :precioMin) " +
           "AND (:precioMax IS NULL OR p.precioBase <= :precioMax)")
    List<Producto> filtrarProductos(
        @Param("nombre") String nombre,
        @Param("categoriaId") UUID categoriaId,
        @Param("precioMin") Double precioMin,
        @Param("precioMax") Double precioMax
    );
}