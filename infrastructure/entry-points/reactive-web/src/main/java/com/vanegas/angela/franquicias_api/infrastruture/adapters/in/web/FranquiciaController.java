package com.vanegas.angela.franquicias_api.infrastruture.adapters.in.web;

import com.vanegas.angela.franquicias_api.application.dto.ProductoConSucursal;
import com.vanegas.angela.franquicias_api.application.port.in.FranquiciaUseCase;
import com.vanegas.angela.franquicias_api.domain.model.Franquicia;
import com.vanegas.angela.franquicias_api.domain.model.Producto;
import com.vanegas.angela.franquicias_api.domain.model.Sucursal;
import com.vanegas.angela.franquicias_api.infrastruture.adapters.in.web.dto.ActualizarNombreRequest;
import com.vanegas.angela.franquicias_api.infrastruture.adapters.in.web.dto.ActualizarStockRequest;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
public class FranquiciaController {

    private static final Logger log = LoggerFactory.getLogger(FranquiciaController.class);

    private final FranquiciaUseCase franquiciaUseCase;

    public FranquiciaController(FranquiciaUseCase franquiciaUseCase) {
        this.franquiciaUseCase = franquiciaUseCase;
    }

    private Mono<ServerResponse> errorHandler(Mono<ServerResponse> response) {
        return response.onErrorResume(ResponseStatusException.class, error -> {
            Map<String, Object> body = new HashMap<>();
            body.put("error", error.getReason());
            body.put("timestamp", new Date());
            body.put("status", error.getStatusCode().value());
            return ServerResponse.status(error.getStatusCode()).contentType(APPLICATION_JSON).bodyValue(body);
        });
    }

    public Mono<ServerResponse> guardarfranquicia(ServerRequest request) {
        Mono<Franquicia> franquicia = request.bodyToMono(Franquicia.class);
        return errorHandler(
                franquicia
                        .doOnNext(body -> log.info("onNext request guardarfranquicia nombre={}", body.getNombre()))
                        .flatMap(franquiciaUseCase::crear)
                        .flatMap(p -> ServerResponse.created(URI.create("/api/franquicia/".concat(p.getId())))
                                .contentType(APPLICATION_JSON)
                                .bodyValue(p))
                        .doOnSuccess(response -> log.info("onComplete guardarfranquicia"))
                        .doOnError(error -> log.error("onError guardarfranquicia", error))
        );
    }

    public Mono<ServerResponse> listarfranquicia(ServerRequest request) {
        return ServerResponse.ok().contentType(APPLICATION_JSON)
                .body(franquiciaUseCase.listar(), Franquicia.class);
    }

    public Mono<ServerResponse> verFranquicia(ServerRequest request) {
        String id = request.pathVariable("id");
        return errorHandler(
                franquiciaUseCase.obtenerPorId(id)
                        .flatMap(p -> ServerResponse.ok().contentType(APPLICATION_JSON).bodyValue(p))
                        .switchIfEmpty(ServerResponse.notFound().build())
        );
    }

    public Mono<ServerResponse> agregarSucursal(ServerRequest request) {
        String franquiciaId = request.pathVariable("id");

        return errorHandler(request.bodyToMono(Sucursal.class)
                .doOnNext(sucursal -> log.info("onNext request agregarSucursal franquiciaId={} nombre={}", franquiciaId, sucursal.getNombre()))
                .flatMap(sucursal -> franquiciaUseCase.agregarSucursal(franquiciaId, sucursal))
                .flatMap(updated -> ServerResponse.status(HttpStatus.CREATED).contentType(APPLICATION_JSON).bodyValue(updated)));
    }

