package com.vanegas.angela.franquicias_api.infrastruture.adapters.out.mongo.service;

import reactor.core.publisher.Mono;

public interface SecuenciaService {

    Mono<String> siguienteValor(String secuenciaId);
}
