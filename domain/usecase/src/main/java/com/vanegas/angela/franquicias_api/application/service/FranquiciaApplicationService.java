package com.vanegas.angela.franquicias_api.application.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.vanegas.angela.franquicias_api.application.dto.ProductoConSucursal;
import com.vanegas.angela.franquicias_api.application.port.in.FranquiciaUseCase;
import com.vanegas.angela.franquicias_api.application.port.out.FranquiciaRepositoryPort;
import com.vanegas.angela.franquicias_api.application.port.out.SecuenciaPort;
import com.vanegas.angela.franquicias_api.domain.model.Franquicia;
import com.vanegas.angela.franquicias_api.domain.model.Producto;
import com.vanegas.angela.franquicias_api.domain.model.Sucursal;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class FranquiciaApplicationService implements FranquiciaUseCase {
    

	private static final Logger log = LoggerFactory.getLogger(FranquiciaApplicationService.class);

	private final FranquiciaRepositoryPort franquiciaRepositoryPort;
	private final SecuenciaPort secuenciaPort;

	public FranquiciaApplicationService(FranquiciaRepositoryPort franquiciaRepositoryPort, SecuenciaPort secuenciaPort) {
		this.franquiciaRepositoryPort = franquiciaRepositoryPort;
		this.secuenciaPort = secuenciaPort;
	}

	@Override
	public Flux<Franquicia> listar() {
		return franquiciaRepositoryPort.findAll()
				.doOnNext(franquicia -> log.info("onNext listar franquiciaId={} nombre={}", franquicia.getId(), franquicia.getNombre()))
				.doOnError(error -> log.error("onError listar franquicias", error))
				.doOnComplete(() -> log.info("onComplete listar franquicias"));
	}

	@Override
	public Mono<Franquicia> obtenerPorId(String id) {
		return franquiciaRepositoryPort.findById(id)
				.doOnNext(franquicia -> log.info("onNext obtenerPorId franquiciaId={}", franquicia.getId()))
				.switchIfEmpty(Mono.defer(() -> {
					log.warn("switchIfEmpty obtenerPorId franquiciaId={}", id);
					return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Franquicia no encontrada"));
				}))
				.doOnError(error -> log.error("onError obtenerPorId franquiciaId={}", id, error))
				.doOnSuccess(franquicia -> {
					if (franquicia != null) {
						log.info("onComplete obtenerPorId franquiciaId={}", franquicia.getId());
					}
				});
	}

	@Override
	public Mono<Franquicia> obtenerPorNombre(String nombre) {
		return franquiciaRepositoryPort.findByNombre(nombre)
				.switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Franquicia no encontrada")));
	}

	@Override
	public Mono<Franquicia> crear(Franquicia franquicia) {
		return asegurarIdFranquicia(franquicia)
				.map(franquiciaConId -> {
					if (franquiciaConId.getCreateAt() == null) {
						franquiciaConId.setCreateAt(new Date());
					}
					if (franquiciaConId.getSucursales() == null) {
						franquiciaConId.setSucursales(new ArrayList<>());
					}
					return franquiciaConId;
				})
				.flatMap(franquiciaRepositoryPort::save)
				.doOnNext(saved -> log.info("onNext crear franquiciaId={}", saved.getId()))
				.doOnError(error -> log.error("onError crear franquicia nombre={}", franquicia.getNombre(), error))
				.doOnSuccess(saved -> {
					if (saved != null) {
						log.info("onComplete crear franquiciaId={}", saved.getId());
					}
				});
	}

	@Override
	public Mono<Franquicia> agregarSucursal(String franquiciaId, Sucursal sucursal) {
		return obtenerPorId(franquiciaId)
				.flatMap(franquicia -> asegurarIdSucursal(sucursal)
						.map(sucursalConId -> {
							if (sucursalConId.getCreateAt() == null) {
								sucursalConId.setCreateAt(new Date());
							}
							if (sucursalConId.getProductos() == null) {
								sucursalConId.setProductos(new ArrayList<>());
							}
							if (franquicia.getSucursales() == null) {
								franquicia.setSucursales(new ArrayList<>());
							}
							franquicia.getSucursales().add(sucursalConId);
							return franquicia;
						})
						.flatMap(franquiciaRepositoryPort::save))
				.doOnNext(franquicia -> log.info("onNext agregarSucursal franquiciaId={} sucursalId={}", franquiciaId, sucursal.getId()))
				.doOnError(error -> log.error("onError agregarSucursal franquiciaId={}", franquiciaId, error))
				.doOnSuccess(franquicia -> log.info("onComplete agregarSucursal franquiciaId={}", franquiciaId));
	}

	@Override
	public Mono<Franquicia> agregarProducto(String franquiciaId, String sucursalId, Producto producto) {
		return obtenerPorId(franquiciaId)
				.flatMap(franquicia -> asegurarIdProducto(producto)
						.map(productoConId -> {
							Sucursal sucursal = obtenerSucursal(franquicia, sucursalId);
							if (productoConId.getCreateAt() == null) {
								productoConId.setCreateAt(new Date());
							}
							sucursal.getProductos().add(productoConId);
							return franquicia;
						})
						.flatMap(franquiciaRepositoryPort::save))
				.doOnNext(franquicia -> log.info("onNext agregarProducto franquiciaId={} sucursalId={} productoId={}", franquiciaId, sucursalId, producto.getId()))
				.doOnError(error -> log.error("onError agregarProducto franquiciaId={} sucursalId={}", franquiciaId, sucursalId, error))
				.doOnSuccess(franquicia -> log.info("onComplete agregarProducto franquiciaId={} sucursalId={}", franquiciaId, sucursalId));
	}

	@Override
	public Mono<Franquicia> eliminarProducto(String franquiciaId, String sucursalId, String productoId) {
		return obtenerPorId(franquiciaId)
				.flatMap(franquicia -> {
					Sucursal sucursal = obtenerSucursal(franquicia, sucursalId);
					boolean eliminado = sucursal.getProductos().removeIf(producto -> productoId.equals(producto.getId()));
					if (!eliminado) {
						return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
					}
					return franquiciaRepositoryPort.save(franquicia);
				})
				.doOnNext(franquicia -> log.info("onNext eliminarProducto franquiciaId={} sucursalId={} productoId={}", franquiciaId, sucursalId, productoId))
				.doOnError(error -> log.error("onError eliminarProducto franquiciaId={} sucursalId={} productoId={}", franquiciaId, sucursalId, productoId, error))
				.doOnSuccess(franquicia -> log.info("onComplete eliminarProducto franquiciaId={} sucursalId={} productoId={}", franquiciaId, sucursalId, productoId));
	}

	@Override
	public Mono<Franquicia> actualizarStockProducto(String franquiciaId, String sucursalId, String productoId, int stock) {
		return obtenerPorId(franquiciaId)
				.flatMap(franquicia -> Mono.just(obtenerProducto(franquicia, sucursalId, productoId))
						.map(producto -> {
							producto.setCantidadSlock(stock);
							return franquicia;
						})
						.flatMap(franquiciaRepositoryPort::save))
				.doOnNext(franquicia -> log.info("onNext actualizarStock franquiciaId={} sucursalId={} productoId={} stock={}", franquiciaId, sucursalId, productoId, stock))
				.doOnError(error -> log.error("onError actualizarStock franquiciaId={} sucursalId={} productoId={}", franquiciaId, sucursalId, productoId, error))
				.doOnSuccess(franquicia -> log.info("onComplete actualizarStock franquiciaId={} sucursalId={} productoId={}", franquiciaId, sucursalId, productoId));
	}

	@Override
	public Mono<Franquicia> actualizarNombreFranquicia(String franquiciaId, String nombre) {
		return obtenerPorId(franquiciaId)
				.map(franquicia -> {
					franquicia.setNombre(nombre);
					return franquicia;
				})
				.flatMap(franquiciaRepositoryPort::save)
				.doOnNext(franquicia -> log.info("onNext actualizarNombreFranquicia franquiciaId={} nombre={}", franquiciaId, nombre))
				.doOnError(error -> log.error("onError actualizarNombreFranquicia franquiciaId={}", franquiciaId, error))
				.doOnSuccess(franquicia -> log.info("onComplete actualizarNombreFranquicia franquiciaId={}", franquiciaId));
	}

	@Override
	public Mono<Franquicia> actualizarNombreSucursal(String franquiciaId, String sucursalId, String nombre) {
		return obtenerPorId(franquiciaId)
				.map(franquicia -> {
					obtenerSucursal(franquicia, sucursalId).setNombre(nombre);
					return franquicia;
				})
				.flatMap(franquiciaRepositoryPort::save)
				.doOnNext(franquicia -> log.info("onNext actualizarNombreSucursal franquiciaId={} sucursalId={} nombre={}", franquiciaId, sucursalId, nombre))
				.doOnError(error -> log.error("onError actualizarNombreSucursal franquiciaId={} sucursalId={}", franquiciaId, sucursalId, error))
				.doOnSuccess(franquicia -> log.info("onComplete actualizarNombreSucursal franquiciaId={} sucursalId={}", franquiciaId, sucursalId));
	}

	@Override
	public Mono<Franquicia> actualizarNombreProducto(String franquiciaId, String sucursalId, String productoId, String nombre) {
		return obtenerPorId(franquiciaId)
				.map(franquicia -> {
					obtenerProducto(franquicia, sucursalId, productoId).setNombre(nombre);
					return franquicia;
				})
				.flatMap(franquiciaRepositoryPort::save)
				.doOnNext(franquicia -> log.info("onNext actualizarNombreProducto franquiciaId={} sucursalId={} productoId={} nombre={}", franquiciaId, sucursalId, productoId, nombre))
				.doOnError(error -> log.error("onError actualizarNombreProducto franquiciaId={} sucursalId={} productoId={}", franquiciaId, sucursalId, productoId, error))
				.doOnSuccess(franquicia -> log.info("onComplete actualizarNombreProducto franquiciaId={} sucursalId={} productoId={}", franquiciaId, sucursalId, productoId));
	}

	@Override
	public Flux<ProductoConSucursal> obtenerProductoConMasStockPorSucursal(String franquiciaId) {
		return obtenerPorId(franquiciaId)
				.flatMapMany(franquicia -> {
					List<Sucursal> sucursales = franquicia.getSucursales() == null ? new ArrayList<>() : franquicia.getSucursales();
					Flux<Sucursal> conProductos = Flux.fromIterable(sucursales)
							.filter(sucursal -> sucursal.getProductos() != null && !sucursal.getProductos().isEmpty());
					Flux<Sucursal> sinProductos = Flux.fromIterable(sucursales)
							.filter(sucursal -> sucursal.getProductos() == null || sucursal.getProductos().isEmpty())
							.doOnNext(sucursal -> log.info("Sucursal sin productos franquiciaId={} sucursalId={}", franquiciaId, sucursal.getId()));
					return conProductos.mergeWith(sinProductos).filter(sucursal -> sucursal.getProductos() != null && !sucursal.getProductos().isEmpty());
				})
				.map(sucursal -> sucursal.getProductos().stream()
						.max((productoA, productoB) -> Integer.compare(productoA.getCantidadSlock(), productoB.getCantidadSlock()))
						.map(producto -> new ProductoConSucursal(producto.getId(), producto.getNombre(), producto.getCantidadSlock(), sucursal.getId(), sucursal.getNombre()))
						.orElse(null))
				.filter(producto -> producto != null)
				.doOnNext(producto -> log.info("onNext productoMayorStock franquiciaId={} sucursalId={} productoId={}", franquiciaId, producto.getSucursalId(), producto.getProductoId()))
				.doOnError(error -> log.error("onError productoMayorStock franquiciaId={}", franquiciaId, error))
				.doOnComplete(() -> log.info("onComplete productoMayorStock franquiciaId={}", franquiciaId));
	}

	private Mono<Franquicia> asegurarIdFranquicia(Franquicia franquicia) {
		if (franquicia.getId() != null && !franquicia.getId().trim().isEmpty()) {
			return Mono.just(franquicia);
		}
		return secuenciaPort.siguienteValor("franquicia").map(id -> {
			franquicia.setId(id);
			return franquicia;
		});
	}

	private Mono<Sucursal> asegurarIdSucursal(Sucursal sucursal) {
		if (sucursal.getId() != null && !sucursal.getId().trim().isEmpty()) {
			return Mono.just(sucursal);
		}
		return secuenciaPort.siguienteValor("sucursal").map(id -> {
			sucursal.setId(id);
			return sucursal;
		});
	}

	private Mono<Producto> asegurarIdProducto(Producto producto) {
		if (producto.getId() != null && !producto.getId().trim().isEmpty()) {
			return Mono.just(producto);
		}
		return secuenciaPort.siguienteValor("producto").map(id -> {
			producto.setId(id);
			return producto;
		});
	}

	private Sucursal obtenerSucursal(Franquicia franquicia, String sucursalId) {
		List<Sucursal> sucursales = franquicia.getSucursales() == null ? new ArrayList<>() : franquicia.getSucursales();
		Optional<Sucursal> sucursal = sucursales.stream().filter(item -> sucursalId.equals(item.getId())).findFirst();
		return sucursal.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sucursal no encontrada"));
	}

	private Producto obtenerProducto(Franquicia franquicia, String sucursalId, String productoId) {
		return obtenerSucursal(franquicia, sucursalId).getProductos().stream()
				.filter(item -> productoId.equals(item.getId()))
				.findFirst()
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
	}
}
