package com.vanegas.angela.franquicias_api.infrastruture.adapters.in.web;

import com.vanegas.angela.franquicias_api.application.port.in.ProductoUseCase;
import com.vanegas.angela.franquicias_api.domain.model.Producto;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
public class ProductoController {

    private final ProductoUseCase productoUseCase;

    public ProductoController(ProductoUseCase productoUseCase) {
        this.productoUseCase = productoUseCase;
    }

    public Mono<ServerResponse> listar(ServerRequest request) {
        return ServerResponse.ok().contentType(APPLICATION_JSON)
                .body(productoUseCase.listar(), Producto.class);
    }

    public Mono<ServerResponse> verProducto(ServerRequest request) {
        String id = request.pathVariable("id");
        return errorHandler(
                productoUseCase.obtenerPorId(id)
                        .flatMap(p -> ServerResponse.ok().contentType(APPLICATION_JSON).bodyValue(p))
                        .switchIfEmpty(ServerResponse.notFound().build())
        );
    }

    public Mono<ServerResponse> editarProducto(ServerRequest request) {
        String id = request.pathVariable("id");
        return request.bodyToMono(Producto.class)
                .flatMap(body -> productoUseCase.actualizar(id, body))
                .flatMap(p -> ServerResponse.created(URI.create("/api/producto/".concat(p.getId())))
                        .contentType(APPLICATION_JSON)
                        .bodyValue(p));
    }

    public Mono<ServerResponse> eliminarProducto(ServerRequest request) {
        String id = request.pathVariable("id");
        return productoUseCase.eliminar(id)
                .then(ServerResponse.noContent().build())
                .switchIfEmpty(ServerResponse.notFound().build());
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

    public Mono<ServerResponse> guardarProducto(ServerRequest request) {
        return errorHandler(
                request.bodyToMono(Producto.class)
                        .flatMap(productoUseCase::crear)
                        .flatMap(p -> ServerResponse.created(URI.create("/api/producto/".concat(p.getId())))
                                .contentType(APPLICATION_JSON)
                                .bodyValue(p))
        );
    }
}
