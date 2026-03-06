package com.vanegas.angela.franquicias_api.domain.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Franquicia {
    
	private String id;
	private String nombre;
	private Date createAt;
	private List<Sucursal> sucursales = new ArrayList<>();

	public Franquicia() {
	}

	public Franquicia(String nombre) {
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

	public List<Sucursal> getSucursales() {
		return sucursales;
	}

	public void setSucursales(List<Sucursal> sucursales) {
		this.sucursales = sucursales;
	}
}

