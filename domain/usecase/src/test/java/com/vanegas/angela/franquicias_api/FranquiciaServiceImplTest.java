package com.vanegas.angela.franquicias_api;

import com.vanegas.angela.franquicias_api.application.dto.ProductoConSucursal;
import com.vanegas.angela.franquicias_api.application.port.out.FranquiciaRepositoryPort;
import com.vanegas.angela.franquicias_api.application.port.out.SecuenciaPort;
import com.vanegas.angela.franquicias_api.application.service.FranquiciaApplicationService;
import com.vanegas.angela.franquicias_api.domain.model.Franquicia;
import com.vanegas.angela.franquicias_api.domain.model.Producto;
import com.vanegas.angela.franquicias_api.domain.model.Sucursal;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FranquiciaServiceImplTest {

    @Mock
    private FranquiciaRepositoryPort franquiciaDao;

    @Mock
    private SecuenciaPort secuenciaService;

    private FranquiciaApplicationService service;

    @BeforeEach
    void setUp() {
        service = new FranquiciaApplicationService(franquiciaDao, secuenciaService);
    }

    @Test
    void debeAgregarSucursalYProductoYActualizarStock() {
        when(franquiciaDao.save(any(Franquicia.class))).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        Franquicia franquicia = new Franquicia("Acme");
        franquicia.setId("fr-1");
        franquicia.setSucursales(new ArrayList<>());

        when(franquiciaDao.findById("fr-1")).thenReturn(Mono.just(franquicia));

        Sucursal sucursal = new Sucursal();
        sucursal.setId("su-1");
        sucursal.setNombre("Centro");

        Producto producto = new Producto();
        producto.setId("pr-1");
        producto.setNombre("Teclado");
        producto.setCantidadSlock(12);

        StepVerifier.create(service.agregarSucursal("fr-1", sucursal)
                        .flatMap(actualizada -> service.agregarProducto(actualizada.getId(), "su-1", producto))
                        .flatMap(actualizada -> service.actualizarStockProducto(actualizada.getId(), "su-1", "pr-1", 25)))
                .assertNext(actualizada -> {
                    Sucursal sucursalActualizada = actualizada.getSucursales().get(0);
                    Producto productoActualizado = sucursalActualizada.getProductos().get(0);
                    assertEquals("Centro", sucursalActualizada.getNombre());
                    assertEquals("Teclado", productoActualizado.getNombre());
                    assertEquals(25, productoActualizado.getCantidadSlock());
                })
                .verifyComplete();
    }

    @Test
    void debeActualizarNombresDeFranquiciaSucursalYProducto() {
        when(franquiciaDao.save(any(Franquicia.class))).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        Producto producto = new Producto();
        producto.setId("pr-1");
        producto.setNombre("Teclado");
        producto.setCantidadSlock(12);

        Sucursal sucursal = new Sucursal();
        sucursal.setId("su-1");
        sucursal.setNombre("Centro");
        sucursal.setProductos(new ArrayList<>(Arrays.asList(producto)));

        Franquicia franquicia = new Franquicia("Acme");
        franquicia.setId("fr-1");
        franquicia.setSucursales(new ArrayList<>(Arrays.asList(sucursal)));

        when(franquiciaDao.findById("fr-1")).thenReturn(Mono.just(franquicia));

        StepVerifier.create(service.actualizarNombreFranquicia("fr-1", "Acme Plus")
                        .flatMap(actualizada -> service.actualizarNombreSucursal(actualizada.getId(), "su-1", "Norte"))
                        .flatMap(actualizada -> service.actualizarNombreProducto(actualizada.getId(), "su-1", "pr-1", "Teclado Pro")))
                .assertNext(actualizada -> {
                    assertEquals("Acme Plus", actualizada.getNombre());
                    assertEquals("Norte", actualizada.getSucursales().get(0).getNombre());
                    assertEquals("Teclado Pro", actualizada.getSucursales().get(0).getProductos().get(0).getNombre());
                })
                .verifyComplete();
    }

    @Test
    void debeRetornarProductoConMasStockPorSucursal() {
        Producto monitor = new Producto();
        monitor.setId("pr-1");
        monitor.setNombre("Monitor");
        monitor.setCantidadSlock(30);

        Producto mouse = new Producto();
        mouse.setId("pr-2");
        mouse.setNombre("Mouse");
        mouse.setCantidadSlock(10);

        Sucursal centro = new Sucursal();
        centro.setId("su-1");
        centro.setNombre("Centro");
        centro.setProductos(new ArrayList<>(Arrays.asList(mouse, monitor)));

        Producto laptop = new Producto();
        laptop.setId("pr-3");
        laptop.setNombre("Laptop");
        laptop.setCantidadSlock(50);

        Sucursal norte = new Sucursal();
        norte.setId("su-2");
        norte.setNombre("Norte");
        norte.setProductos(new ArrayList<>(Arrays.asList(laptop)));

        Franquicia franquicia = new Franquicia("Acme");
        franquicia.setId("fr-1");
        franquicia.setSucursales(new ArrayList<>(Arrays.asList(centro, norte)));

        when(franquiciaDao.findById("fr-1")).thenReturn(Mono.just(franquicia));

        StepVerifier.create(service.obtenerProductoConMasStockPorSucursal("fr-1"))
                .assertNext(producto -> validarProducto(producto, "Monitor", 30, "Centro"))
                .assertNext(producto -> validarProducto(producto, "Laptop", 50, "Norte"))
                .verifyComplete();
    }

    private void validarProducto(ProductoConSucursal producto, String nombre, int stock, String sucursal) {
        assertEquals(nombre, producto.getProductoNombre());
        assertEquals(stock, producto.getStock());
        assertEquals(sucursal, producto.getSucursalNombre());
    }
}