    public Mono<ServerResponse> agregarProducto(ServerRequest request) {
        String franquiciaId = request.pathVariable("franquiciaId");
        String sucursalId = request.pathVariable("sucursalId");

        return errorHandler(request.bodyToMono(Producto.class)
                .doOnNext(producto -> log.info("onNext request agregarProducto franquiciaId={} sucursalId={} nombre={}", franquiciaId, sucursalId, producto.getNombre()))
                .flatMap(producto -> franquiciaUseCase.agregarProducto(franquiciaId, sucursalId, producto))
                .flatMap(updated -> ServerResponse.status(HttpStatus.CREATED).contentType(APPLICATION_JSON).bodyValue(updated)));
    }

    public Mono<ServerResponse> eliminarProductoSucursal(ServerRequest request) {
        String franquiciaId = request.pathVariable("franquiciaId");
        String sucursalId = request.pathVariable("sucursalId");
        String productoId = request.pathVariable("productoId");

        return errorHandler(franquiciaUseCase.eliminarProducto(franquiciaId, sucursalId, productoId)
                .flatMap(updated -> ServerResponse.ok().contentType(APPLICATION_JSON).bodyValue(updated)));
    }

    public Mono<ServerResponse> modificarStockProducto(ServerRequest request) {
        String franquiciaId = request.pathVariable("franquiciaId");
        String sucursalId = request.pathVariable("sucursalId");
        String productoId = request.pathVariable("productoId");

        return errorHandler(request.bodyToMono(ActualizarStockRequest.class)
                .zipWith(franquiciaUseCase.obtenerPorId(franquiciaId), (body, franquicia) -> body)
                .flatMap(body -> franquiciaUseCase.actualizarStockProducto(franquiciaId, sucursalId, productoId, body.getStock()))
                .flatMap(updated -> ServerResponse.ok().contentType(APPLICATION_JSON).bodyValue(updated)));
    }

    public Mono<ServerResponse> modificarNombreFranquicia(ServerRequest request) {
        String franquiciaId = request.pathVariable("id");

        return errorHandler(request.bodyToMono(ActualizarNombreRequest.class)
                .zipWith(franquiciaUseCase.obtenerPorId(franquiciaId), (body, franquicia) -> body)
                .flatMap(body -> franquiciaUseCase.actualizarNombreFranquicia(franquiciaId, body.getNombre()))
                .flatMap(updated -> ServerResponse.ok().contentType(APPLICATION_JSON).bodyValue(updated)));
    }

    public Mono<ServerResponse> modificarNombreSucursal(ServerRequest request) {
        String franquiciaId = request.pathVariable("franquiciaId");
        String sucursalId = request.pathVariable("sucursalId");

        return errorHandler(request.bodyToMono(ActualizarNombreRequest.class)
                .zipWith(franquiciaUseCase.obtenerPorId(franquiciaId), (body, franquicia) -> body)
                .flatMap(body -> franquiciaUseCase.actualizarNombreSucursal(franquiciaId, sucursalId, body.getNombre()))
                .flatMap(updated -> ServerResponse.ok().contentType(APPLICATION_JSON).bodyValue(updated)));
    }

    public Mono<ServerResponse> modificarNombreProducto(ServerRequest request) {
        String franquiciaId = request.pathVariable("franquiciaId");
        String sucursalId = request.pathVariable("sucursalId");
        String productoId = request.pathVariable("productoId");

        return errorHandler(request.bodyToMono(ActualizarNombreRequest.class)
                .zipWith(franquiciaUseCase.obtenerPorId(franquiciaId), (body, franquicia) -> body)
                .flatMap(body -> franquiciaUseCase.actualizarNombreProducto(franquiciaId, sucursalId, productoId, body.getNombre()))
                .flatMap(updated -> ServerResponse.ok().contentType(APPLICATION_JSON).bodyValue(updated)));
    }

    public Mono<ServerResponse> listarProductoMayorStock(ServerRequest request) {
        String franquiciaId = request.pathVariable("id");
        return errorHandler(ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(franquiciaUseCase.obtenerProductoConMasStockPorSucursal(franquiciaId), ProductoConSucursal.class));
    }
}
