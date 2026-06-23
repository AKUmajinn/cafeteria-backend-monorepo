package com.cafeteria.gateway;

import com.cafeteria.gateway.config.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates.path;
import static org.springframework.cloud.gateway.server.mvc.filter.LoadBalancerFilterFunctions.lb;

@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {

    @Autowired
    private JwtFilter jwtFilter; 

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }


    @Bean
    public RouterFunction<ServerResponse> authRoute() {
        return route("ms-auth")
                .route(path("/auth/**"), http())
                .filter(lb("MS-AUTH"))
                .build();
    }


    @Bean
    public RouterFunction<ServerResponse> catalogoRoute() {
        return route("ms-catalogo")
                .route(path("/api/catalogo/**"), http())
                .filter(lb("MS-CATALOGO"))
                .filter(jwtFilter)
                .build();
    }


    @Bean
    public RouterFunction<ServerResponse> pedidosRoute() {
        return route("ms-pedidos")
                .route(path("/api/pedidos/**"), http())
                .filter(lb("MS-PEDIDOS"))
                .filter(jwtFilter)
                .build();
    }
}