package com.vanegas.angela.franquicias_api;

import com.vanegas.angela.franquicias_api.application.port.out.ProductoRepositoryPort;
import com.vanegas.angela.franquicias_api.application.port.out.SecuenciaPort;
import com.vanegas.angela.franquicias_api.application.service.ProductoApplicationService;
import com.vanegas.angela.franquicias_api.domain.model.Producto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductoControllerTest {

    @Mock
    private ProductoRepositoryPort productoRepositoryPort;

    @Mock
    private SecuenciaPort secuenciaPort;

    private ProductoApplicationService service;

    @BeforeEach
    void setUp() {
        service = new ProductoApplicationService(productoRepositoryPort, secuenciaPort);
    }

    @Test
    void crearDebeAsignarIdCuandoNoExiste() {
        Producto producto = new Producto();
        producto.setNombre("Cargador");
        when(secuenciaPort.siguienteValor("producto")).thenReturn(Mono.just("11"));
        when(productoRepositoryPort.save(any(Producto.class))).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        StepVerifier.create(service.crear(producto))
                .expectNextMatches(saved -> "11".equals(saved.getId()) && "Cargador".equals(saved.getNombre()))
                .verifyComplete();
    }

    @Test
    void listarDebeRetornarFlux() {
        Producto p1 = new Producto();
        p1.setId("1");
        Producto p2 = new Producto();
        p2.setId("2");
        when(productoRepositoryPort.findAll()).thenReturn(Flux.just(p1, p2));

        StepVerifier.create(service.listar())
                .expectNextCount(2)
                .verifyComplete();
    }
}
