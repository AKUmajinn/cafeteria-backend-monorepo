package com.cafeteria.pedidos.service;

import com.cafeteria.pedidos.dto.PedidoRequest;
import com.cafeteria.pedidos.dto.ResumenTurnoResponse;
import com.cafeteria.pedidos.entity.*;
import com.cafeteria.pedidos.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final TurnoRepository turnoRepository;

    @Transactional
    public Pedido crearPedido(PedidoRequest request) {
        Turno turnoActivo = turnoRepository.findByEstado("ACTIVO")
                .orElseThrow(() -> new RuntimeException("No hay un turno activo. Debe abrir caja primero."));

        Pedido pedido = Pedido.builder()
                .fecha(LocalDateTime.now())
                .cajero(request.getCajero())
                .tipo(request.getTipo())
                .estado("COMPLETADA")
                .turno(turnoActivo)
                .build();

        List<DetallePedido> detalles = request.getDetalles().stream().map(d -> {
            BigDecimal subtotal = d.getPrecioUnitario().multiply(new BigDecimal(d.getCantidad()));
            return DetallePedido.builder()
                    .productoId(d.getProductoId())
                    .nombreProducto(d.getNombreProducto())
                    .cantidad(d.getCantidad())
                    .precioUnitario(d.getPrecioUnitario())
                    .subtotal(subtotal)
                    .pedido(pedido)
                    .build();
        }).collect(Collectors.toList());

        pedido.setDetalles(detalles);
        
        BigDecimal total = detalles.stream()
                .map(DetallePedido::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        pedido.setTotal(total);


        turnoActivo.setVentasTotales(turnoActivo.getVentasTotales().add(total));
        turnoActivo.setOrdenesCompletadas(turnoActivo.getOrdenesCompletadas() + 1);
        turnoRepository.save(turnoActivo);

        return pedidoRepository.save(pedido);
    }

    public List<Pedido> listarPedidos() {
        return pedidoRepository.findAll();
    }

    public Pedido obtenerPedido(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
    }


    @Transactional
    public Turno abrirTurno(String cajero) {
        if (turnoRepository.findByEstado("ACTIVO").isPresent()) {
            throw new RuntimeException("Ya existe un turno activo.");
        }
        Turno nuevoTurno = Turno.builder()
                .fechaApertura(LocalDateTime.now())
                .cajeroApertura(cajero)
                .estado("ACTIVO")
                .ventasTotales(BigDecimal.ZERO)
                .ordenesCompletadas(0)
                .ordenesCanceladas(0)
                .build();
        return turnoRepository.save(nuevoTurno);
    }

    @Transactional
    public Turno cerrarTurno() {
        Turno turnoActivo = turnoRepository.findByEstado("ACTIVO")
                .orElseThrow(() -> new RuntimeException("No hay turno activo para cerrar."));
        
        turnoActivo.setEstado("CERRADO");
        turnoActivo.setFechaCierre(LocalDateTime.now());
        return turnoRepository.save(turnoActivo);
    }

    public ResumenTurnoResponse obtenerResumenTurnoActivo() {
        Turno turnoActivo = turnoRepository.findByEstado("ACTIVO")
                .orElseThrow(() -> new RuntimeException("No hay turno activo."));
        
        ResumenTurnoResponse response = new ResumenTurnoResponse();
        response.setTurnoId(turnoActivo.getId());
        response.setEstado(turnoActivo.getEstado());
        response.setCajero(turnoActivo.getCajeroApertura());
        response.setVentasHoy(turnoActivo.getVentasTotales());
        response.setCompletadas(turnoActivo.getOrdenesCompletadas());
        response.setCanceladas(turnoActivo.getOrdenesCanceladas());
        return response;
    }
}