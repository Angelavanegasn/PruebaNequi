package com.vanegas.angela.franquicias_api.application.port.out;

import reactor.core.publisher.Mono;

public interface SecuenciaPort {
    
	Mono<String> siguienteValor(String secuenciaId);
}

