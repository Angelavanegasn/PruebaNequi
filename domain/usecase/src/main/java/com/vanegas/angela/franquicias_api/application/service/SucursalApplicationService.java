package com.vanegas.angela.franquicias_api.application.service;

import java.util.ArrayList;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.vanegas.angela.franquicias_api.application.port.in.SucursalUseCase;
import com.vanegas.angela.franquicias_api.application.port.out.SecuenciaPort;
import com.vanegas.angela.franquicias_api.application.port.out.SucursalRepositoryPort;
import com.vanegas.angela.franquicias_api.domain.model.Sucursal;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
public class SucursalApplicationService implements SucursalUseCase {
    
private static final Logger log = LoggerFactory.getLogger(SucursalApplicationService.class);

	private final SucursalRepositoryPort sucursalRepositoryPort;
	private final SecuenciaPort secuenciaPort;

	public SucursalApplicationService(SucursalRepositoryPort sucursalRepositoryPort, SecuenciaPort secuenciaPort) {
		this.sucursalRepositoryPort = sucursalRepositoryPort;
		this.secuenciaPort = secuenciaPort;
	}

	@Override
	public Flux<Sucursal> listar() {
		return sucursalRepositoryPort.findAll();
	}

	@Override
	public Mono<Sucursal> obtenerPorId(String id) {
		return sucursalRepositoryPort.findById(id)
				.switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Sucursal no encontrada")))
				.doOnNext(sucursal -> log.info("onNext obtenerSucursal id={}", sucursal.getId()))
				.doOnError(error -> log.error("onError obtenerSucursal id={}", id, error));
	}

	@Override
	public Mono<Sucursal> crear(Sucursal sucursal) {
		return asegurarId(sucursal)
				.map(sucursalConId -> {
					if (sucursalConId.getCreateAt() == null) {
						sucursalConId.setCreateAt(new Date());
					}
					if (sucursalConId.getProductos() == null) {
						sucursalConId.setProductos(new ArrayList<>());
					}
					return sucursalConId;
				})
				.flatMap(sucursalRepositoryPort::save);
	}

	@Override
	public Mono<Sucursal> obtenerPorNombre(String nombre) {
		return sucursalRepositoryPort.findByNombre(nombre);
	}

	private Mono<Sucursal> asegurarId(Sucursal sucursal) {
		if (sucursal.getId() != null && !sucursal.getId().trim().isEmpty()) {
			return Mono.just(sucursal);
		}
		return secuenciaPort.siguienteValor("sucursal")
				.map(id -> {
					sucursal.setId(id);
					return sucursal;
				});
	}
}
