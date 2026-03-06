package com.vanegas.angela.franquicias_api.infrastruture.adapters.out.mongo.repository;

import com.vanegas.angela.franquicias_api.infrastruture.adapters.out.mongo.document.FranquiciaDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface FranquiciaMongoRepository extends ReactiveMongoRepository<FranquiciaDocument, String> {

    Mono<FranquiciaDocument> findByNombre(String nombre);
}
