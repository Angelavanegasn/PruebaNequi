package com.vanegas.angela.franquicias_api.infrastruture.adapters.out.mongo.repository;

import com.vanegas.angela.franquicias_api.infrastruture.adapters.out.mongo.document.ProductoDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface ProductoMongoRepository extends ReactiveMongoRepository<ProductoDocument, String> {

    Mono<ProductoDocument> findByNombre(String nombre);
}
