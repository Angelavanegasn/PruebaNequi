package com.vanegas.angela.franquicias_api.application.service;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.vanegas.angela.franquicias_api.application.port.in.ProductoUseCase;
import com.vanegas.angela.franquicias_api.application.port.out.ProductoRepositoryPort;
import com.vanegas.angela.franquicias_api.application.port.out.SecuenciaPort;
import com.vanegas.angela.franquicias_api.domain.model.Producto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductoApplicationService implements ProductoUseCase {
    

	private static final Logger log = LoggerFactory.getLogger(ProductoApplicationService.class);

	private final ProductoRepositoryPort productoRepositoryPort;
	private final SecuenciaPort secuenciaPort;

	public ProductoApplicationService(ProductoRepositoryPort productoRepositoryPort, SecuenciaPort secuenciaPort) {
		this.productoRepositoryPort = productoRepositoryPort;
		this.secuenciaPort = secuenciaPort;
	}

	@Override
	public Flux<Producto> listar() {
		return productoRepositoryPort.findAll();
	}

	@Override
	public Mono<Producto> obtenerPorId(String id) {
		return productoRepositoryPort.findById(id)
				.switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado")))
				.doOnNext(producto -> log.info("onNext obtenerProducto id={}", producto.getId()))
				.doOnError(error -> log.error("onError obtenerProducto id={}", id, error));
	}

	@Override
	public Mono<Producto> crear(Producto producto) {
		return asegurarId(producto)
				.map(productoConId -> {
					if (productoConId.getCreateAt() == null) {
						productoConId.setCreateAt(new Date());
					}
					return productoConId;
				})
				.flatMap(productoRepositoryPort::save);
	}

	@Override
	public Mono<Producto> actualizar(String id, Producto producto) {
		return obtenerPorId(id)
				.zipWith(Mono.just(producto), (db, req) -> {
					db.setNombre(req.getNombre());
					db.setSucursal(req.getSucursal());
					db.setCantidadSlock(req.getCantidadSlock());
					return db;
				})
				.flatMap(productoRepositoryPort::save);
	}

	@Override
	public Mono<Void> eliminar(String id) {
		return obtenerPorId(id).flatMap(productoRepositoryPort::delete);
	}

	@Override
	public Mono<Producto> obtenerPorNombre(String nombre) {
		return productoRepositoryPort.findByNombre(nombre);
	}

	private Mono<Producto> asegurarId(Producto producto) {
		if (producto.getId() != null && !producto.getId().trim().isEmpty()) {
			return Mono.just(producto);
		}
		return secuenciaPort.siguienteValor("producto")
				.map(id -> {
					producto.setId(id);
					return producto;
				});
	}
}

