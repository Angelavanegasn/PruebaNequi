package com.vanegas.angela.franquicias_api.infrastruture.adapters.in.web;

import com.vanegas.angela.franquicias_api.application.port.in.SucursalUseCase;
import com.vanegas.angela.franquicias_api.domain.model.Sucursal;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
public class SucursaController {

    private final SucursalUseCase sucursalUseCase;

    public SucursaController(SucursalUseCase sucursalUseCase) {
        this.sucursalUseCase = sucursalUseCase;
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

    public Mono<ServerResponse> guardarSucursal(ServerRequest request) {
        return errorHandler(
                request.bodyToMono(Sucursal.class)
                        .flatMap(sucursalUseCase::crear)
                        .flatMap(p -> ServerResponse.created(URI.create("/api/sucursal/".concat(p.getId())))
                                .contentType(APPLICATION_JSON)
                                .bodyValue(p))
        );
    }

    public Mono<ServerResponse> verSucursal(ServerRequest request) {
        String id = request.pathVariable("id");
        return errorHandler(
                sucursalUseCase.obtenerPorId(id)
                        .flatMap(p -> ServerResponse.ok().contentType(APPLICATION_JSON).bodyValue(p))
                        .switchIfEmpty(ServerResponse.notFound().build())
        );
    }

    public Mono<ServerResponse> listarSucursal(ServerRequest request) {
        return ServerResponse.ok().contentType(APPLICATION_JSON)
                .body(sucursalUseCase.listar(), Sucursal.class);
    }
}
