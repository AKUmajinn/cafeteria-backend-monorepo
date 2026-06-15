package com.cafeteria.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

/**
 * Configuración CORS centralizada en el API Gateway.
 *
 * Al estar aquí, aplica a TODOS los microservicios que pasan por el gateway
 * (ms-catalogo, ms-pedidos y los que se agreguen después). Los microservicios
 * NO deben configurar CORS por su cuenta: si lo hicieran, el navegador
 * recibiría cabeceras Access-Control-Allow-Origin duplicadas y bloquearía
 * la respuesta igual.
 *
 * Usamos un CorsFilter (filtro servlet estándar) en lugar de propiedades
 * porque este gateway usa la variante MVC (spring-cloud-starter-gateway-server-webmvc),
 * cuyo soporte de CORS vía properties varía según la versión. Un filtro
 * funciona siempre, incluso con las rutas funcionales (RouterFunction)
 * definidas en ApiGatewayApplication.
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // Orígenes permitidos: el dev server de Angular.
        // En producción se reemplaza/añade el dominio real del front.
        config.setAllowedOrigins(List.of("http://localhost:4200"));

        // Métodos que usa el front. OPTIONS es obligatorio: es el "preflight"
        // que el navegador envía antes de cualquier POST/PUT/DELETE.
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Cabeceras que el front puede enviar (Content-Type, Authorization, etc.)
        config.setAllowedHeaders(List.of("*"));

        // true si en el futuro se usan cookies o tokens con credenciales.
        // OJO: con allowCredentials=true NO se puede usar "*" en allowedOrigins.
        config.setAllowCredentials(true);

        // Cuántos segundos puede el navegador cachear la respuesta del preflight
        // (evita un OPTIONS extra por cada petición).
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // aplica a todas las rutas
        return new CorsFilter(source);
    }
}
