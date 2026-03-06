package com.vanegas.angela.franquicias_api.infrastruture.adapters.out.mongo;

import com.vanegas.angela.franquicias_api.domain.model.Franquicia;
import com.vanegas.angela.franquicias_api.domain.model.Producto;
import com.vanegas.angela.franquicias_api.domain.model.Sucursal;
import com.vanegas.angela.franquicias_api.infrastruture.adapters.out.mongo.document.FranquiciaDocument;
import com.vanegas.angela.franquicias_api.infrastruture.adapters.out.mongo.document.ProductoDocument;
import com.vanegas.angela.franquicias_api.infrastruture.adapters.out.mongo.document.SucursalDocument;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class FranquiciaMongoMapper {

    public Franquicia toDomain(FranquiciaDocument document) {
        Franquicia franquicia = new Franquicia();
        franquicia.setId(document.getId());
        franquicia.setNombre(document.getNombre());
        franquicia.setCreateAt(document.getCreateAt());
        franquicia.setSucursales(toDomainSucursales(document.getSucursales()));
        return franquicia;
    }

    public FranquiciaDocument toDocument(Franquicia domain) {
        FranquiciaDocument franquicia = new FranquiciaDocument();
        franquicia.setId(domain.getId());
        franquicia.setNombre(domain.getNombre());
        franquicia.setCreateAt(domain.getCreateAt());
        franquicia.setSucursales(toDocumentSucursales(domain.getSucursales()));
        return franquicia;
    }

    private List<Sucursal> toDomainSucursales(List<SucursalDocument> sucursales) {
        List<Sucursal> resultado = new ArrayList<>();
        if (sucursales == null) {
            return resultado;
        }
        for (SucursalDocument sucursal : sucursales) {
            Sucursal domain = new Sucursal();
            domain.setId(sucursal.getId());
            domain.setNombre(sucursal.getNombre());
            domain.setCreateAt(sucursal.getCreateAt());
            domain.setProductos(toDomainProductos(sucursal.getProductos()));
            resultado.add(domain);
        }
        return resultado;
    }

    private List<SucursalDocument> toDocumentSucursales(List<Sucursal> sucursales) {
        List<SucursalDocument> resultado = new ArrayList<>();
        if (sucursales == null) {
            return resultado;
        }
        for (Sucursal sucursal : sucursales) {
            SucursalDocument document = new SucursalDocument();
            document.setId(sucursal.getId());
            document.setNombre(sucursal.getNombre());
            document.setCreateAt(sucursal.getCreateAt());
            document.setProductos(toDocumentProductos(sucursal.getProductos()));
            resultado.add(document);
        }
        return resultado;
    }

    private List<Producto> toDomainProductos(List<ProductoDocument> productos) {
        List<Producto> resultado = new ArrayList<>();
        if (productos == null) {
            return resultado;
        }
        for (ProductoDocument producto : productos) {
            Producto domain = new Producto();
            domain.setId(producto.getId());
            domain.setNombre(producto.getNombre());
            domain.setCantidadSlock(producto.getCantidadSlock());
            domain.setCreateAt(producto.getCreateAt());
            resultado.add(domain);
        }
        return resultado;
    }

    private List<ProductoDocument> toDocumentProductos(List<Producto> productos) {
        List<ProductoDocument> resultado = new ArrayList<>();
        if (productos == null) {
            return resultado;
        }
        for (Producto producto : productos) {
            ProductoDocument document = new ProductoDocument();
            document.setId(producto.getId());
            document.setNombre(producto.getNombre());
            document.setCantidadSlock(producto.getCantidadSlock());
            document.setCreateAt(producto.getCreateAt());
            resultado.add(document);
        }
        return resultado;
    }
}
