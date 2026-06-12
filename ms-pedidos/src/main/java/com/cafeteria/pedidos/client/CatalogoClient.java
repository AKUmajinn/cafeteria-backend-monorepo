package com.cafeteria.pedidos.client;

import com.cafeteria.pedidos.dto.ProductoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.UUID;

@FeignClient(name = "ms-catalogo", url = "http://localhost:8081")
public interface CatalogoClient {

    @GetMapping("/api/catalogo/{id}")
    ProductoResponse obtenerProducto(@PathVariable("id") UUID id);
}