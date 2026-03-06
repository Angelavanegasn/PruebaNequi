package com.vanegas.angela.franquicias_api.application.port.in;


import com.vanegas.angela.franquicias_api.application.dto.ProductoConSucursal;
import com.vanegas.angela.franquicias_api.domain.model.Franquicia;
import com.vanegas.angela.franquicias_api.domain.model.Producto;
import com.vanegas.angela.franquicias_api.domain.model.Sucursal;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FranquiciaUseCase {
    

	Flux<Franquicia> listar();

	Mono<Franquicia> obtenerPorId(String id);

	Mono<Franquicia> obtenerPorNombre(String nombre);

	Mono<Franquicia> crear(Franquicia franquicia);

	Mono<Franquicia> agregarSucursal(String franquiciaId, Sucursal sucursal);

	Mono<Franquicia> agregarProducto(String franquiciaId, String sucursalId, Producto producto);

	Mono<Franquicia> eliminarProducto(String franquiciaId, String sucursalId, String productoId);

	Mono<Franquicia> actualizarStockProducto(String franquiciaId, String sucursalId, String productoId, int stock);

	Mono<Franquicia> actualizarNombreFranquicia(String franquiciaId, String nombre);

	Mono<Franquicia> actualizarNombreSucursal(String franquiciaId, String sucursalId, String nombre);

	Mono<Franquicia> actualizarNombreProducto(String franquiciaId, String sucursalId, String productoId, String nombre);

	Flux<ProductoConSucursal> obtenerProductoConMasStockPorSucursal(String franquiciaId);
}
