package com.vanegas.angela.franquicias_api.application.port.in;

import com.vanegas.angela.franquicias_api.domain.model.Sucursal;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SucursalUseCase {
    

	Flux<Sucursal> listar();

	Mono<Sucursal> obtenerPorId(String id);

	Mono<Sucursal> crear(Sucursal sucursal);

	Mono<Sucursal> obtenerPorNombre(String nombre);
}
