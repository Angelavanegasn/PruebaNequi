package com.vanegas.angela.franquicias_api.infrastruture.adapters.out.mongo;

import com.vanegas.angela.franquicias_api.domain.model.Producto;
import com.vanegas.angela.franquicias_api.domain.model.Sucursal;
import com.vanegas.angela.franquicias_api.infrastruture.adapters.out.mongo.document.ProductoDocument;
import com.vanegas.angela.franquicias_api.infrastruture.adapters.out.mongo.document.SucursalDocument;
import java.util.ArrayList;
import org.springframework.stereotype.Component;

@Component
public class ProductoMongoMapper {

    public Producto toDomain(ProductoDocument document) {
        Producto producto = new Producto();
        producto.setId(document.getId());
        producto.setNombre(document.getNombre());
        producto.setCantidadSlock(document.getCantidadSlock());
        producto.setCreateAt(document.getCreateAt());

        if (document.getSucursal() != null) {
            Sucursal sucursal = new Sucursal();
            sucursal.setId(document.getSucursal().getId());
            sucursal.setNombre(document.getSucursal().getNombre());
            sucursal.setCreateAt(document.getSucursal().getCreateAt());
            sucursal.setProductos(new ArrayList<>());
            producto.setSucursal(sucursal);
        }
        return producto;
    }

    public ProductoDocument toDocument(Producto domain) {
        ProductoDocument producto = new ProductoDocument();
        producto.setId(domain.getId());
        producto.setNombre(domain.getNombre());
        producto.setCantidadSlock(domain.getCantidadSlock());
        producto.setCreateAt(domain.getCreateAt());

        if (domain.getSucursal() != null) {
            SucursalDocument sucursal = new SucursalDocument();
            sucursal.setId(domain.getSucursal().getId());
            sucursal.setNombre(domain.getSucursal().getNombre());
            sucursal.setCreateAt(domain.getSucursal().getCreateAt());
            sucursal.setProductos(new ArrayList<>());
            producto.setSucursal(sucursal);
        }
        return producto;
    }
}
