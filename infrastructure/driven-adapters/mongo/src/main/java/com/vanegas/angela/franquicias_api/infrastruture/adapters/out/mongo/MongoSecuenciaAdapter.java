package com.vanegas.angela.franquicias_api.infrastruture.adapters.out.mongo;

import com.vanegas.angela.franquicias_api.application.port.out.SecuenciaPort;
import com.vanegas.angela.franquicias_api.infrastruture.adapters.out.mongo.service.SecuenciaService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class MongoSecuenciaAdapter implements SecuenciaPort {

    private final SecuenciaService secuenciaService;

    public MongoSecuenciaAdapter(SecuenciaService secuenciaService) {
        this.secuenciaService = secuenciaService;
    }

    @Override
    public Mono<String> siguienteValor(String secuenciaId) {
        return secuenciaService.siguienteValor(secuenciaId);
    }
}
