package com.cafeteria.catalogo.controller;

import com.cafeteria.catalogo.entity.Categoria;
import com.cafeteria.catalogo.entity.Producto;
import com.cafeteria.catalogo.service.CatalogoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/catalogo") // <-- Agrega "/api" aquí
public class CatalogoController {

    private final CatalogoService catalogoService;

    public CatalogoController(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    // 1. GET /api/catalogo/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Producto> getProductoPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(catalogoService.obtenerProductoPorId(id));
    }

    // 2. GET /api/catalogo/categorias
    @GetMapping("/categorias")
    public ResponseEntity<List<Categoria>> getCategorias() {
        return ResponseEntity.ok(catalogoService.listarCategorias());
    }

    // 3. DELETE /api/catalogo/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable UUID id) {
        catalogoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }
}