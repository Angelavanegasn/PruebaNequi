package com.vanegas.angela.franquicias_api.infrastruture.adapters.out.mongo;

import com.vanegas.angela.franquicias_api.application.port.out.ProductoRepositoryPort;
import com.vanegas.angela.franquicias_api.domain.model.Producto;
import com.vanegas.angela.franquicias_api.infrastruture.adapters.out.mongo.repository.ProductoMongoRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ProductoMongoAdapter implements ProductoRepositoryPort {

    private final ProductoMongoRepository productoDao;
    private final ProductoMongoMapper mapper;

    public ProductoMongoAdapter(ProductoMongoRepository productoDao, ProductoMongoMapper mapper) {
        this.productoDao = productoDao;
        this.mapper = mapper;
    }

    @Override
    public Flux<Producto> findAll() {
        return productoDao.findAll().map(mapper::toDomain);
    }

    @Override
    public Mono<Producto> findById(String id) {
        return productoDao.findById(id).map(mapper::toDomain);
    }

    @Override
    public Mono<Producto> findByNombre(String nombre) {
        return productoDao.findByNombre(nombre).map(mapper::toDomain);
    }

    @Override
    public Mono<Producto> save(Producto producto) {
        return productoDao.save(mapper.toDocument(producto)).map(mapper::toDomain);
    }

    @Override
    public Mono<Void> delete(Producto producto) {
        return productoDao.delete(mapper.toDocument(producto));
    }
}
