package com.vanegas.angela.franquicias_api.infrastruture.adapters.out.mongo.document;

import java.util.Date;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "productos")
public class ProductoDocument {

    @Id
    private String id;

    private String nombre;

    private SucursalDocument sucursal;

    private int cantidadSlock;

    private Date createAt;

    public ProductoDocument() {}

    public ProductoDocument(String nombre, SucursalDocument sucursal, int cantidadSlock) {
        this.nombre = nombre;
        this.sucursal = sucursal;
        this.cantidadSlock = cantidadSlock;
        this.createAt = new Date();
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

    public SucursalDocument getSucursal() {
        return sucursal;
    }

    public void setSucursal(SucursalDocument sucursal) {
        this.sucursal = sucursal;
    }

    public int getCantidadSlock() {
        return cantidadSlock;
    }

    public void setCantidadSlock(int cantidadSlock) {
        this.cantidadSlock = cantidadSlock;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }
}
