package com.vanegas.angela.franquicias_api;

import com.vanegas.angela.franquicias_api.application.port.out.SecuenciaPort;
import com.vanegas.angela.franquicias_api.application.port.out.SucursalRepositoryPort;
import com.vanegas.angela.franquicias_api.application.service.SucursalApplicationService;
import com.vanegas.angela.franquicias_api.domain.model.Sucursal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SucursalControllerTest {

    @Mock
    private SucursalRepositoryPort sucursalRepositoryPort;

    @Mock
    private SecuenciaPort secuenciaPort;

    private SucursalApplicationService service;

    @BeforeEach
    void setUp() {
        service = new SucursalApplicationService(sucursalRepositoryPort, secuenciaPort);
    }

    @Test
    void crearDebeGenerarId() {
        Sucursal sucursal = new Sucursal();
        sucursal.setNombre("Centro");
        when(secuenciaPort.siguienteValor("sucursal")).thenReturn(Mono.just("20"));
        when(sucursalRepositoryPort.save(any(Sucursal.class))).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        StepVerifier.create(service.crear(sucursal))
                .expectNextMatches(saved -> "20".equals(saved.getId()) && "Centro".equals(saved.getNombre()))
                .verifyComplete();
    }
}
