package com.cafeteria.catalogo.service;

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

    // Inyección por constructor (Buenas prácticas, prescindimos de @Autowired)
    public CatalogoService(ProductoRepository productoRepository, CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    // 1. GET /catalogo/{id} - Detalle de producto activo con sus modificadores
    @Transactional(readOnly = true)
    public Producto obtenerProductoPorId(UUID id) {
        return productoRepository.findByIdAndEstado(id, "ACTIVO")
                .orElseThrow(() -> new RuntimeException("El producto no existe o fue dado de baja."));
    }

    // 2. GET /catalogo/categorias - Listar categorías activas para los filtros del front
    @Transactional(readOnly = true)
    public List<Categoria> listarCategorias() {
        return categoriaRepository.findByEstado("ACTIVO");
    }

    // 3. DELETE /catalogo/{id} - Borrado lógico del producto
    @Transactional
    public void eliminarProducto(UUID id) {
        Producto producto = productoRepository.findByIdAndEstado(id, "ACTIVO")
                .orElseThrow(() -> new RuntimeException("No se puede eliminar: el producto no existe o ya está inactivo."));
        
        producto.setEstado("INACTIVO");
        productoRepository.save(producto);
    }
}