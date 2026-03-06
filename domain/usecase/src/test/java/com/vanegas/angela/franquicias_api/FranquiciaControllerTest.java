package com.vanegas.angela.franquicias_api;

import com.vanegas.angela.franquicias_api.application.port.out.FranquiciaRepositoryPort;
import com.vanegas.angela.franquicias_api.application.port.out.SecuenciaPort;
import com.vanegas.angela.franquicias_api.application.service.FranquiciaApplicationService;
import com.vanegas.angela.franquicias_api.domain.model.Franquicia;
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
class FranquiciaControllerTest {

    @Mock
    private FranquiciaRepositoryPort franquiciaRepositoryPort;

    @Mock
    private SecuenciaPort secuenciaPort;

    private FranquiciaApplicationService service;

    @BeforeEach
    void setUp() {
        service = new FranquiciaApplicationService(franquiciaRepositoryPort, secuenciaPort);
    }

    @Test
    void crearDebeGuardarFranquicia() {
        Franquicia franquicia = new Franquicia("Franquicia A");
        when(secuenciaPort.siguienteValor("franquicia")).thenReturn(Mono.just("1"));
        when(franquiciaRepositoryPort.save(any(Franquicia.class))).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        StepVerifier.create(service.crear(franquicia))
                .expectNextMatches(saved -> "1".equals(saved.getId()) && "Franquicia A".equals(saved.getNombre()))
                .verifyComplete();
    }

    @Test
    void listarDebeRetornarElementos() {
        Franquicia f1 = new Franquicia("A");
        Franquicia f2 = new Franquicia("B");
        when(franquiciaRepositoryPort.findAll()).thenReturn(Flux.just(f1, f2));

        StepVerifier.create(service.listar())
                .expectNextCount(2)
                .verifyComplete();
    }
}
