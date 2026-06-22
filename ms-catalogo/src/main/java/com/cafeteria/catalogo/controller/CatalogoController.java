package com.cafeteria.catalogo.controller;

import com.cafeteria.catalogo.dto.CategoriaRequest;
import com.cafeteria.catalogo.dto.ProductoRequest;
import com.cafeteria.catalogo.entity.Categoria;
import com.cafeteria.catalogo.entity.Producto;
import com.cafeteria.catalogo.service.CatalogoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/catalogo")
public class CatalogoController {

    private final CatalogoService catalogoService;

    public CatalogoController(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> getProductoPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(catalogoService.obtenerProductoPorId(id));
    }

    @GetMapping("/categorias")
    public ResponseEntity<List<Categoria>> getCategorias() {
        return ResponseEntity.ok(catalogoService.listarCategorias());
    }

    @GetMapping("/productos")
    public ResponseEntity<List<Producto>> getProductos(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) UUID categoriaId,
            @RequestParam(required = false) Double precioMin,
            @RequestParam(required = false) Double precioMax) {
        return ResponseEntity.ok(catalogoService.listarProductosFiltrados(nombre, categoriaId, precioMin, precioMax));
    }

    @PostMapping("/productos")
    public ResponseEntity<Producto> crearProducto(@RequestBody ProductoRequest request) {
        return new ResponseEntity<>(catalogoService.crearProducto(request), HttpStatus.CREATED);
    }

    @PutMapping("/productos/{id}")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable UUID id, @RequestBody ProductoRequest request) {
        return ResponseEntity.ok(catalogoService.actualizarProducto(id, request));
    }

    @PostMapping("/categorias")
    public ResponseEntity<Categoria> crearCategoria(@RequestBody CategoriaRequest request) {
        return new ResponseEntity<>(catalogoService.crearCategoria(request), HttpStatus.CREATED);
    }

    @PutMapping("/categorias/{id}")
    public ResponseEntity<Categoria> actualizarCategoria(@PathVariable UUID id, @RequestBody CategoriaRequest request) {
        return ResponseEntity.ok(catalogoService.actualizarCategoria(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable UUID id) {
        catalogoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }
}