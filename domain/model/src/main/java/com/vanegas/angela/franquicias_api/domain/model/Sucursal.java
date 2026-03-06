package com.vanegas.angela.franquicias_api.domain.model;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Sucursal {
    
private String id;
	private String nombre;
	private List<Producto> productos = new ArrayList<>();
	private Date createAt;

	public Sucursal() {
	}

	public Sucursal(String nombre) {
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

	public List<Producto> getProductos() {
		return productos;
	}

	public void setProductos(List<Producto> productos) {
		this.productos = productos;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}
}
