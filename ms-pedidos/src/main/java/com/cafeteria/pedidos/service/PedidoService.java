package com.cafeteria.pedidos.service;

import com.cafeteria.pedidos.client.CatalogoClient;
import com.cafeteria.pedidos.dto.PedidoRequest;
import com.cafeteria.pedidos.dto.ProductoResponse;
import com.cafeteria.pedidos.dto.ResumenTurnoResponse;
import com.cafeteria.pedidos.entity.*;
import com.cafeteria.pedidos.exception.TurnoNoActivoException;
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
    private final CatalogoClient catalogoClient;

    @Transactional
    public Pedido crearPedido(PedidoRequest request) {
        Turno turnoActivo = turnoRepository.findByEstado("ACTIVO")
                .orElseThrow(() -> new TurnoNoActivoException("No hay un turno activo. Debe abrir caja primero."));

        Pedido pedido = Pedido.builder()
                .fecha(LocalDateTime.now())
                .cajero(turnoActivo.getCajeroApertura())
                .tipo(request.getTipo())
                .estado("PENDIENTE") // Cambio: ahora nace como PENDIENTE
                .turno(turnoActivo)
                .build();

        List<DetallePedido> detalles = request.getDetalles().stream().map(d -> {
            ProductoResponse productoReal = catalogoClient.obtenerProducto(d.getProductoId());

            if (productoReal.getPrecioBase().doubleValue() != d.getPrecioUnitario().doubleValue()) {
                throw new RuntimeException("Error: El precio de " + d.getNombreProducto() + " no coincide con el catálogo.");
            }

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

        // Ya NO sumamos al turno aquí, porque el pedido aún no está completado.
        
        return pedidoRepository.save(pedido);
    }

    public List<Pedido> listarPedidos() {
        return pedidoRepository.findAll();
    }

    public Pedido obtenerPedido(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
    }

    // NUEVO MÉTODO: Actualizar estado y recalcular métricas del turno
    @Transactional
    public Pedido actualizarEstado(Long id, String nuevoEstado) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        String estadoAnterior = pedido.getEstado();
        if (estadoAnterior.equals(nuevoEstado)) {
            return pedido; // Sin cambios
        }

        Turno turno = pedido.getTurno();

        // 1. Revertir el efecto del estado anterior
        if ("COMPLETADA".equals(estadoAnterior)) {
            turno.setVentasTotales(turno.getVentasTotales().subtract(pedido.getTotal()));
            turno.setOrdenesCompletadas(turno.getOrdenesCompletadas() - 1);
        } else if ("CANCELADA".equals(estadoAnterior)) {
            turno.setOrdenesCanceladas(turno.getOrdenesCanceladas() - 1);
        }

        // 2. Aplicar el efecto del nuevo estado
        if ("COMPLETADA".equals(nuevoEstado)) {
            turno.setVentasTotales(turno.getVentasTotales().add(pedido.getTotal()));
            turno.setOrdenesCompletadas(turno.getOrdenesCompletadas() + 1);
        } else if ("CANCELADA".equals(nuevoEstado)) {
            turno.setOrdenesCanceladas(turno.getOrdenesCanceladas() + 1);
        }

        pedido.setEstado(nuevoEstado);
        turnoRepository.save(turno);
        return pedidoRepository.save(pedido);
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
                .orElseThrow(() -> new TurnoNoActivoException("No hay turno activo para cerrar."));

        turnoActivo.setEstado("CERRADO");
        turnoActivo.setFechaCierre(LocalDateTime.now());
        return turnoRepository.save(turnoActivo);
    }

    public ResumenTurnoResponse obtenerResumenTurnoActivo() {
        Turno turnoActivo = turnoRepository.findByEstado("ACTIVO")
                .orElseThrow(() -> new TurnoNoActivoException("No hay turno activo."));

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