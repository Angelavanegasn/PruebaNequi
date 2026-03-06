package com.vanegas.angela.franquicias_api.infrastruture.adapters.out.mongo.document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "franquicias")
public class FranquiciaDocument {

    @Id
    private String id;

    private String nombre;

    private Date createAt = new Date();

    private List<SucursalDocument> sucursales = new ArrayList<>();

    public FranquiciaDocument() {}

    public FranquiciaDocument(String nombre) {
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

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public List<SucursalDocument> getSucursales() {
        return sucursales;
    }

    public void setSucursales(List<SucursalDocument> sucursales) {
        this.sucursales = sucursales;
    }
}
