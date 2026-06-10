package com.cafeteria.pedidos.repository;

import com.cafeteria.pedidos.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByTurnoId(Long turnoId);
}