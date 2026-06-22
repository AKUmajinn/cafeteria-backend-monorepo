package com.cafeteria.catalogo.service;

import com.cafeteria.catalogo.dto.CategoriaRequest;
import com.cafeteria.catalogo.dto.ProductoRequest;
import com.cafeteria.catalogo.entity.Categoria;
import com.cafeteria.catalogo.entity.Producto;
import com.cafeteria.catalogo.repository.CategoriaRepository;
import com.cafeteria.catalogo.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class CatalogoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public CatalogoService(ProductoRepository productoRepository, CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional(readOnly = true)
    public Producto obtenerProductoPorId(UUID id) {
        return productoRepository.findByIdAndEstado(id, "ACTIVO")
                .orElseThrow(() -> new RuntimeException("El producto no existe o fue dado de baja."));
    }

    @Transactional(readOnly = true)
    public List<Categoria> listarCategorias() {
        return categoriaRepository.findByEstado("ACTIVO");
    }

    @Transactional(readOnly = true)
    public List<Producto> listarProductosFiltrados(String nombre, UUID categoriaId, Double precioMin, Double precioMax) {
        return productoRepository.filtrarProductos(nombre, categoriaId, precioMin, precioMax);
    }

    @Transactional
    public Producto crearProducto(ProductoRequest request) {
        Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("La categoria especificada no existe."));

        Producto producto = new Producto();
        producto.setCategoria(categoria);
        producto.setNombre(request.getNombre());
        producto.setDescripcion(request.getDescripcion());
        producto.setPrecioBase(request.getPrecioBase());
        producto.setImagenUrl(request.getImagenUrl());
        producto.setEstado("ACTIVO");

        return productoRepository.save(producto);
    }

    @Transactional
    public Producto actualizarProducto(UUID id, ProductoRequest request) {
        Producto producto = productoRepository.findByIdAndEstado(id, "ACTIVO")
                .orElseThrow(() -> new RuntimeException("El producto no existe o está inactivo."));

        Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("La categoria especificada no existe."));

        producto.setCategoria(categoria);
        producto.setNombre(request.getNombre());
        producto.setDescripcion(request.getDescripcion());
        producto.setPrecioBase(request.getPrecioBase());
        producto.setImagenUrl(request.getImagenUrl());

        return productoRepository.save(producto);
    }

    @Transactional
    public Categoria crearCategoria(CategoriaRequest request) {
        Categoria categoria = new Categoria();
        categoria.setNombre(request.getNombre());
        categoria.setDescripcion(request.getDescripcion());
        categoria.setEstado("ACTIVO");

        return categoriaRepository.save(categoria);
    }

    @Transactional
    public Categoria actualizarCategoria(UUID id, CategoriaRequest request) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("La categoria no existe."));

        categoria.setNombre(request.getNombre());
        categoria.setDescripcion(request.getDescripcion());

        return categoriaRepository.save(categoria);
    }

    @Transactional
    public void eliminarProducto(UUID id) {
        Producto producto = productoRepository.findByIdAndEstado(id, "ACTIVO")
                .orElseThrow(() -> new RuntimeException("No se puede eliminar: el producto no existe o ya está inactivo."));
        
        producto.setEstado("INACTIVO");
        productoRepository.save(producto);
    }
}