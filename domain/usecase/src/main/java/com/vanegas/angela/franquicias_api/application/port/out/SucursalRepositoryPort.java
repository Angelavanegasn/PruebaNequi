package com.vanegas.angela.franquicias_api.application.port.out;

import com.vanegas.angela.franquicias_api.domain.model.Sucursal;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SucursalRepositoryPort {
    
	Flux<Sucursal> findAll();

	Mono<Sucursal> findById(String id);

	Mono<Sucursal> findByNombre(String nombre);

	Mono<Sucursal> save(Sucursal sucursal);
}

