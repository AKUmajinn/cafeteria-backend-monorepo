package com.cafeteria.pedidos.controller;

import com.cafeteria.pedidos.dto.PedidoRequest;
import com.cafeteria.pedidos.dto.ResumenTurnoResponse;
import com.cafeteria.pedidos.entity.Pedido;
import com.cafeteria.pedidos.entity.Turno;
import com.cafeteria.pedidos.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<Pedido> crearPedido(@RequestBody PedidoRequest request) {
        return new ResponseEntity<>(pedidoService.crearPedido(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Pedido>> listarPedidos() {
        return ResponseEntity.ok(pedidoService.listarPedidos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> obtenerPedido(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.obtenerPedido(id));
    }

    // NUEVO ENDPOINT: Actualizar estado
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Pedido> actualizarEstado(@PathVariable Long id, @RequestParam String estado) {
        return ResponseEntity.ok(pedidoService.actualizarEstado(id, estado.toUpperCase()));
    }

    @GetMapping("/turno/activo")
    public ResponseEntity<ResumenTurnoResponse> obtenerResumenTurno() {
        return ResponseEntity.ok(pedidoService.obtenerResumenTurnoActivo());
    }

    @PostMapping("/turno/apertura")
    public ResponseEntity<Turno> iniciarTurno(@RequestParam String cajero) {
        return new ResponseEntity<>(pedidoService.abrirTurno(cajero), HttpStatus.CREATED);
    }

    @PostMapping("/turno/cierre")
    public ResponseEntity<Turno> cerrarTurno() {
        return ResponseEntity.ok(pedidoService.cerrarTurno());
    }
}