package com.vanegas.angela.franquicias_api.infrastruture.adapters.out.mongo.document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "sucursales")
public class SucursalDocument {

    @Id
    private String id;

    private String nombre;

    private List<ProductoDocument> productos = new ArrayList<>();

    private Date createAt;

    public SucursalDocument() {}

    public SucursalDocument(String nombre, FranquiciaDocument franquicia) {
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<ProductoDocument> getProductos() {
        return productos;
    }

    public void setProductos(List<ProductoDocument> productos) {
        this.productos = productos;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }
}
