package com.vanegas.angela.franquicias_api.application.port.in;

import com.vanegas.angela.franquicias_api.domain.model.Producto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductoUseCase {
    
	Flux<Producto> listar();

	Mono<Producto> obtenerPorId(String id);

	Mono<Producto> crear(Producto producto);

	Mono<Producto> actualizar(String id, Producto producto);

	Mono<Void> eliminar(String id);

	Mono<Producto> obtenerPorNombre(String nombre);
}

