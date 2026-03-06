package com.vanegas.angela.franquicias_api.application.port.out;

import com.vanegas.angela.franquicias_api.domain.model.Producto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductoRepositoryPort {
    
	Flux<Producto> findAll();

	Mono<Producto> findById(String id);

	Mono<Producto> findByNombre(String nombre);

	Mono<Producto> save(Producto producto);

	Mono<Void> delete(Producto producto);
}

