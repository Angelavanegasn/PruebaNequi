package com.vanegas.angela.franquicias_api.infrastruture.config;

import com.vanegas.angela.franquicias_api.infrastruture.adapters.in.web.FranquiciaController;
import com.vanegas.angela.franquicias_api.infrastruture.adapters.in.web.ProductoController;
import com.vanegas.angela.franquicias_api.infrastruture.adapters.in.web.SucursaController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterConfig {

    @Bean
    public RouterFunction<ServerResponse> rutasProducto(ProductoController productoController) {
        return route(GET("/api/producto"), productoController::listar)
                .andRoute(GET("/api/producto/{id}"), productoController::verProducto)
                .andRoute(PUT("/api/producto/{id}"), productoController::editarProducto)
                .andRoute(DELETE("/api/producto/{id}"), productoController::eliminarProducto)
                .andRoute(POST("/api/producto"), productoController::guardarProducto);
    }

    @Bean
    public RouterFunction<ServerResponse> rutasSucursales(SucursaController sucursaController) {
        return route(POST("/api/sucursal"), sucursaController::guardarSucursal)
                .andRoute(GET("/api/sucursal/{id}"), sucursaController::verSucursal)
                .andRoute(GET("/api/sucursal"), sucursaController::listarSucursal);
    }

    @Bean
    public RouterFunction<ServerResponse> rutasFranquicias(FranquiciaController franquiciaController) {
        return route(POST("/api/franquicia"), franquiciaController::guardarfranquicia)
                .andRoute(GET("/api/franquicia"), franquiciaController::listarfranquicia)
                .andRoute(GET("/api/franquicia/{id}"), franquiciaController::verFranquicia)
                .andRoute(POST("/api/franquicia/{id}/sucursal"), franquiciaController::agregarSucursal)
                .andRoute(PUT("/api/franquicia/{id}/nombre"), franquiciaController::modificarNombreFranquicia)
                .andRoute(PUT("/api/franquicia/{franquiciaId}/sucursal/{sucursalId}/nombre"), franquiciaController::modificarNombreSucursal)
                .andRoute(POST("/api/franquicia/{franquiciaId}/sucursal/{sucursalId}/producto"), franquiciaController::agregarProducto)
                .andRoute(PUT("/api/franquicia/{franquiciaId}/sucursal/{sucursalId}/producto/{productoId}/nombre"), franquiciaController::modificarNombreProducto)
                .andRoute(DELETE("/api/franquicia/{franquiciaId}/sucursal/{sucursalId}/producto/{productoId}"), franquiciaController::eliminarProductoSucursal)
                .andRoute(PUT("/api/franquicia/{franquiciaId}/sucursal/{sucursalId}/producto/{productoId}/stock"), franquiciaController::modificarStockProducto)
                .andRoute(GET("/api/franquicia/{id}/productos-mayor-stock"), franquiciaController::listarProductoMayorStock);
    }
}
