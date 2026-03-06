package com.vanegas.angela.franquicias_api.infrastruture.adapters.out.mongo;

import com.vanegas.angela.franquicias_api.application.port.out.SucursalRepositoryPort;
import com.vanegas.angela.franquicias_api.domain.model.Sucursal;
import com.vanegas.angela.franquicias_api.infrastruture.adapters.out.mongo.repository.SucursalMongoRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class SucursalMongoAdapter implements SucursalRepositoryPort {

    private final SucursalMongoRepository sucursalDao;
    private final SucursalMongoMapper mapper;

    public SucursalMongoAdapter(SucursalMongoRepository sucursalDao, SucursalMongoMapper mapper) {
        this.sucursalDao = sucursalDao;
        this.mapper = mapper;
    }

    @Override
    public Flux<Sucursal> findAll() {
        return sucursalDao.findAll().map(mapper::toDomain);
    }

    @Override
    public Mono<Sucursal> findById(String id) {
        return sucursalDao.findById(id).map(mapper::toDomain);
    }

    @Override
    public Mono<Sucursal> findByNombre(String nombre) {
        return sucursalDao.findByNombre(nombre).map(mapper::toDomain);
    }

    @Override
    public Mono<Sucursal> save(Sucursal sucursal) {
        return sucursalDao.save(mapper.toDocument(sucursal)).map(mapper::toDomain);
    }
}
