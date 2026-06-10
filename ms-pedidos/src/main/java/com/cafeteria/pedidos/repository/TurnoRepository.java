package com.cafeteria.pedidos.repository;

import com.cafeteria.pedidos.entity.Turno;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TurnoRepository extends JpaRepository<Turno, Long> {
    Optional<Turno> findByEstado(String estado);
}