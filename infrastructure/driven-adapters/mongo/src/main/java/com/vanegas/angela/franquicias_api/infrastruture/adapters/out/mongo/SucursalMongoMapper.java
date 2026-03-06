package com.vanegas.angela.franquicias_api.infrastruture.adapters.out.mongo;

import com.vanegas.angela.franquicias_api.domain.model.Producto;
import com.vanegas.angela.franquicias_api.domain.model.Sucursal;
import com.vanegas.angela.franquicias_api.infrastruture.adapters.out.mongo.document.ProductoDocument;
import com.vanegas.angela.franquicias_api.infrastruture.adapters.out.mongo.document.SucursalDocument;
import java.util.ArrayList;
import org.springframework.stereotype.Component;

@Component
public class SucursalMongoMapper {

    public Sucursal toDomain(SucursalDocument document) {
        Sucursal sucursal = new Sucursal();
        sucursal.setId(document.getId());
        sucursal.setNombre(document.getNombre());
        sucursal.setCreateAt(document.getCreateAt());
        sucursal.setProductos(new ArrayList<>());
        if (document.getProductos() != null) {
            document.getProductos().forEach(item -> {
                Producto producto = new Producto();
                producto.setId(item.getId());
                producto.setNombre(item.getNombre());
                producto.setCantidadSlock(item.getCantidadSlock());
                producto.setCreateAt(item.getCreateAt());
                sucursal.getProductos().add(producto);
            });
        }
        return sucursal;
    }

    public SucursalDocument toDocument(Sucursal domain) {
        SucursalDocument sucursal = new SucursalDocument();
        sucursal.setId(domain.getId());
        sucursal.setNombre(domain.getNombre());
        sucursal.setCreateAt(domain.getCreateAt());
        sucursal.setProductos(new ArrayList<>());
        if (domain.getProductos() != null) {
            domain.getProductos().forEach(item -> {
                ProductoDocument producto = new ProductoDocument();
                producto.setId(item.getId());
                producto.setNombre(item.getNombre());
                producto.setCantidadSlock(item.getCantidadSlock());
                producto.setCreateAt(item.getCreateAt());
                sucursal.getProductos().add(producto);
            });
        }
        return sucursal;
    }
}
