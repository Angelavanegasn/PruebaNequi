package com.vanegas.angela.franquicias_api.infrastruture.adapters.out.mongo;

import com.vanegas.angela.franquicias_api.application.port.out.FranquiciaRepositoryPort;
import com.vanegas.angela.franquicias_api.domain.model.Franquicia;
import com.vanegas.angela.franquicias_api.infrastruture.adapters.out.mongo.repository.FranquiciaMongoRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class FranquiciaMongoAdapter implements FranquiciaRepositoryPort {

    private final FranquiciaMongoRepository franquiciaDao;
    private final FranquiciaMongoMapper mapper;

    public FranquiciaMongoAdapter(FranquiciaMongoRepository franquiciaDao, FranquiciaMongoMapper mapper) {
        this.franquiciaDao = franquiciaDao;
        this.mapper = mapper;
    }

    @Override
    public Flux<Franquicia> findAll() {
        return franquiciaDao.findAll().map(mapper::toDomain);
    }

    @Override
    public Mono<Franquicia> findById(String id) {
        return franquiciaDao.findById(id).map(mapper::toDomain);
    }

    @Override
    public Mono<Franquicia> save(Franquicia franquicia) {
        return franquiciaDao.save(mapper.toDocument(franquicia)).map(mapper::toDomain);
    }

    @Override
    public Mono<Franquicia> findByNombre(String nombre) {
        return franquiciaDao.findByNombre(nombre).map(mapper::toDomain);
    }
}
