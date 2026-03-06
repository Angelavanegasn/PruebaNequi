package com.vanegas.angela.franquicias_api.infrastruture.adapters.out.mongo.repository;

import com.vanegas.angela.franquicias_api.infrastruture.adapters.out.mongo.document.SucursalDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface SucursalMongoRepository extends ReactiveMongoRepository<SucursalDocument, String> {

    Mono<SucursalDocument> findByNombre(String nombre);
}
