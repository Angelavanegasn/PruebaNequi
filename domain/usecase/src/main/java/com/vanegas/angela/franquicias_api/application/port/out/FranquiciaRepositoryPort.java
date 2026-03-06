package com.vanegas.angela.franquicias_api.application.port.out;

import com.vanegas.angela.franquicias_api.domain.model.Franquicia;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FranquiciaRepositoryPort {

	Flux<Franquicia> findAll();

	Mono<Franquicia> findById(String id);

	Mono<Franquicia> save(Franquicia franquicia);

	Mono<Franquicia> findByNombre(String nombre);
}
